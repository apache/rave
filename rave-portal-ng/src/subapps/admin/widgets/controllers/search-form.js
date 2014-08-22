/*
 * searchForm
 * A controller for a widgets page. It keeps our
 * data up-to-date as the user filters things.
 *
 */

define(function(require) {

  // Return the categories resource
  return ['$scope', 'widgetsResource', 'pagination', '$state',
  function($scope, widgetsResource, pagination, $state) {

    // Our paginationPages
    $scope.paginationPages = pagination.paginationPages;

    $scope.prevPageDisabled = function() {
      if (!$scope.currentPage) {
        return 'disabled';
      }
      return $scope.currentPage === 1 ? 'disabled' : '';
    };

    $scope.nextPageDisabled = function() {
      if (!$scope.widgetsMeta) {
        return 'disabled';
      }
      var maxPage = $scope.currentPage === $scope.widgetsMeta.pageCount;
      var noPages = $scope.widgetsMeta.pageCount === 0;
      var onePage = $scope.widgetsMeta.pageCount === 1;
      return maxPage || noPages || onePage ? 'disabled' : '';
    };

    // Whether or not we show the table
    $scope.showResults = function() {

      // If there aren't any users, we assume that there will be.
      // So we still show the table.
      if (!$scope.widgets) {
        return true;
      }

      // If there are users, we won't show the table if the
      // length is 0.
      else {
        return !!$scope.widgets.length;
      }
    };

    $scope.search = function(options) {
      var widgetsList = widgetsResource.get(options);

      widgetsList.$promise
        .then(function(response) {
          $scope.widgets = response.data;
          if ($scope.resetFilter) {
            $scope.filter = '';
          }

          var widgetsMeta = widgetsList.metadata;

          // Coerce each piece of metadata to a number.
          _.each(widgetsMeta, function(val, key) {
            widgetsMeta[key] = +val;
          });

          $scope.widgetsMeta = widgetsMeta;
          $state.transitionTo('portal.admin.widgets', {page:1, filter:$scope.filter});
        })
        .catch(function() {
        });
    };
  }];
});
