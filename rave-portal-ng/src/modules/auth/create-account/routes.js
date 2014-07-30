define(function(require) {
  var angular = require('angular');

  angular.module('auth').config([
    '$stateProvider', '$urlRouterProvider',
    function($stateProvider, $urlRouterProvider) {
      $stateProvider.state('portal.createAccount', {
        url: '/create-account',
        templateUrl: '/modules/auth/create-account/create-account.html',
        authenticate: 'no'
      });
    }
  ]);
});
