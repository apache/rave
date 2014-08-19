/*
 * loginEndpoint
 * The endpoint for /auth/login
 *
 */

define(function(require) {
  var Endpoint = require('../../../util/endpoint');
  var userUtil = require('../../user/user-util');
  var ErrorResponse = require('../../../util/error-response');
  var tokenUtil = require('../../../util/token-util');

  var loginEndpoint = new Endpoint({

    url: '/auth/login',

    authorize: false,

    // Request for the new username
    post: function(url, data, headers, params, currentUser) {

      // Throw an error if the username or password doesn't exist
      if (!data.username || !data.password) {
        return new ErrorResponse(401, 'Missing username and/or password field(s).');
      }

      var user = userUtil.get({username: data.username, password: data.password});

      if (!user) {
        return new ErrorResponse(401, 'Invalid login.');
      }

      // Filter out the unwanted properties from the object
      user = _.pick(user, userUtil.publicKeys);

      // Generate our token, then save it to the user
      var token = tokenUtil.generateToken();
      userUtil.updateUser({username: data.username}, {sessionToken: token});

      // Lastly, send the success
      return [200, {
        authorized: true,
        user: user,
        token: token
      }];
    }
  });

  return loginEndpoint;
});
