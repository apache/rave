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

  function getUser(id) {
    var results = api.db.query('users', {
      ID: id
    });

    if (results.length === 1) {
      return _.pick(results[0], userKeys);
    }

    return false;
  }

  function processRequest(method, url, data, headers) {
    if (method !== 'GET') {
      return [405, 'Unknown request'];
    } else if (!this.requestHasToken) {
      return [401, 'A valid token is required'];
    } else if (!this.userIsAuthenticated) {
      return [401, 'Invalid token'];
    }

    // attempt to parse the user ID
    var userID = parseInt(url.replace('/api/v1/user/', ''), 10);
    if (!_.isNumber(userID) || _.isNaN(userID)) {
      return [400, 'Invalid user ID'];
    }

    var user = getUser(userID);
    if (!user) {
      return [404, 'User does not exist'];
    }

    return [200, user];
  }

  api.register('/user/:id', 'get', processRequest);

} );
