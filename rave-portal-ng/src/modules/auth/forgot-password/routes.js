define(function(require) {
  require('../auth');
  var angular = require('angular');

  angular.module('auth').config([
    '$stateProvider', '$urlRouterProvider',
    function($stateProvider, $urlRouterProvider) {
      $stateProvider.state('portal.forgotPassword', {
        url: '/forgot-password',
        templateUrl: '/modules/auth/forgot-password/forgot-password.html',
        authenticate: 'no'
      });
    }
  ]);
});
