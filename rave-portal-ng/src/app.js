/*
 * rave
 * This file defines the main module, called rave.
 *
 */

(function() {

  // The dependencies of our application
  var raveDependencies = [
    'ui.router',
    'profile',
    'ngMockE2E'
  ];

  var rave = angular.module('rave', raveDependencies);

  // Effectively our $rootScope.
  rave.controller('appData', ['$scope', '$state', '$stateParams', function($scope, $state, $stateParams) {
    $scope.user = window._initialData.user;
    $scope.nav = window._initialData.nav;

    // The ui-router doesn't do everything, unfortunately. So we need to
    // store our state data so we can make new directives for it.
    $scope.$state = $state;
    $scope.$stateParams = $stateParams;
  }]);

  // Exposed on the window *only* for debugging purposes.
  window.rave = rave;
})();
