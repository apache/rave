/*
 * home
 * A module for the index page of the admin section of the site.
 * It's a simple section that just displays a link to the documentation.
 *
 */

define(function(require) {
  var ng = require('angular');

  // Our module dependencies
  require('uiRouter');

  // The array of names for Angular's dependency injection
  var homeDependencies = [
    'ui.router'
  ];

  // Create our module
  var home = ng.module('admin.home', homeDependencies);

  // Register the routes
  var routes = require('./routes');
  home.config(routes);

  // Export the module
  return home;
});
