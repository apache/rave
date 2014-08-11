/*
 * widgets
 * A module for the widgets section of the Admin.
 *
 */

define(function(require) {
  var ng = require('angular');

  // Our module dependencies
  require('uiRouter');

  // The array of names for Angular's dependency injection
  var widgetsDependencies = [
    'ui.router'
  ];

  // Create our module
  var widgets = ng.module('admin.widgets', widgetsDependencies);

  // Register the routes
  var routes = require('./routes');
  widgets.config(routes);

  // Export the module
  return widgets;
});
