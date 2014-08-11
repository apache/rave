/*
 * forgotPasswordApiRoutes
 * The server-side API routes for forgotten passwords
 *
 */

define(function(require) {
  return ['apiRoute', 'authApiBase',
    function(apiRoute, authApiBase) {
      var base = apiRoute + authApiBase;
      return {
        forgotPassword: base + 'forgot-password',
      };
    }
  ];
});
