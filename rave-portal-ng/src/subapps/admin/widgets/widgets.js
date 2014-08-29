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

  // Register our providers
  widgets.factory('widgetsMessages', require('./services/widgets-messages'));
  widgets.factory('widgetsResource', require('./resources/widgets'));
  widgets.factory('widgetResource', require('./resources/widget'));
  widgets.controller('widgetsCtrl', require('./controllers/widgets'));
  widgets.controller('widgetCtrl', require('./controllers/widget'));
  widgets.controller('widgetSearchCtrl', require('./controllers/search-form'));

  // Register the routes
  var routes = require('./routes');
  widgets.config(routes);

  // Export the module
  return widgets;
});
