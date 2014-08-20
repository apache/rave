/*
 * category
 * Handles submission of our form
 *
 */

define(function(require) {
  var $ = require('jquery');
  
  return ['$scope', 'categoryResource', '$state', '$stateParams', 'category', 'categoriesMessages',
  function($scope, categoryResource, $state, $stateParams, category, categoriesMessages) {
    $scope.category = category;

    $scope.category.$promise.then(function(res) {
      $scope.text = res.text;
    }).catch(function(err) {
    });

    // Whether or not the user has updated the text of the category
    $scope.isUpdated = function() {
      return $scope.text !== $scope.category.text;
    };

    // Remove the category from the list of categories in the scope
    this.removeFromList = function() {
      var oldCategory = _.findWhere($scope.categories, {ID:+$stateParams.id});
      var oldIndex = _.indexOf($scope.categories, oldCategory);
      $scope.categories.splice(oldIndex, 1);
    };

    // Replace the old item in the list with the new
    this.updateList = function(newResource) {
      var oldCategory = _.findWhere($scope.categories, {ID:+$stateParams.id});
      var oldIndex = _.indexOf($scope.categories, oldCategory);
      $scope.categories[oldIndex] = newResource;
      $scope.category = newResource;
    };

    var ctrl = this;

    $scope.onSave = function() {

      var savedResource = categoryResource.update({
        id: $stateParams.id,
        text: $scope.text
      });
      
      savedResource.$promise
        .then(function(updatedResource) {
          categoriesMessages.updateMessage({
            newText: $scope.text,
            oldText: $scope.category.text
          });
          ctrl.updateList(updatedResource);
          $state.transitionTo('portal.admin.categories');
        })
        .catch(function(err) {
          categoriesMessages.errorMessage(err.data.message);
        });
    };

    $scope.onDelete = function() {
      var deletedResource = categoryResource.delete({
        id: $stateParams.id
      });

      deletedResource.$promise
        .then(function() {
          categoriesMessages.deleteMessage($scope.category.text);
          ctrl.removeFromList();
          $('#confirm-modal').modal('hide');
          $state.transitionTo('portal.admin.categories');
        })
        .catch(function(err) {
          categoriesMessages.errorMessage(err.data.message);
        });
    };
  }];
});
