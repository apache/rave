/*
 * endpoint
 * The actual endpoint for preferences
 *
 */

define(function(require) {
  var Endpoint = require('../../util/endpoint');
  var prefsDb = require('./db');

  var endpoint = new Endpoint({

    url: '/preferences',

    // Our get endpoint. All that it does is return our
    // of preferences as a simple object.
    get: function(url, data, headers, params, currentUser) {
      return [200, prefsDb.get()];
    },

    // Puts our data into the database.
    put: function(url, data, headers, params, currentUser) {
      return [200, prefsDb.put(data)];
    }
  });

  return endpoint;
});
