/*
 * forgotPasswordApi
 * Our client-side API for our server-side API
 *
 */

define(function(require) {
  return ['$http', 'forgotPasswordApiRoutes',
    function($http, apiRoutes) {

      // Each of our auth routes.
      return {
        forgotPassword: function(email) {
          return $http.post(apiRoutes.forgotPassword, {email:email});
        }
      };
    }
  ];
});
