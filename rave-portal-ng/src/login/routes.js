define(function(require) {
  var rave = require('rave');
  rave.config([
    '$stateProvider', '$urlRouterProvider',
    function($stateProvider, $urlRouterProvider) {
      // Our profile states
      $stateProvider.state('portal.login', {
        url: '/login',
        templateUrl: '/login/login.html'
      });
    }
  ]);
});
