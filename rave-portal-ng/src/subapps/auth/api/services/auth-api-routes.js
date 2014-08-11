/*
 * authApiRoutes
 * Defines our API routes in a single location.
 *
 */

define(function(require) {
  return ['apiRoute', 'authApiBase',
    function(apiRoute, authApiBase) {
      var base = apiRoute + authApiBase;
      return {
        login: base + 'login',
        logout: base + 'logout',
        verify: base + 'verify'
      };
    }
  ];
});
