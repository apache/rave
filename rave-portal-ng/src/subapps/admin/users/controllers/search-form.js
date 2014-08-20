/*
 * searchForm
 * A controller for a users page. It keeps our
 * data up-to-date as the user filters things.
 *
 */

define(function(require) {

  // Return the categories resource
  return ['$scope', 'usersResource', 'pagination',
  function($scope, usersResource, pagination) {

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
      var maxPage = $scope.currentPage === $scope.usersMeta.pageCount;
      var noPages = $scope.usersMeta.pageCount === 0;
      var onePage = $scope.usersMeta.pageCount === 1;
      return maxPage || noPages || onePage ? 'disabled' : '';
    };

    // Whether or not we show the table
    $scope.showResults = function() {

      // If there aren't any users, we assume that there will be.
      // So we still show the table.
      if (!$scope.users) {
        return true;
      }

      // If there are users, we won't show the table if the
      // length is 0.
      else {
        return !!$scope.users.length;
      }
    };

    $scope.clearSearch = function() {
      var usersList = usersResource.get();

      usersList.$promise
        .then(function(response) {
          $scope.users = response.data;

          var usersMeta = usersList.metadata;

          // Coerce each piece of metadata to a number.
          _.each(usersMeta, function(val, key) {
            usersMeta[key] = +val;
          });

          $scope.usersMeta = usersMeta;
        })
        .catch(function() {
        });
    };

    $scope.search = function() {
      var usersList = usersResource.get({
        filter: $scope.filter
      });

      usersList.$promise
        .then(function(response) {
          $scope.users = response.data;

          var usersMeta = usersList.metadata;

          // Coerce each piece of metadata to a number.
          _.each(usersMeta, function(val, key) {
            usersMeta[key] = +val;
          });

          $scope.usersMeta = usersMeta;
        })
        .catch(function() {
        });
    };
  }];
});
