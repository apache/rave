define(function(require) {

  // The API endpoint for categories
  var URL = '/api/v1/category/:id';

  // Return the categories resource
  return ['$resource', 'authToken',
  function($resource, authToken) {
    var authHeader = {
      Authorization: 'Basic ' + authToken.get()
    };

    return $resource(URL, {id: '@id'}, {
      get: {
        method: 'GET',
        headers: authHeader
      },
      update: {
        method: 'PUT',
        headers: authHeader
      },
      delete: {
        method: 'DELETE',
        headers: authHeader
      }
    });
  }];
});
