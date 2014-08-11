/*
 * createAccountApiRoutes
 * Defines our API routes in a single location.
 *
 */

define(function(require) {
  return ['apiRoute', 'authApiBase',
    function(apiRoute, authApiBase) {
      var base = apiRoute + authApiBase;
      return {
        createAccount: base + 'create-account',
      };
    }
  ];
});
