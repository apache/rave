/*
 * createAccountEndpoint
 * The endpoint for /auth/create-account
 *
 */

define(function(require) {
  var Endpoint = require('../../../util/endpoint');
  var userUtil = require('../../user/user-util');
  var createAccountUtil = require('./create-account-util');
  var ErrorResponse = require('../../../util/error-response');

  var createAccountEndpoint = new Endpoint({

    url: '/auth/create-account',

    authorize: false,

    // Request for the new username
    post: function(url, data, headers, params, currentUser) {

      // validate that require fields exist
      if (!createAccountUtil.ensureFields(data)) {
        return new ErrorResponse(400, 'Missing required fields');
      }

      // now make sure this username doesn't exist
      else if (userUtil.userExists({username: data.username})) {
        return new ErrorResponse(422, 'Username is not available.');
      }

      // now make sure the email doesn't exist
      else if (userUtil.userExists({username: data.username})) {
        return new ErrorResponse(422, 'That email is already in use.');
      }

      // The password needs to be long enough, too
      else if (data.password.length < 8) {
        return new ErrorResponse(422, 'The password is not long enough.');
      }

      // And that the email is valid
      else if (!createAccountUtil.validateEmail(data.email)) {
        return new ErrorResponse(422, 'The email address is not valid.');
      }

      // Create the account
      createAccountUtil.createAccount(data);

      // Lastly, send the success
      return [200, {message: 'Account created.'}];
    }
  });

  return createAccountEndpoint;
});
