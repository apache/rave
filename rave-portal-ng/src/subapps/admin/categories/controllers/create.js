/*
 * createCtrl
 * Handles submission of our form
 *
 */

define(function(require) {
  return ['$scope', 'categoriesResource',
  function($scope, categoriesResource) {
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
