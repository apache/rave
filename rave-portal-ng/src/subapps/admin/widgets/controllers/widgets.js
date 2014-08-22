/*
 * widgetsController
 * Sets up our data & pagination.
 *
 */

define(function(require) {
  return ['$scope', '$stateParams', 'pagination', 'widgetsList', '$rootScope',
  function($scope, $stateParams, pagination, widgetsList, $rootScope) {

    $scope.currentPage = +$stateParams.page || 0;
    $scope.filter = $stateParams.filter || '';

    widgetsList.$promise.then(function() {
      $scope.widgets = widgetsList.data;

      var widgetsMeta = widgetsList.metadata;

      // Coerce each piece of metadata to a number.
      _.each(widgetsMeta, function(val, key) {
        widgetsMeta[key] = +val;
      });

      $scope.widgetsMeta = widgetsMeta;
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
      if (!$scope.widgetsMeta) {
        return 'disabled';
      }
      return $scope.currentPage === $scope.widgetsMeta.pageCount ? 'disabled' : '';
    };
  }];
});
