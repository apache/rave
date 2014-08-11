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

      // Our widget store states. This is configuration from Angular's ui-router.
      $stateProvider.state('portal.widgetStore', {
        url: '/app/widget-store',
        templateUrl: '/subapps/widget-store/templates/widget-store.html',
        authenticate: true
      });
    }
  ];
});
