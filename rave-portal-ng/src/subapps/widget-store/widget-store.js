/*
 * widgetStore
 * The sub-application module for our WidgetStore. This is where you
 * can peruse the available Rave widgets for your Rave installation.
 *
 */

define(function(require) {
  var ng = require('angular');

  // Ensure that our dependencies are loaded
  require('uiRouter');

  // An array of our dependencies for Angular's dependency injection
  var widgetStoreDeps = [
    'ui.router'
  ];

  // Create our subapp as a module
  var widgetStore = ng.module('widgetStore', widgetStoreDeps);

  // Set up our routes for the ui-router
  var routes = require('./routes');
  widgetStore.config(routes);

  // Return the module
  return widgetStore;
});
