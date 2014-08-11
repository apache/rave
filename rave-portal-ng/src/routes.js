/*
 * routes
 * The base route for the Rave project. This makes /portal
 * the root location to access the app from.
 *
 */

define(function(require) {

  return ['$stateProvider', '$urlRouterProvider',
    function($stateProvider, $urlRouterProvider) {

      // Set up our abstract base state.
      $stateProvider.state('portal', {
        url: '/portal',
        abstract: true,
        template: '<ui-view/>'
      });
    }
  ];
});
