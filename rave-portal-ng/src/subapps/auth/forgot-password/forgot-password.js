/*
 * forgotPassword
 * The module that manages everything for the forgot password section
 * of the app.
 *
 */

define(function(require) {
  var ng = require('angular');

  // Ensure that our dependencies are loaded
  require('uiRouter');
  var authApi = require('../api/api');

  // Put our dependencies into an array for Angular's dependency injection
  var forgotPasswordDependencies = [
    'ui.router',
    authApi.name
  ];

  // Create our subapp as a module
  var forgotPassword = ng.module('forgotPassword', forgotPasswordDependencies);

  // Controllers
  var forgotPasswordCtrl = require('./controllers/forgot-password');
  forgotPassword.controller('forgotPasswordController', forgotPasswordCtrl);

  // Services (the API)
  var forgotPasswordApiRoutes = require('./services/forgot-password-api-routes');
  forgotPassword.factory('forgotPasswordApiRoutes', forgotPasswordApiRoutes);

  var forgotPasswordApi = require('./services/forgot-password-api');
  forgotPassword.factory('forgotPasswordApi', forgotPasswordApi);

  // Register our routes for this subapp
  var routes = require('./routes');
  forgotPassword.config(routes);

  // Return the module
  return forgotPassword;
});
