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

  // Exposed on the window *only* for debugging purposes.
  window.rave = rave;
})();
