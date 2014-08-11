/*
 * routes
 * The Angular UI-Router states for this section of the application.
 *
 */

define(function(require) {
  return['$stateProvider', '$urlRouterProvider',
    function($stateProvider, $urlRouterProvider) {

      // The logout page. All this
      // does is delegate to our securityService to
      // perform the actual act of logging out.
      $stateProvider.state('portal.logout', {
        url: '/logout',
        template: '<ui-view/>',
        onEnter: ['security',
          function(security) {
            security.logout();
          }
        ]
      });
    }
  ];
});
