define(function(require) {
  'use strict';

  var api = require('../../core.js');
  require('underscore/underscore');

  function userExists(id) {
    var results = api.db.query('users', {
      ID: id
    });

    if (results.length === 1) {
      return true;
    }

    return false;
  }

  function deleteUser(id) {
    api.db.deleteRows('users', {
      ID: id
    });
    api.db.commit();
  }

  function processRequest(method, url, data, headers) {
    if (method !== 'DELETE') {
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
    } else if (!userExists(userID)) {
      return [404, 'User does not exist'];
    }

    deleteUser(userID);

    return [200, null];
  }

  api.register('/user/:id', 'delete', processRequest);

} );
