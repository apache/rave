/*
 * verifyEndpoint
 * The endpoint for /auth/verify
 *
 */

define(function(require) {
  var Endpoint = require('../../../util/endpoint');
  var userUtil = require('../../user/user-util');
  var ErrorResponse = require('../../../util/error-response');
  var _ = require('underscore');

  var verifyEndpoint = new Endpoint({

    url: '/auth/verify',

    authorize: false,

    // Request for the new username
    post: function(url, data, headers, params, currentUser) {

      var token = data.token;

      // Ensure that the token exists
      if (!token) {
        return new ErrorResponse(400, 'Missing token field.');
      }

      // Also that it has the right form
      else if (!_.isString(token) || token.length !== 32) {
        return new ErrorResponse(401, 'A valid token is required.');
      }

      // If so, get the token
      var user = userUtil.get({sessionToken: token});

      // Return an error if the user doesn't exist
      if (!user) {
        return new ErrorResponse(401, 'Invalid token.');
      }

      // Filter out the unwanted properties from the object
      user = _.pick(user, userUtil.publicKeys);

      // Lastly, send the success
      return [200, {
        authorized: true,
        user: user
      }];
    }
  });

  return verifyEndpoint;
});
