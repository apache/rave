/*
 * endpoint
 * The endpoint for status
 *
 */

define(function(require) {
  var Endpoint = require('../../util/endpoint');

  var endpoint = new Endpoint({

    url: '/status',

    // Returns that the API is a-okay
    get: function(url, data, headers, params, currentUser) {
      var responseData = {
        status: 'ok'
      };
      return [200, responseData];
    }
  });

  return endpoint;
});
