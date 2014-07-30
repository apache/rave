define(function(require) {
  var angular = require('angular');

  angular.module('admin').config([
    '$stateProvider', '$urlRouterProvider',
    function($stateProvider, $urlRouterProvider) {
      $stateProvider
        .state('portal.admin', {
          url: '/app/admin',
          templateUrl: '/modules/admin/admin.html',
          authenticate: true
        })
        .state('portal.admin.home', {
          url: '/',
          templateUrl: '/modules/admin/home/home.html',
          authenticate: true
        });
    }
  ]);
});

