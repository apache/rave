/*
 * endpoint
 * The actual endpoint for preferences
 *
 */

define(function(require) {
  var Endpoint = require('../../util/endpoint');
  var preferencesUtil = require('./preferences-util');

  var endpoint = new Endpoint({

    url: '/preferences',

    // Our get endpoint. All that it does is return our
    // of preferences as a simple object.
    get: function(url, data, headers, params, currentUser) {
      return [200, preferencesUtil.getAll()];
    },

    // Puts our data into the database.
    put: function(url, data, headers, params, currentUser) {
      preferencesUtil.put(data);
      return [200, preferencesUtil.getAll()];
    }
  });

  return endpoint;
});
