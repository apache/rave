define(function(require) {
  var auth = require('./auth');

  require('./services/security');
  
  auth.config([
    '$stateProvider', '$urlRouterProvider',
    function($stateProvider, $urlRouterProvider) {
      $stateProvider

        // Our login page.
        // We don't allow you to access this page if you're authenticated.
        .state('portal.login', {
          url: '/login',
          templateUrl: '/subapps/auth/login.html',
          authenticate: 'no'
        })

        // The logout page. All this
        // does is delegate to our securityService to
        // perform the actual act of logging out.
        .state('portal.logout', {
          url: '/logout',
          template: '<ui-view/>',
          onEnter: [
            'security',
            function(security) {
              security.logout();
            }
          ]
        });
    }
  ]);
});
