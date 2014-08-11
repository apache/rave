/*
 * forgotUsername
 * The module that manages everything for the forgot username section
 * of the app.
 *
 */

define(function(require) {
  var ng = require('angular');

  // Ensure that our dependencies are loaded
  require('uiRouter');
  var authApi = require('../api/api');

  // Put our dependencies into an array for Angular's dependency injection
  var forgotUsernameDependencies = [
    'ui.router',
    authApi.name
  ];

  // Create our subapp as a module
  var forgotUsername = ng.module('forgotUsername', forgotUsernameDependencies);

  // Controllers
  var forgotUsernameCtrl = require('./controllers/forgot-username');
  forgotUsername.controller('forgotUsernameController', forgotUsernameCtrl);

  // Services (the API)
  var forgotUsernameApiRoutes = require('./services/forgot-username-api-routes');
  forgotUsername.factory('forgotUsernameApiRoutes', forgotUsernameApiRoutes);

  var forgotUsernameApi = require('./services/forgot-username-api');
  forgotUsername.factory('forgotUsernameApi', forgotUsernameApi);

  // Register our routes for this subapp
  var routes = require('./routes');
  forgotUsername.config(routes);

  // Return the module
  return forgotUsername;
});
