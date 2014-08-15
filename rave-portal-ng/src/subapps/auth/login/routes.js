/*
 * routes
 * The base route for the Rave project. This makes /portal
 * the root location to access the app from.
 *
 */

define(function(require) {
  return['$stateProvider', '$urlRouterProvider',
    function($stateProvider, $urlRouterProvider) {

      // Our login page.
      // We don't allow you to access this page if you're authenticated.
      $stateProvider.state('portal.login', {
        url: '/login',
        templateUrl: '/subapps/auth/login/templates/login.html',
        authenticate: 'no',
        data: {
          title: 'Login'
        }
      });
    }
  ];
});
