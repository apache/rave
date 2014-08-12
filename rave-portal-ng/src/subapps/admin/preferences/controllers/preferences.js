/*
 * preferencesController
 * Manages submission of our form; also handles the data binding.
 *
 */

define(function(require) {
  var _ = require('underscore');

  // Our list of preferences. You must put any
  // new preferences here for Angular to auto-update
  // the ng-model.
  var preferencesKeys = [
    'titleSuffix',
    'pageSize',
    'defaultWidgetHeight',
    'showStackTrace',
    'initialWidgetStatus',
    'externalMarketplaceUrl',
    'javascriptDebugMode'
  ];

  return ['$scope', 'preferencesResource', 'preferences',
  function($scope, preferencesResource, preferences) {

    // Create our preferences object on the scope.
    $scope.preferences = {};

    // Try to get the current preferences to show the user
    $scope.currentPreferences = preferences;
    $scope.currentPreferences.$promise
      .then(function(res) {
        var currentPrefs = _.pick(res, preferencesKeys);
        _.each(currentPrefs, function(value, key) {
          $scope.preferences[key] = value;
        });
      })
      .catch(function() {
      });

    $scope.onSubmit = function() {
      var updatedPreferences = preferencesResource.update(
        $scope.preferences
      );
      updatedPreferences.$promise
        .then(function(res) {

        })
        .catch(function(err) {
        });
    };
  }];
});
