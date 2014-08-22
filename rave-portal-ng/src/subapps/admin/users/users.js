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
  users.factory('usersMessages', require('./services/users-messages'));
  users.factory('usersResource', require('./resources/users'));
  users.factory('userResource', require('./resources/user'));
  users.controller('usersCtrl', require('./controllers/users'));
  users.controller('userCtrl', require('./controllers/user'));
  users.controller('createUserCtrl', require('./controllers/create-user'));
  users.controller('userSearchCtrl', require('./controllers/search-form'));

  // Register the routes
  var routes = require('./routes');
  users.config(routes);

  // Export the module
  return users;
});
