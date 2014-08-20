/*
 * endpoint
 * The /user endpoint
 *
 */

define(function(require) {
  var Endpoint = require('../../util/endpoint');
  var ErrorResponse = require('../../util/error-response');
  var userUtil = require('./user-util');

  var endpoint = new Endpoint({

    url: '/user/:id',

    // Return our paginated data
    get: function(url, data, headers, params, currentUser) {

      // Get the user's ID from the url
      var userId = userUtil.idFromUrl(url);

      // If the ID is invalid, throw an error
      if (!_.isNumber(userId) || _.isNaN(userId)) {
        return new ErrorResponse(400, 'Invalid user ID');
      }

      // Retrieve the user from the database
      var user = userUtil.get({ID:userId});

      // If there's no user then we return the 404
      if (!user) {
        return new ErrorResponse(404, 'User does not exist.');
      }

      // Return the user
      return [200, user];
    },

    put: function(url, data, headers, params, currentUser) {

      // Get the user's ID from the url
      var userId = userUtil.idFromUrl(url);

      // If the ID is invalid, throw an error
      if (!_.isNumber(userId) || _.isNaN(userId)) {
        return new ErrorResponse(400, 'Invalid user ID.');
      }

      // Return a 404 if the user doesn't exist
      if (!userUtil.userExists({ID:userId})) {
        return new ErrorResponse(404, 'User does not exist.');
      }

      // Filter out the data
      data = _.pick(data, userUtil.updateKeys);

      // Actually make the update
      userUtil.updateUser({ID: userId}, data);

      // We want to return the user, so wetrieve them from the database
      var user = userUtil.get({ID:userId});

      // If there's no user then we return the 404
      if (!user) {
        return new ErrorResponse(404, 'User does not exist.');
      }

      // Make the return
      return [200, user];
    },

    delete: function(url, data, headers, params, currentUser) {
      
      // Get the user's ID from the url
      var userId = userUtil.idFromUrl(url);

      // If the ID is invalid, throw an error
      if (!_.isNumber(userId) || _.isNaN(userId)) {
        return new ErrorResponse(400, 'Invalid user ID.');
      }

      // Return a 404 if the user doesn't exist
      if (!userUtil.userExists({ID:userId})) {
        return new ErrorResponse(404, 'User does not exist.');
      }

      // Actually delete the user
      userUtil.deleteUser({ID:userId});

      // Share that the user was deleted
      return [200, null];
    }
  });

  return endpoint;
});
