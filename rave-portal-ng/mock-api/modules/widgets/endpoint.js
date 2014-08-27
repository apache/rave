/*
 * endpoint
 * The /widgets endpoint
 *
 */

define(function(require) {
  var Endpoint = require('../../util/endpoint');
  var widgetsUtil = require('./widgets-util');

  var endpoint = new Endpoint({

    url: '/widgets',

    // Return our paginated data
    get: function(url, data, headers, params, currentUser) {
      var currentPage = params.page || 1;
      return [200, widgetsUtil.getPage(currentPage, params.filter)];
    }
  });

  return endpoint;
});
