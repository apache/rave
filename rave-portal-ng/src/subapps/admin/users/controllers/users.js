/*
 * usersController
 * Sets up our data & pagination.
 *
 */

define(function(require) {
  return ['$scope', '$stateParams', 'pagination', 'usersList', '$rootScope',
  function($scope, $stateParams, pagination, usersList, $rootScope) {

    $scope.currentPage = +$stateParams.page || 0;

    usersList.$promise.then(function() {
      $scope.users = usersList.data;

      var usersMeta = usersList.metadata;

      // Coerce each piece of metadata to a number.
      _.each(usersMeta, function(val, key) {
        usersMeta[key] = +val;
      });

      $scope.usersMeta = usersMeta;
    });

    // Our paginationPages
    $scope.paginationPages = pagination.paginationPages;

    $scope.prevPageDisabled = function() {
      if (!$scope.currentPage) {
        return 'disabled';
      }
      return $scope.currentPage === 1 ? 'disabled' : '';
    };

    $scope.nextPageDisabled = function() {
      if (!$scope.usersMeta) {
        return 'disabled';
      }
      return $scope.currentPage === $scope.usersMeta.pageCount ? 'disabled' : '';
    };
  }];
});
