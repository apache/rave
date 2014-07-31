define(function(require) {
  require('./admin');
  var angular = require('angular');

  angular.module('admin').config([
    '$stateProvider', '$urlRouterProvider',
    function($stateProvider, $urlRouterProvider) {
      $stateProvider
        .state('portal.admin', {
          url: '/app/admin',
          templateUrl: '/subapps/admin/admin.html',
          authenticate: true
        })
        .state('portal.admin.home', {
          url: '/',
          templateUrl: '/subapps/admin/home/home.html',
          authenticate: true
        });
    }
  ]);
});

