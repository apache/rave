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

      // Make this our default route
      $urlRouterProvider.otherwise('/portal/home');

      // This state doesn't append anything
      // to our abstract base state. This makes
      // the url '/portal' correspond to our home state.
      $stateProvider.state('portal.home', {
        url: '/home',
        templateUrl: '/subapps/home/templates/home.html',
        authenticate: true,
        data: {
          title: 'Home'
        }
      });
    }
  ];
});
