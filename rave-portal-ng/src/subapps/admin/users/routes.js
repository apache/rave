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
  var createUserCtrl = require('./controllers/create-user');

  return ['$stateProvider', '$urlRouterProvider',
    function($stateProvider, $urlRouterProvider) {
      $stateProvider

        // The main state. Displays our list of users.
        .state('portal.admin.users', {
          url: '/users?page',
          templateUrl: '/subapps/admin/users/templates/users.html',
          authenticate: true,
          controller: usersCtrl,
          onExit: ['usersMessages', function(usersMessages) {
            usersMessages.clearMessage();
          }],
          resolve: {
            usersList: ['usersResource', '$stateParams',
              function(usersResource, $stateParams) {
                return usersResource.get({
                  page: $stateParams.page
                });
              }]
          }
        })

        // Show a particular user's profile
        .state('portal.admin.users.detail', {
          url: '/detail-:id',
          templateUrl: '/subapps/admin/users/templates/user.html',
          authenticate: true,
          controller: userCtrl,
          resolve: {
            user: ['userResource', '$stateParams',
              function(userResource, $stateParams) {
                return userResource.get({id: $stateParams.id});
              }]
          }
        })

        // Create a new account
        .state('portal.admin.users.create', {
          url: '/users/create-user',
          templateUrl: '/subapps/admin/users/templates/create-user.html',
          authenticate: true,
          controller: createUserCtrl
        });
    }
  ];
});
