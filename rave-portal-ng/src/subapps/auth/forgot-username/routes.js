/*
 * routes
 * The Angular UI-Router states for this section of the application.
 *
 */

define(function(require) {
  return ['$stateProvider', '$urlRouterProvider',
    function($stateProvider, $urlRouterProvider) {
      $stateProvider.state('portal.forgotUsername', {
        url: '/forgot-username',
        templateUrl: '/subapps/auth/forgot-username/templates/forgot-username.html',
        authenticate: 'no',
        data: {
          title: 'Forgot Username'
        }
      });
    }
  ];
});
