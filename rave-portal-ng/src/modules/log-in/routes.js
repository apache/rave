define(function(require) {
  var angular = require('angular');

  angular.module('login').config([
    '$stateProvider', '$urlRouterProvider',
    function($stateProvider, $urlRouterProvider) {
      $stateProvider.state('portal.login', {
        url: '/login',
        templateUrl: '/modules/log-in/login.html'
      });
    }
  ]);
});
