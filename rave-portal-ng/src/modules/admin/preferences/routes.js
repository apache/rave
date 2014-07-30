define(function(require) {
  var angular = require('angular');

  angular.module('admin').config([
    '$stateProvider', '$urlRouterProvider',
    function($stateProvider, $urlRouterProvider) {
      $stateProvider
        .state('portal.admin.preferences', {
          url: '/app/admin/preferences',
          templateUrl: '/modules/admin/preferences/preferences.html',
          authenticate: true
        })
        .state('portal.admin.preferences.detail', {
          url: '/app/admin/preferences/detail',
          templateUrl: '/modules/admin/preferences/detail/detail.html',
          authenticate: true
        });
    }
  ]);
});
