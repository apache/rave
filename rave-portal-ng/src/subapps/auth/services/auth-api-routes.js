/*
 * auth-api-routes
 * Defines our API routes in a single location.
 *
 */

define(function(require) {
  var auth = require('../auth');
  require('./auth-api-base');

  auth.factory('authApiRoutes', [
    'apiRoute', 'authApiBase',
    function(apiRoute, authApiBase) {
      var base = apiRoute + authApiBase;
      return {
        login: base + 'login',
        logout: base + 'logout',
        verify: base + 'verify'
      };
    }
  ]);
});
