/*
 * widget
 * Handles submission of our form
 *
 */

define(function(require) {
  var $ = require('jquery');

  return ['$scope', 'widgetResource', '$state', '$stateParams', 'widget', 'widgetsMessages',
  function($scope, widgetResource, $state, $stateParams, widget, widgetsMessages) {

    $scope.widget = widget;

    // The values to merge onto this scope once the promise resolves
    var keys = [
      'title',
      'titleUrl',
      'url',
      'thumbnailUrl',
      'screenshotUrl',
      'type',
      'author',
      'authorEmail',
      'description',
      'status',
      'properties',
      'disable',
      'disabledMessage',
      'featured',
      'categoriesList',
      'categories'
    ];

    widget.$promise.then(function(res) {
      _.extend($scope, _.pick(res, keys));

      // This sets the selected categories on the scope based on what's returned
      // by the server.
      $scope.selectedCategories = _.filter($scope.categoriesList, function(category) {
        return _.contains($scope.categories, category.text);
      });

    }).catch(function(err) {
    });

    // Remove the widget from the list of widget in the scope
    this.removeFromList = function() {
      var oldUser = _.findWhere($scope.widget, {ID:+$stateParams.id});
      var oldIndex = _.indexOf($scope.widget, oldUser);
      $scope.widgets.splice(oldIndex, 1);
    };

    // Replace the old item in the list with the new
    this.updateList = function(newResource) {
      var oldUser = _.findWhere($scope.widgets, {ID:+$stateParams.id});
      var oldIndex = _.indexOf($scope.widgets, oldUser);
      $scope.widgets[oldIndex] = newResource;
      $scope.widget = newResource;
    };

    var ctrl = this;

    // Keys that are present in what we receive from the server
    // that we do not want to send back to the server
    var blacklistedKeys = [
      'categoriesList'
    ];

    $scope.onSave = function() {
      var data = _.pick($scope, _.difference(keys, blacklistedKeys));
      data.categories = _.pluck($scope.selectedCategories, 'text');
      data.id = $stateParams.id;
      
      var savedResource = widgetResource.update(data);
      
      savedResource.$promise
        .then(function(response) {
          ctrl.updateList(response);
          widgetsMessages.updateMessage(response.widgetname);
          $state.transitionTo('portal.admin.widgets');
        })
        .catch(function(err) {
        });
    };

    $scope.onDelete = function() {
      var deletedResource = widgetResource.delete({
        id: $stateParams.id
      });

      deletedResource.$promise
        .then(function() {
          ctrl.removeFromList();
          $('#confirm-modal').modal('hide');
          widgetsMessages.deleteMessage($scope.widget.widgetname);
          $state.transitionTo('portal.admin.widgets');
        })
        .catch(function() {
        });
    };
  }];
});
