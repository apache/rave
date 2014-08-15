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
  return ['$rootScope', 'authToken', '$q', 'authApi', 'authCache', '$state', 'locationCache', '$http',
    function($rootScope, authToken, $q, authApi, authCache, $state, locationCache, $http) {

      // Naive setting of our token. This is for resources that are requested on page load;
      // it's unverified, though, so it might not even work, and, in those cases, the resource
      // pages are set up to display the proper error.
      $http.defaults.headers.common.Authorization = 'Basic ' + authToken.get();

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
                $rootScope.currentUser = res.data.user;
                $http.defaults.headers.common.Authorization = 'Basic ' + myToken;
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
              $rootScope.currentUser = user;
              authCache.put('verified', true);
              authToken.set(res.data.token, credentials.remember);

              $http.defaults.headers.common.Authorization = 'Basic ' + res.data.token;

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
  ];
});
