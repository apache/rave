/*
 * preferences
 * A module for the admin preferences page.
 *
 */

define(function(require) {
  var ng = require('angular');

  // Our module dependencies
  require('uiRouter');

  // The array of names for Angular's dependency injection
  var preferencesDependencies = [
    'ui.router'
  ];

  // Create our module
  var preferences = ng.module('admin.preferences', preferencesDependencies);

  // Register our providers for the preferences
  var preferencesMessages = require('./services/preferences-messages');
  preferences.factory('preferencesMessages', preferencesMessages);

  var preferencesResource = require('./resources/preferences');
  preferences.factory('preferencesResource', preferencesResource);

  var preferencesCtrl = require('./controllers/preferences');
  preferences.controller('preferencesCtrl', preferencesCtrl);

  // Register the routes
  var routes = require('./routes');
  preferences.config(routes);

  // Export the module
  return preferences;
});
