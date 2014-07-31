/*
 * securityService
 * This is our single point-of-entry for authentication in our app. It exposes
 * all of the methods our app needs to handle authentication.
 * 
 * Given the nature of authentication these services are typically related to routing.
 *
 * It provides three services
 *
 * 1. Attempts to log the user in with the given credentials
 * 2. Determines if our user is verified (with a cached server response)
 * 3. Logs out the user
 * 
 */

define(function(require) {
  var auth = require('../auth');
  require('./auth-api');
  require('./auth-cache');
  require('./auth-token');
  require('./location-cache');

  auth.factory('security', [
    '$rootScope', 'authToken', '$q', 'authApi', 'authCache', '$state', 'locationCache',
    function($rootScope, authToken, $q, authApi, authCache, $state, locationCache) {
      return {

        // Returns a promise that resolves true or rejects false.
        // This promise represents whether the user is currently verified.
        // If the token is stored in a cookie, it checks if we've been verified
        // with the server for this session. If we have, it resolves true.
        // If we haven't, it checks the token with the server. If the server returns
        // false, then the token is destroyed.
        verify: function() {
          var myToken = authToken.get();
          var response = $q.defer();

          // If we're verified, then we can move along.
          if (myToken && authCache.get('verified') === true) {
            response.resolve(true);
          }

          // If we have no token or aren't verified, then we reject it.
          else if (myToken === null || authCache.get('verified') === false) {
            response.reject(false);
          }

          // Lastly, we verify with the server using our token
          else {
            authApi.verify(myToken)
              .then(function(res) {
                $rootScope.authenticated = true;
                authCache.put('verified', true);
                response.resolve(true);
              })
              .catch(function(err) {
                authToken.destroy();
                response.reject(false);
              });
          }
          return response.promise;
        },

        // Log us in with the credentials
        // Returns the XHR result of the login attempt.
        // Errors aren't handled within this method.
        login: function(credentials) {

          return authApi.login(credentials)

            // On success, we do quite a few things. We set that we're
            // verified on the $rootScope, we cache our verification,
            // we set our token, and lastly we 
            .then(function(res) {
              var user = res.data.user;
              $rootScope.authenticated = true;
              authCache.put('verified', true);
              authToken.set(user.token, credentials.remember);

              var toState = locationCache.get('toState') || 'portal.home';
              var toParams = locationCache.get('toParams') || undefined;

              locationCache.removeAll();

              $state.transitionTo(toState, toParams);
            });
        },

        // Logs us out.
        logout: function() {
          $rootScope.authenticated = false;
          authCache.put('verified', false);
          authToken.destroy();
          $state.transitionTo('portal.login');
        },
      };
    }
  ]);
});
