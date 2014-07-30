define(function(require) {
  var angular = require('angular');

  var auth = angular.module('auth', [
    'ngCookies'
  ]);

  // Store our authentication token as a cookie
  auth.factory('authToken', [
    '$cookies',
    function($cookies) {
      return $cookies.raveToken || null;
    }
  ]);

  // Checks if we're registered or not with the API.
  // It only checks on the first request to save on bandwidth.
  // One day we'll make an HTTP request in here
  auth.factory('authenticated', [
    'authToken', '$cookies',
    function(authToken, $cookies) {

      // For now, we're always authenticated. How nice!
      // In the future, it will destroy the token if we're not
      // authorized.
      if (false) {
        $cookies.raveToken = null;
        return false;
      } else {
        return true;
      }

    }
  ]);

  // Intercept our routes and check if we're logged in or not
  auth.run([
    '$rootScope', '$state', 'authenticated',
    function($rootScope, $state, authenticated) {

      // Intercept every $stateChangeStart event.
      $rootScope.$on('$stateChangeStart', function(event, toState, toParams){
        if (toState.authenticate && !authenticated){

          // Store their attempted state in a cache.
          // This way, once they log in we can redirect them to
          // their intended state
          $rootScope.loginCache = {
            transitionTo: toState,
            params: toParams
          };

          // Redirect them to the login page
          $state.transitionTo('portal.login');

          // Finally, prevent the intended state change
          event.preventDefault(); 
        }
      });
    }
  ]);

  return auth;
});
