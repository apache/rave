/*
 * category
 * Handles submission of our form
 *
 */

define(function(require) {
  var $ = require('jquery');
  
  return ['$scope', 'categoryResource', '$state', '$stateParams', 'category',
  function($scope, categoryResource, $state, $stateParams, category) {
    $scope.category = category;

    $scope.category.$promise.then(function(res) {
      $scope.text = res.text;
    }).catch(function(err) {
    });

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
        .then(ctrl.updateList)
        .catch(function() {
        });
    };

    $scope.onDelete = function() {
      var deletedResource = categoryResource.delete({
        id: $stateParams.id
      });

      deletedResource.$promise
        .then(function() {
          ctrl.removeFromList();
          $('#confirm-modal').modal('hide');
          $state.transitionTo('portal.admin.categories');
        })
        .catch(function() {
        });
    };
  }];
});
