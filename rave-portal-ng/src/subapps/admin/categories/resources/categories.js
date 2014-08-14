/*
 * categoriesResource
 * The resource for the categories list
 *
 */

define(function(require) {

  // The API endpoint for categories
  var URL = '/api/v1/categories';

  // Return the categories resource
  return ['$resource',
  function($resource) {
    return $resource(URL);
  }];
});
