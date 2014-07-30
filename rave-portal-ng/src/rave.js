define(function(require) {
  var angular = require('angular');

  // The individual modules
  require('./modules/home/index');
  require('./modules/admin/index');
  require('./modules/auth/index');
  require('./modules/profile/index');
  require('./modules/widget-store/index');

  var raveDependencies = [
    'ui.router',
    'profile',
    'ngMockE2E',
    'home',
    'widget-store',
    'profile',
    'auth',
    'admin',
    'filters'
  ];

  var rave = angular.module('rave', raveDependencies);

  rave.controller('appData', [
    '$scope', '$state', '$stateParams',
    function($scope, $state, $stateParams) {
    $scope.user = window._initialData.user;
    $scope.nav = window._initialData.nav;
    $scope.loginNav = window._initialData.loginNav;

    // The ui-router doesn't do everything, unfortunately. So we need to
    // store our state data so we can make new directives for it.
    $scope.$state = $state;
    $scope.$stateParams = $stateParams;
  }]);

  // Exposed on the window *only* for debugging purposes
  window.rave = rave;

  // Export rave from this module
  return rave;
});
