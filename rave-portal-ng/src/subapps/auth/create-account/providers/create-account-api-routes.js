/*
 * auth-api-routes
 * Defines our API routes in a single location.
 *
 */

define(function(require) {
  var auth = require('../../auth');
  require('../../services/auth-api-base');

  auth.factory('createAccountApiRoutes', [
    'apiRoute', 'authApiBase',
    function(apiRoute, authApiBase) {
      var base = apiRoute + authApiBase;
      return {
        createAccount: base + 'create-account',
      };
    }
  ]);
});
