/*
 * categoriesResource
 * The resource for the categories list
 *
 */

define(function(require) {

  // The API endpoint for categories
  var URL = '/api/v1/categories';

  // Return the categories resource
  return ['$resource', 'authToken',
  function($resource, authToken) {
    var authHeader = { 
      Authorization: 'Basic ' + authToken.get()
    };

    return $resource(URL, {}, {
      save: {
        method: 'POST',
        headers: authHeader
      },

      query: {
        method: 'GET',
        isArray: true,
        headers: authHeader
      }
    });
  }];
});
