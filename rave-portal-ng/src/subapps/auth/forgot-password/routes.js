/*
 * routes
 * The Angular UI-Router states for this section of the application.
 *
 */

define(function(require) {
  return ['$stateProvider', '$urlRouterProvider',
    function($stateProvider, $urlRouterProvider) {
      $stateProvider.state('portal.forgotPassword', {
        url: '/forgot-password',
        templateUrl: '/subapps/auth/forgot-password/templates/forgot-password.html',
        authenticate: 'no',
        data: {
          title: 'Forgot Password'
        }
      });
    }
  ];
});
