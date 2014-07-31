/*
 * token
 * Manages our authentication token, which is stored
 * in a cookie.
 *
 */

define(function(require) {
  var auth = require('../auth');

  var COOKIE_NAME = 'raveToken';

  // Checks if we're registered or not with the API.
  // It only checks on the first request to save on bandwidth.
  // One day we'll make an HTTP request in here
  auth.factory('authToken', [
    '$cookieStore', '$rootScope',
    function($cookieStore, $rootScope) {
      return {

        // Gets your token from the cookies
        get: function() {
          return $cookieStore.get(COOKIE_NAME);
        },

        // Sets the new token value, then emits an associated event
        set: function(newValue) {
          $cookieStore.put(COOKIE_NAME, newValue);
          $rootScope.$emit('set:' + COOKIE_NAME, newValue);
        },

        // Destroys the token, then emits an associated event
        destroy: function() {
          $cookieStore.remove(COOKIE_NAME);
          $rootScope.$emit('destroy:' + COOKIE_NAME);
        }
      };
    }
  ]);
});
