define(function(require) {
  return ['$scope', '$stateParams', 'pagination',
  function($scope, $stateParams, pagination) {

    $scope.currentPage = +$stateParams.page || 0;

    // How many items to show per page
    $scope.itemsPerPage = 10;

    // Some fake users for us to display.
    $scope.users = [];
    $scope.users.push({},{},{},{},{},{},{},{},{},{},{});

    // Our total number of users.
    $scope.totalUsers = 1000;

    // The generated pageCount.
    $scope.pageCount = Math.ceil($scope.totalUsers / $scope.itemsPerPage);

    // Our paginationPages
    $scope.paginationPages = pagination.paginationPages;

    $scope.prevPageDisabled = function() {
      return $scope.currentPage === 1 ? 'disabled' : '';
    };

    $scope.nextPageDisabled = function() {
      return $scope.currentPage === $scope.pageCount ? 'disabled' : '';
    };
  }];
});
