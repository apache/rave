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

  function getPageSize() {
    var prefs = api.db.query('preferences');
    // return prefs;
    var pageSize = _.findWhere(prefs, {key: 'pageSize'});
    return pageSize.value;
  }

  function getUsers(params) {

    var rawUsers = api.db.query('users');
    var filteredUsers = [];

    _.each(rawUsers, function(rawUser) {
      filteredUsers.push(_.pick(rawUser, userKeys));
    });

    var page = params.page || 1;
    var pageSize = getPageSize();

    // The first index to start from
    var startIndex = (page - 1) * pageSize;

    // The naive end index. We may have overshot this.
    var endIndex = page * pageSize;

    // Truncate our end index if it's too long. Slice only goes UP TO
    // this index, which is why we don't use (length - 1)
    if (endIndex > filteredUsers.length) {
      endIndex = filteredUsers.length;
    }

    var results = filteredUsers.slice(startIndex, endIndex);

    var returnObj = {};

    returnObj.data = results;
    returnObj.metadata = {
      pageSize: pageSize,
      page: page,
      start: startIndex+1,
      end: endIndex,
      pageCount: Math.ceil(filteredUsers.length / pageSize),
      totalUsers: filteredUsers.length
    };

    return returnObj;
  }

  function processRequest(method, url, data, headers) {
    if (method !== 'GET') {
      return [405, 'Unknown request'];
    } else if (!this.requestHasToken) {
      return [401, 'A valid token is required'];
    } else if (!this.userIsAuthenticated) {
      return [401, 'Invalid token'];
    }

    var params = this.parseQueryString(url);

    var usersData = getUsers(params);

    var object = {
      data: usersData.data,
      metadata: usersData.metadata
    };

    return [200, object];
  }

  api.register('/users', 'get', processRequest);

} );
