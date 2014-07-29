define(function(require) {
  var angular = require('angular');
  angular.module('logout').config([
    '$stateProvider', '$urlRouterProvider',
    function($stateProvider, $urlRouterProvider) {
      $stateProvider.state('portal.logout', {
        url: '/logout',
        template: '<ui-view/>'
      });
    }
  ]);
});
