/*
 * home
 * The most important part of your Rave: your home page. This subapp
 * handles displaying your widgets.
 *
 */

define(function(require) {
  var ng = require('angular');

  // Ensure that our dependencies are loaded
  require('uiRouter');

  // Put our dependencies into an array for Angular's dependency injection
  var homeDependencies = [
    'ui.router'
  ];

  // Create our subapp as a module
  var home = ng.module('home', homeDependencies);

  // Register our routes for this subapp
  var routes = require('./routes');
  home.config(routes);

  // Return the module
  return home;
});
