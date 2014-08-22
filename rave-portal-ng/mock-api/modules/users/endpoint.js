/*
 * endpoint
 * The /users endpoint
 *
 */

define(function(require) {
  var Endpoint = require('../../util/endpoint');
  var usersUtil = require('./users-util');

  var endpoint = new Endpoint({

    url: '/users',

    // Return our paginated data
    get: function(url, data, headers, params, currentUser) {
      var currentPage = params.page || 1;
      return [200, usersUtil.getPage(currentPage, params.filter)];
    }
  });

  return endpoint;
});
