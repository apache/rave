/*
 * api-routes
 * Defines our API routes in a single location.
 *
 */

define(function(require) {
  var auth = require('../auth');

  var AUTH_BASE = 'auth/';

  auth.factory('authApiRoutes', [
    'apiRoute',
    function(apiRoute) {
      var base = apiRoute + AUTH_BASE;
      return {
        login: base + 'login',
        logout: base + 'logout',
        verify: base + 'verify'
      };
    }
  ]);
});
