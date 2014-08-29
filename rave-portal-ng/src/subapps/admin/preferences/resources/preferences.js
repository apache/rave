/*
 * preferences-resource
 * A resource for the Rave preferences.
 *
 */

define(function(require) {

  // The API endpoint for categories
  var URL = '/api/v1/preferences';

  // Return the categories resource
  return ['$resource',
  function($resource) {
    return $resource(URL, {}, {
      update: {
        method: 'PUT'
      },
    });
  }];
});
