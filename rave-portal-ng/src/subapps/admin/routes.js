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

      // The base state for the admin page
      $stateProvider.state('portal.admin', {
          url: '/app/admin',
          templateUrl: '/subapps/admin/templates/admin.html',
          authenticate: true,
          data: {
            title: 'Admin'
          }
        });
    }
  ];
});

