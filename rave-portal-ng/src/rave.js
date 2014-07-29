define(function(require) {
  var angular = require('angular');

  var raveDependencies = [
    'ui.router',
    'profile',
    'ngMockE2E'
  ];

  var rave = angular.module('rave', raveDependencies);

  rave.controller('appData', ['$scope', function($scope) {
    $scope.user = window._initialData.user;
    $scope.nav = window._initialData.nav;
  }]);

  // Exposed on the window *only* for debugging purposes
  window.rave = rave;

  // Export rave from this module
  return rave;
});
