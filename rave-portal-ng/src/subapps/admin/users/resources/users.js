/*
 * usersResource
 * The resource for the users list
 *
 */

define(function(require) {

  // The API endpoint for categories
  var URL = '/api/v1/users';

  // Return the categories resource
  return ['$resource',
  function($resource) {
    return $resource(URL);
  }];
});
