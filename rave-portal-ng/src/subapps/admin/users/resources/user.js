/*
 * userResource
 * A resource for a single user
 *
 */

define(function(require) {

  // The API endpoint for categories
  var URL = '/api/v1/user/:id';

  // Return the categories resource
  return ['$resource',
  function($resource) {
    return $resource(URL, {id: '@id'}, {
      update: {
        method: 'PUT'
      }
    });
  }];
});
