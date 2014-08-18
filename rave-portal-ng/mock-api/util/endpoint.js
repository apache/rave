/*
 * endpoint
 * Defines a RESTful endpoint
 *
 */

define(function(require) {
  var _ = require('underscore');
  var paramsFromUrl = require('./params');
  var ErrorResponse = require('./error-response');
  var tokenUtil = require('./token-util');
  var userDb = require('../modules/user/db');

  // These are the valid options that you can pass into
  // a new endpoint
  var endpointOptions = [
    'url',
    'authorize',
    'get',
    'post',
    'delete',
    'put'
  ];

  var Endpoint = function(options) {

    // Merge valid options directly into the endpoint
    _.extend(this, _.pick(options, endpointOptions));
  };

  _.extend(Endpoint.prototype, {

    // By default, all requests are 
    authorize: true,

    // What is ultimately returned from the endpoint.
    callback: function(method, url, data, headers) {

      // Our current user, if there is one.
      var currentUser;

      // The first thing we need to check for every endpoint is if we want to authorize or not
      if (this.authorize) {

        // Attempt to get our token from the headers
        var token = tokenUtil.tokenFromHeaders(headers);

        // If it doesn't exist, then we throw an error
        if (!token) {
          return new ErrorResponse(401, 'A valid token is required.');
        }

        // Otherwise, we try to get the user from the token
        var user = userDb.get({
          sessionToken: token
        });

        // Again, throw an error if there's no user
        if (!user) {
          return new ErrorResponse(401, 'Invalid token.');
        }

        // Otherwise, we set our currentUser as the user.
        currentUser = user;
      }

      // Get our query parameters from the url
      var params = paramsFromUrl(url);

      // Ensure that our method is lowercase
      method = method.toLowerCase();

      var callback = this[method];

      // If we have the associated method, then we call it
      if (_.isFunction(callback)) {
        return callback(url, data, headers, params, currentUser);
      }

      // Otherwise, this is an unhandled request.
      else {
        return new ErrorResponse(405, 'Unknown request');
      }
    }
  });

  return Endpoint;
});
