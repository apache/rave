/*
 * users
 * The module for the users page of the admin
 *
 */

define(function(require) {
  var ng = require('angular');

  // Our module dependencies
  require('uiRouter');

  // The array of names for Angular's dependency injection
  var usersDependencies = [
    'ui.router'
  ];

  // Create our module
  var users = ng.module('admin.users', usersDependencies);

  // Register our providers for the users
  var usersCtrl = require('./controllers/users');
  users.controller('usersCtrl', usersCtrl);

  // Register the routes
  var routes = require('./routes');
  users.config(routes);

  // Export the module
  return users;
});
