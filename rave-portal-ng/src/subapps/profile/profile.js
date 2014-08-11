/*
 * profile
 * The sub-application module for our Profile. The profile
 * lets you view and change your information, and also manage
 * your relationships.
 *
 */

define(function(require) {
  var ng = require('angular');

  // Ensure that our dependencies are loaded
  require('uiRouter');

  // Create an array of our dependencies for DI
  var profileDependencies = [
    'ui.router'
  ];

  // Create the profile module
  var profile = ng.module('profile', profileDependencies);

  // Register our route states for the ui-router
  var routes = require('./routes');
  profile.config(routes);

  // Return the profile
  return profile;
});
