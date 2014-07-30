define(function(require) {
  var angular = require('angular');
  
  angular.module('widget-store').config([
    '$stateProvider', '$urlRouterProvider',
    function($stateProvider, $urlRouterProvider) {
      $stateProvider.state('portal.widgetStore', {
        url: '/app/widget-store',
        templateUrl: '/modules/widget-store/widget-store.html',
        authenticate: true
      });
    }
  ]);
});
