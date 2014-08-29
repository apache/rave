/*
 * pageTitle
 * A directive that updates our page title.
 *
 * The title is made up of two parts: the base comes from the ui-router
 * states, and the suffix comes from the configuration specified by the
 * user.
 *
 */

define(function(require) {
  var ng = require('angular');

  var pageTitle = ng.module('pageTitle', []);

  pageTitle.directive('updateTitle', [
    '$rootScope', '$state',
    function($rootScope, $state) {

      // We set our default values here.
      var suffix = '';
      var defaultTitle = '';

      // Generate our base title based on the ui-router.
      var titleBase = function() {
        var currentState = $state.current;
        var data = currentState.data ? currentState.data : {};
        return data.title ? data.title : defaultTitle;
      };
      
      return {
        link: function(scope, element) {

          // Listen to changes in the preferences. This can happen when
          // the user updates the preferences from the admin panel,
          // or during the initial page load. When that happens, we
          // update the suffix, then the page title itself.
          scope.$watch('preferences', function(newValue, oldValue) {
            suffix = newValue.titleSuffix || '';
            element.text(titleBase() + suffix);
          });

          // Anytime the route change is successful we update our page
          // title based on the 
          $rootScope.$on('$stateChangeSuccess', function() {
            element.text(titleBase() + suffix);
          });
        }
      };
    }
  ]);

  return pageTitle;
});
