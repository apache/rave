/*
 * authApi
 * Exposes high-level methods for interacting with the remote API for authentication.
 * All methods defer to $http and, consequently, return an HttpPromise.
 *
 * There is NEVER a need to use this service directly. Instead, all external auth-related
 * activities should go through the securityService.
 *
 */

define(function(require) {
  return ['$http', 'authApiRoutes',
    function($http, authApiRoutes) {

      // Each of our auth routes.
      return {
        login: function(credentials) {
          return $http.post(authApiRoutes.login, credentials);
        },
        logout: function() {
          return $http.post(authApiRoutes.logout);
        },
        verify: function(token) {
          return $http.post(authApiRoutes.verify, {token: token});
        }
      };
    }
  ];
});
