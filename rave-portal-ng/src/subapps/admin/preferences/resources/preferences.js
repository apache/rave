define(function(require) {

  // The API endpoint for categories
  var URL = '/api/v1/preferences';

  // Return the categories resource
  return ['$resource', 'authToken',
  function($resource, authToken) {
    var authHeader = {
      Authorization: 'Basic ' + authToken.get()
    };

    return $resource(URL, {}, {
      get: {
        method: 'GET',
        headers: authHeader
      },

      update: {
        method: 'PUT',
        headers: authHeader
      },
    });
  }];
});
