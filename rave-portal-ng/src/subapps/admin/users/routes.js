define(function(require) {
  require('../admin');
  var angular = require('angular');

  angular.module('admin').config([
    '$stateProvider', '$urlRouterProvider',
    function($stateProvider, $urlRouterProvider) {
      $stateProvider
        .state('portal.admin.users', {
          url: '/users?page',
          templateUrl: '/subapps/admin/users/users.html',
          authenticate: true
        })
        .state('portal.admin.users.detail', {
          url: '/users/detail-:id',
          templateUrl: '/subapps/admin/users/detail/detail.html',
          authenticate: true
        });
    }
  ]);
});
