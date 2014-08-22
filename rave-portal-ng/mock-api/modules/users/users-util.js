/*
 * usersUtil
 * Methods for interacting with the users data
 *
 */

define(function(require) {
  var api = require('../../core');
  var preferencesUtil = require('../preferences/preferences-util');
  var userUtil = require('../user/user-util');

  var usersUtil = {

    // Get every single user from the database.
    getAll: function() {
      return api.db.query('users');
    },

    // Get the list of users, optionally filtering by filter string
    getPage: function(currentPage, filter) {

      // Get the page size from the database
      var pageSize = preferencesUtil.getPreference('pageSize');

      // Get a naive list of users from the DB
      var allUsers = usersUtil.getAll();

      // Filter them, if filter is passed
      if (filter) {
        allUsers = _.filter(allUsers, function(user) {
          return user.username.indexOf(filter) > -1;
        });
      }

      var filteredUsers = [];

      _.each(allUsers, function(rawUser) {
        filteredUsers.push(_.pick(rawUser, userUtil.publicKeys));
      });

      // The first index to start from
      var startIndex = (currentPage - 1) * pageSize;

      // The naive end index. We may have overshot this.
      var endIndex = currentPage * pageSize;

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
        currentPage: currentPage,
        start: startIndex+1,
        end: endIndex,
        pageCount: Math.ceil(filteredUsers.length / pageSize),
        totalUsers: filteredUsers.length
      };

      return returnObj;
    }
  };

  return usersUtil;
});
