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
  var usersResource = require('./resources/users');
  users.factory('usersResource', usersResource);

  var userResource = require('./resources/user');
  users.factory('userResource', userResource);
  
  var usersCtrl = require('./controllers/users');
  users.controller('usersCtrl', usersCtrl);

  var userCtrl = require('./controllers/user');
  users.controller('userCtrl', userCtrl);

  var searchCtrl = require('./controllers/search-form');
  users.controller('searchCtrl', searchCtrl);

  // Register the routes
  var routes = require('./routes');
  users.config(routes);

  // Export the module
  return users;
});
