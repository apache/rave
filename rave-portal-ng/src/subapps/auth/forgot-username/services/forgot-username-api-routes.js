/*
 * forgotUsernameApiRoutes
 * Define our API routes here. Consumed by the API itself.
 *
 */

define(function(require) {
  return ['apiRoute', 'authApiBase',
    function(apiRoute, authApiBase) {
      var base = apiRoute + authApiBase;
      return {
        forgotUsername: base + 'forgot-username',
      };
    }
  ];
});
