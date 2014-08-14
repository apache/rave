/*
 * categories
 * Attaches our resolved resources to the scope
 *
 */

define(function(require) {
  return ['$scope', 'categoriesResource', '$stateParams', '$rootScope', 'categoriesList', 'categoriesMessages',
  function($scope, categoriesResource, $stateParams, $rootScope, categoriesList, categoriesMessages) {
    $scope.categories = categoriesList;

    $scope.showTable = function() {
      return $scope.categories.length ? true : false;
    };

    $scope.showMessage = categoriesMessages.showMessage;
    $scope.messageHtml = categoriesMessages.messageHtml;
    $scope.messageClassName = categoriesMessages.messageClassName;
  }];
});
