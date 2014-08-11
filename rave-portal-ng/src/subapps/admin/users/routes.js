/*
 * routes
 * Routes for this subapp. Rave uses the Angular-UI UI-Router
 * library for routing, so be sure to familiarize yourself
 * with that library.
 *
 */

define(function(require) {
  var usersCtrl = require('./controllers/users');

  return ['$stateProvider', '$urlRouterProvider',
    function($stateProvider, $urlRouterProvider) {
      $stateProvider

        // The main state. Displays our list of users.
        .state('portal.admin.users', {
          url: '/users?page',
          templateUrl: '/subapps/admin/users/templates/users.html',
          authenticate: true,
          controller: usersCtrl
        })

        // Show a particular user's profile
        .state('portal.admin.users.detail', {
          url: '/users/detail-:id',
          templateUrl: '/subapps/admin/users/templates/detail.html',
          authenticate: true
        });
    }
  ];
});
