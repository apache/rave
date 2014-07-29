define(function(require) {
  var angular = require('angular');
  
  angular.module('profile').config([
    '$stateProvider', '$urlRouterProvider',
    function($stateProvider, $urlRouterProvider) {
      // Our profile states
      $stateProvider.state('portal.profile', {
        url: '/profile',
        templateUrl: '/modules/profile/profile.html'
      });
    }
  ]);
});
