define(function(require) {
  require('../auth');
  var angular = require('angular');

  angular.module('auth').config([
    '$stateProvider', '$urlRouterProvider',
    function($stateProvider, $urlRouterProvider) {
      $stateProvider.state('portal.createAccount', {
        url: '/create-account',
        templateUrl: '/subapps/auth/create-account/create-account.html',
        authenticate: 'no'
      });
    }
  ]);
});
