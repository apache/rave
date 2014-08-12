/*
 * categories
 * Attaches our resolved resources to the scope
 *
 */

define(function(require) {
  return ['$scope', 'categoriesResource', '$stateParams', '$rootScope', 'categoriesList',
  function($scope, categoriesResource, $stateParams, $rootScope, categoriesList) {
    $scope.categories = categoriesList;
  }];
});
