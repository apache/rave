define(function(require) {
  var angular = require('angular');

  angular.module('auth').config([
    '$stateProvider', '$urlRouterProvider',
    function($stateProvider, $urlRouterProvider) {
      $stateProvider
        .state('portal.login', {
          url: '/login',
          templateUrl: '/modules/auth/login.html',
          authenticate: 'no'
        })
        .state('portal.logout', {
          url: '/logout',
          template: '<ui-view/>'
        });
    }
  ]);
});
