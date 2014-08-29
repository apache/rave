/*
 * auth
 * An Angular module that serves as a container for all of our
 * other auth-related modules.
 *
 */

define(function(require) {
  var angular = require('angular');

  // Ensure that our dependencies are loaded
  require('angularCookie');

  // Load the different modules that make up this subapp
  var security = require('./security/security');
  var logout = require('./logout/logout');
  var login = require('./login/login');
  var forgotPassword = require('./forgot-password/forgot-password');
  var forgotUsername = require('./forgot-username/forgot-username');
  var createAccount = require('./create-account/create-account');

  // Define our array of dependencies for Angular's DI
  var authDependencies = [
    'ipCookie',
    security.name,
    logout.name,
    login.name,
    forgotPassword.name,
    forgotUsername.name,
    createAccount.name
  ];

  var auth = angular.module('auth', authDependencies);

  return auth;
});
