/*
 * preferences
 * Sets up the preferences section of the API. This includes two things:
 * the actual endpoints and methods for retrieving data
 *
 */

define(function(require) {
  var api = require('../../core.js');

  // Register our endpoints
  var endpoint = require('./endpoint');
  api.registerEndpoint(endpoint);
});
