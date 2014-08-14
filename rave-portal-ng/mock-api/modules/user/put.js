define(function(require) {
  'use strict';

  var api = require('../../core.js');
  require('underscore/underscore');

  // The keys that a user is allowed to update through the client
  var updateKeys = [
    'email',
    'openIdUrl',
    'enabled',
    'expired',
    'locked',
    'authorities'
  ];

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

  function userExists(id) {
    var results = api.db.query('users', {
      ID: id
    });

    if (results.length === 1) {
      return results[0];
    }

    return false;
  }

  function updateUser(userID, data) {
    var searchParams = {
      ID: userID
    };
    api.db.update('users', searchParams, function(row) {
      _.extend(row, _.pick(data, updateKeys));
      return row;
    });
    api.db.commit();

    var results = api.db.query('users', searchParams);
    if (results.length === 1) {
      return _.pick(results[0], userKeys);
    }

    return false;
  }

  function processRequest(method, url, data, headers) {
    if (method !== 'PUT') {
      return [405, 'Unknown request'];
    } else if (!this.requestHasToken) {
      return [401, 'A valid token is required'];
    } else if (!this.userIsAuthenticated) {
      return [401, 'Invalid token'];
    }

    // attempt to parse the user ID
    var userID = parseInt( url.replace( '/api/v1/user/', '' ), 10 );
    if (!_.isNumber(userID) || _.isNaN(userID)) {
      return [400, 'Invalid user ID'];
    }

    data = angular.fromJson(data);

    if (!userExists(userID)) {
      return [404, 'User does not exist'];
    }

    var updatedUser = updateUser(userID, data);
    if (!updatedUser) {
      return [500, 'An internal database error has occurred'];
    }

    return [200, updatedUser];
  }

  api.register('/user/:id', 'put', processRequest);

} );
