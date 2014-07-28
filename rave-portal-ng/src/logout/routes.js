define(function(require) {
  var rave = require('rave');
  rave.config([
    '$stateProvider', '$urlRouterProvider',
    function($stateProvider, $urlRouterProvider) {
      $stateProvider.state('portal.logout', {
        url: '/logout',
        template: '<ui-view/>'
      });
    }
  ]);
});
