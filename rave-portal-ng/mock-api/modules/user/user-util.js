/*
 * user-util
 * Utilities for user data
 *
 */

define(function(require) {
  var api = require('../../core');

  var userUtil = {

    // Safe keys to return back from the API
    publicKeys: [
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
    ],

    // The valid keys for updating
    updateKeys: [
      'email',
      'openIdUrl',
      'enabled',
      'expired',
      'locked',
      'authorities'
    ],

    // Retrieve from the user's ID from the URL
    idFromUrl: function(url) {
      return parseInt(url.replace('/api/v1/user/', ''), 10);
    },

    // Whether a particular user exists or not
    userExists: function(identifier) {
      return !!userUtil.get(identifier);
    },

    // Retrieve the user by an identifier. Optionally filter
    // to remove secure properties.
    get: function(identifier, filter) {

      // Whether or not we want to filter the user's data.
      // Filtering removes data we wouldn't want to send back,
      // like passwords and tokens. Filtering is true by default
      if (typeof filter === 'undefined') {
        filter = true;
      }

      var results = api.db.query('users', identifier);

      var user = results[0];

      // If we don't have a user, then we return false
      if (!user || results.length > 1) {
        return false;
      }

      // If we want to filter, then do it
      user = filter ? _.pick(user, userUtil.publicKeys) : user;

      return user;
    },

    // Update the user's information in the database
    updateUser: function(identifier, data) {

      api.db.update('users', identifier, function(row) {
        _.extend(row, data);
        return row;
      });
      api.db.commit();
    },

    // Delete a user by identifier
    deleteUser: function(identifier) {
      api.db.deleteRows('users', identifier);
      api.db.commit();
    }
  };

  return userUtil;
});
