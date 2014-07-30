define(function(require) {
  var angular = require('angular');

  angular.module('home').config([
    '$stateProvider', '$urlRouterProvider',
    function($stateProvider, $urlRouterProvider) {
      // Make this our default route
      $urlRouterProvider.otherwise('/portal/home');

      // This state doesn't append anything
      // to our abstract base state. This makes
      // the url '/portal' correspond to our home state.
      $stateProvider.state('portal.home', {
        url: '/home',
        controller: 'homeController',
        templateUrl: '/modules/home/home.html',
        authenticate: true
      });
    }
  ]);
});
