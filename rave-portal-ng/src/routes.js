define(function(require) {
  var rave = require('rave');
  
  rave.config([
    '$stateProvider', '$urlRouterProvider',
    function($stateProvider, $urlRouterProvider) {

      // Set up our abstract base state.
      $stateProvider.state('portal', {
          url: '/portal',
          abstract: true,
          template: '<ui-view/>'
        });
      }
  ]);
});
