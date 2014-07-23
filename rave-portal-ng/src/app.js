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

  // This array are the objects we send back from the server
  // var initialData = ['user', 'nav'];

  rave.controller('appData', ['$scope', function($scope) {
    // var dataPoints = _.pick(window._initialData, initialData);
    // angular.merge($scope, dataPoints);
    $scope.user = window._initialData.user;
    $scope.nav = window._initialData.nav;
  }]);

  // Exposed on the window *only* for debugging purposes.
  window.rave = rave;
})();
