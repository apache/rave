/*
 * routes
 * Routes for this subapp. Rave uses the Angular-UI UI-Router
 * library for routing, so be sure to familiarize yourself
 * with that library.
 *
 */

define(function(require) {
  var preferencesCtrl = require('./controllers/preferences');

  return [
    '$stateProvider', '$urlRouterProvider',
    function($stateProvider, $urlRouterProvider) {

      // Our preferences state
      $stateProvider.state('portal.admin.preferences', {
          url: '/preferences',
          templateUrl: '/subapps/admin/preferences/templates/preferences.html',
          authenticate: true,
          controller: preferencesCtrl,
          onExit: ['preferencesMessages', function(messages) {
            messages.clearMessage();
          }],
          resolve: {
            preferences: ['preferencesResource', function(preferencesResource) {
             return preferencesResource.get(); 
            }]
          }
        });
    }
  ];
});
