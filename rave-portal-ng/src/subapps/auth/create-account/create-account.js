/*
 * createAccount
 * The module that manages everything for the creation of a new
 * account.
 *
 */

define(function(require) {
  var ng = require('angular');

  // Ensure that our dependencies are loaded
  require('uiRouter');
  var authApi = require('../api/api');

  // Put our dependencies into an array for Angular's dependency injection
  var createAccountDependencies = [
    'ui.router',
    authApi.name
  ];

  // Create our subapp as a module
  var createAccount = ng.module('createAccount', createAccountDependencies);

  // Controllers
  var createAccountCtrl = require('./controllers/create-account');
  createAccount.controller('createAccountController', createAccountCtrl);

  // Directives
  var passwordCheckDirective = require('./directives/password-check');
  createAccount.directive('pwCheck', passwordCheckDirective);

  // Services
  var createAccountApiRoutes = require('./services/create-account-api-routes');
  createAccount.factory('createAccountApiRoutes', createAccountApiRoutes);

  var createAccountApi = require('./services/create-account-api');
  createAccount.factory('createAccountApi', createAccountApi);

  // Register our routes for this subapp
  var routes = require('./routes');
  createAccount.config(routes);

  // Return the module
  return createAccount;
});
