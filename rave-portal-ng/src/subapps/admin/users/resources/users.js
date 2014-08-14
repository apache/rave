/*
 * usersResource
 * The resource for the users list
 *
 */

define(function(require) {

  // The API endpoint for categories
  var URL = '/api/v1/users';

  // Return the categories resource
  return ['$resource', 'authToken',
  function($resource, authToken) {

    var authHeader = {};

    // We return a factory method from this due to logging in and out
    // requiring a dynamic header
    return function() {

      // Dynamically set the authorization header, based on our current authToken
      authHeader.Authorization = 'Basic ' + authToken.get();

      // Return the resource.
      return $resource(URL, {}, {
        query: {
          method: 'GET',
          isArray: true,
          headers: authHeader
        }
      });
    };
  }];
});
