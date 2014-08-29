/*
 * login
 * A module that registers the login section of the app.
 *
 */

define(function(require) {
  var ng = require('angular');

  // Ensure that our dependencies are loaded
  require('uiRouter');
  var security = require('../security/security');

  // Put our dependencies into an array for Angular's dependency injection
  var loginDependencies = [
    'ui.router',
    security.name
  ];

  // Create our subapp as a module
  var login = ng.module('login', loginDependencies);

  // Register our services
  var loginCtrl = require('./controllers/login-form');
  login.controller('loginController', loginCtrl);

  // Register our routes for this subapp
  var routes = require('./routes');
  login.config(routes);

  // Return the module
  return login;
});
