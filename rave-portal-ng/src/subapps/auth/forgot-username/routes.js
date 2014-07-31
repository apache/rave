define(function(require) {
  require('../auth');
  var angular = require('angular');

  angular.module('auth').config([
    '$stateProvider', '$urlRouterProvider',
    function($stateProvider, $urlRouterProvider) {
      $stateProvider.state('portal.forgotUsername', {
        url: '/forgot-username',
        templateUrl: '/subapps/auth/forgot-username/forgot-username.html',
        authenticate: 'no'
      });
    }
  ]);
});
