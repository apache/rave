/*
 * db
 * Methods for getting and setting users.
 *
 */

define(function(require) {
  var api = require('../../core.js');

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

  var userDb = {

    // Retrieve the user by an identifier
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
      user = filter ? _.pick(user, userKeys) : user;

      return user;
    }
  };

  return userDb;
});
