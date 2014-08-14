/*
 * rave
 * The main Angular module for Rave.
 * This module is mainly a container for the other modules that
 * make up the application.
 *
 */

define(function(require) {
  var ng = require('angular');

  // Our module dependencies
  require('uiRouter');
  require('angularMocks');
  require('angularCookie');
  require('angularResource');

  // Our 'subapp' dependencies
  var home = require('./subapps/home/home');
  var profile = require('./subapps/profile/profile');
  var widgetStore = require('./subapps/widget-store/widget-store');
  var admin = require('./subapps/admin/admin');
  var auth = require('./subapps/auth/auth');

  // Create an array out of our dependencies for Angular's DI
  var raveDependencies = [
    'ui.router',
    'profile',
    'ngMockE2E',
    home.name,
    widgetStore.name,
    profile.name,
    auth.name,
    admin.name,
    'filters'
  ];

  // Create Rave as an Angular module
  var rave = ng.module('rave', raveDependencies);

  // Some fake data until the mock API adds the Bootstrap route.
  rave.controller('appData', [
    '$scope', '$state', '$stateParams',
    function($scope, $state, $stateParams) {
      $scope.nav = window._initialData.nav;
      $scope.loginNav = window._initialData.loginNav;

      $scope.preferences = window.initialData.preferences;

      // The ui-router doesn't do everything, unfortunately. So we need to
      // store our state data so we can make new directives for it.
      $scope.$state = $state;
      $scope.$stateParams = $stateParams;
    }]);

  // Routes
  var routes = require('./routes');
  rave.config(routes);

  // Exposed on the window *only* for debugging purposes
  window.rave = rave;

  // Export rave from this module
  return rave;
});
