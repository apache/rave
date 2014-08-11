/*
 * categories
 * Handles submission of our form
 *
 */

define(function(require) {
  return ['$scope', 'categoriesResource', '$stateParams', '$rootScope',
  function($scope, categoriesResource, $stateParams, $rootScope) {

    $scope.categories = categoriesResource.query();

    $scope.onCreate = function() {
      var newResource = categoriesResource.save({
        text: $scope.newText
      });

      newResource.$promise
        .then(function() {
          $scope.categories.push(newResource);
        })
        .catch(function() {
        });
    };
  }];
});
