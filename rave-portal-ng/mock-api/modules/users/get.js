define(function(require) {
  'use strict';

  var api = require('../../core.js');
  require('underscore/underscore');

  // The whitelisted keys that we send back with our request for the users
  var userKeys = [
    'ID',
    'username',
    'description',
    'firstName',
    'lastName',
    'locked',
    'enabled',
    'expired',
    'authorities',
    'openIdUrl',
    'email'
  ];

  function getUsers() {
    var rawUsers = api.db.query('users');
    var filteredUsers = [];

    _.each(rawUsers, function(rawUser) {
      filteredUsers.push(_.pick(rawUser, userKeys));
    });

    return filteredUsers;
  }

  function processRequest(method, url, data, headers) {
    if (method !== 'GET') {
      return [405, 'Unknown request'];
    } else if (!this.requestHasToken) {
      return [401, 'A valid token is required'];
    } else if (!this.userIsAuthenticated) {
      return [401, 'Invalid token'];
    }

    return [200, getUsers()];
  }

  api.register('/users', 'get', processRequest);

} );
