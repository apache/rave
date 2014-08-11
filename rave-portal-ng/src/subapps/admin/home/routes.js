/*
 * routes
 * The UI states for the homepage of the admin section
 *
 */

define(function(require) {
  return ['$stateProvider', '$urlRouterProvider',
    function($stateProvider, $urlRouterProvider) {

      // The state for the home page
      $stateProvider.state('portal.admin.home', {
          url: '/',
          templateUrl: '/subapps/admin/home/templates/home.html',
          authenticate: true
        });
    }
  ];
});

