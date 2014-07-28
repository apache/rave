define(function(require) {
  var rave = require('rave');

  rave.config([
    '$stateProvider', '$urlRouterProvider',
    function($stateProvider, $urlRouterProvider) {
      $stateProvider.state('portal.admin', {
        url: '/app/admin',
        templateUrl: '/admin/admin.html'
      });
    }
  ]);
});

