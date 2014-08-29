/*
 * widgetsResource
 * The resource for the widgets list
 *
 */

define(function(require) {

  // The API endpoint for categories
  var URL = '/api/v1/widgets';

  // Return the categories resource
  return ['$resource',
  function($resource) {
    return $resource(URL);
  }];
});
