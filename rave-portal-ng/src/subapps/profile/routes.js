/*
 * routes
 * Routes for this subapp. Rave uses the Angular-UI UI-Router
 * library for routing, so be sure to familiarize yourself
 * with that library.
 *
 */

define(function(require) {
  return ['$stateProvider', '$urlRouterProvider',
    function($stateProvider, $urlRouterProvider) {

      // Our profile states. This is configuration from Angular's ui-router.
      $stateProvider.state('portal.profile', {
        url: '/profile',
        templateUrl: '/subapps/profile/templates/profile.html',
        authenticate: true
      });
    }
  ];
});
