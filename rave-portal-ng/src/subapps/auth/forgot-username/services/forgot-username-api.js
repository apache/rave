/*
 * forgotUsernameApi
 * Posts to the server that the user forgot their username!
 *
 */

define(function(require) {
  return ['$http', 'forgotUsernameApiRoutes',
    function($http, apiRoutes) {

      // There's just a single route for the forgot username section
      return {
        forgotUsername: function(email) {
          return $http.post(apiRoutes.forgotUsername, {email:email});
        }
      };
    }
  ];
});
