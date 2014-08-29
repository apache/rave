/*
 * logout
 * A module that registers our logout route. All this does is defer to the security
 * provider to destroy our cookie and redirect us to the login page.
 *
 */

define(function(require) {
  var ng = require('angular');

  // Ensure that our dependencies are loaded
  require('uiRouter');
  var security = require('../security/security');

  // Put our dependencies into an array for Angular's dependency injection
  var logoutDependencies = [
    'ui.router',
    security.name
  ];

  // Create our subapp as a module
  var logout = ng.module('logout', logoutDependencies);

  // Register our routes for this subapp
  var routes = require('./routes');
  logout.config(routes);

  // Return the module
  return logout;
});
