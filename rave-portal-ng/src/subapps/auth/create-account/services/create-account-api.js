/*
 * createAccountApi
 * Our client-side API for interacting with the server-side API
 *
 */

define(function(require) {
  return ['$http', 'createAccountApiRoutes',
    function($http, apiRoutes) {

      // Each of our auth routes.
      return {
        createAccount: function(accountInfo) {
          return $http.post(apiRoutes.createAccount, accountInfo);
        }
      };
    }
  ];
});
