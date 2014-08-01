/*
 * api
 * Exposes high-level methods for interacting with the remote API for authentication.
 * All methods defer to $http and, consequently, return an HttpPromise.
 *
 * There is NEVER a need to use this service directly. Instead, all external auth-related
 * activities should go through the securityService.
 *
 */

define(function(require) {
  var auth = require('../../auth');
  require('./create-account-api-routes');

  auth.factory('createAccountApi', [
    '$http', 'createAccountApiRoutes',
    function($http, apiRoutes) {

      // Each of our auth routes.
      return {
        createAccount: function(accountInfo) {
          return $http.post(apiRoutes.createAccount, accountInfo);
        }
      };
    }
  ]);
});
