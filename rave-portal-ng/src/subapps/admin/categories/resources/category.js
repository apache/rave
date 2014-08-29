/*
 * categoryResource
 * A resource for a single category
 *
 */

define(function(require) {

  // The API endpoint for categories
  var URL = '/api/v1/category/:id';

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
