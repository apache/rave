define(function(require) {
  var rave = require('rave');
  rave.config([
    '$stateProvider', '$urlRouterProvider',
    function($stateProvider, $urlRouterProvider) {
      $stateProvider
        .state('portal.admin.preferences', {
          url: '/app/admin/preferences',
          templateUrl: '/admin/preferences/preferences.html'
        })
        .state('portal.admin.preferences.detail', {
          url: '/app/admin/preferences/detail',
          templateUrl: '/admin/preferences/detail/detail.html'
        });
    }
  ]);
});
