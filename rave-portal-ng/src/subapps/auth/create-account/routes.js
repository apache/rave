/*
 * routes
 * The Angular UI-Router states for this section of the application.
 *
 */

define(function(require) {
  return ['$stateProvider', '$urlRouterProvider',
    function($stateProvider, $urlRouterProvider) {
      $stateProvider.state('portal.createAccount', {
        url: '/create-account',
        templateUrl: '/subapps/auth/create-account/templates/create-account.html',
        authenticate: 'no'
      });
    }
  ];
});
