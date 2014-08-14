/*
 * routes
 * Routes for this subapp. Rave uses the Angular-UI UI-Router
 * library for routing, so be sure to familiarize yourself
 * with that library.
 *
 */

define(function(require) {
  var usersCtrl = require('./controllers/users');
  var userCtrl = require('./controllers/user');

  return ['$stateProvider', '$urlRouterProvider',
    function($stateProvider, $urlRouterProvider) {
      $stateProvider

        // The main state. Displays our list of users.
        .state('portal.admin.users', {
          url: '/users?page',
          templateUrl: '/subapps/admin/users/templates/users.html',
          authenticate: true,
          controller: usersCtrl,
          resolve: {
            usersList: ['usersResource',
              function(usersResource) {
                return usersResource.query();
              }]
          }
        })

        // Show a particular user's profile
        .state('portal.admin.users.detail', {
          url: '/users/detail-:id',
          templateUrl: '/subapps/admin/users/templates/user.html',
          authenticate: true,
          controller: userCtrl,
          resolve: {
            user: ['userResource', '$stateParams',
              function(userResource, $stateParams) {
                return userResource.get({id: $stateParams.id});
              }]
          }
        });
    }
  ];
});
