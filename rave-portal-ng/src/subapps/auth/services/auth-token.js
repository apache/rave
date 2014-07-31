/*
 * token
 * Manages our authentication token, which is stored
 * in a cookie.
 *
 */

define(function(require) {
  var auth = require('../auth');

  // The name of our cookie
  var COOKIE_NAME = 'raveToken';

  // If they choose to be remembered, it will
  // last for 20 years.
  var EXPIRE_DAYS = 20 * 365;

  // Checks if we're registered or not with the API.
  // It only checks on the first request to save on bandwidth.
  // One day we'll make an HTTP request in here
  auth.factory('authToken', [
    'ipCookie', '$rootScope',
    function(ipCookie, $rootScope) {

      return {

        // Gets your token from the cookies
        get: function() {
          return ipCookie(COOKIE_NAME);
        },

        // Sets the new token value, then emits an associated event
        set: function(newValue, persist) {
          var expireDays = persist ? EXPIRE_DAYS : undefined;
          ipCookie(COOKIE_NAME, newValue, {expires: expireDays});
          $rootScope.$emit('set:' + COOKIE_NAME, newValue, persist);
        },

        // Destroys the token, then emits an associated event
        destroy: function() {
          ipCookie.remove(COOKIE_NAME);
          $rootScope.$emit('destroy:' + COOKIE_NAME);
        }
      };
    }
  ]);
});
