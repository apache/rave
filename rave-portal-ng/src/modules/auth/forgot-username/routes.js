define(function(require) {
  var angular = require('angular');

  angular.module('auth').config([
    '$stateProvider', '$urlRouterProvider',
    function($stateProvider, $urlRouterProvider) {
      $stateProvider.state('portal.forgotUsername', {
        url: '/forgot-username',
        templateUrl: '/modules/auth/forgot-username/forgot-username.html'
      });
    }
  ]);
});
