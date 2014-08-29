/*
 * tokenUtil
 * Utility methods for working with tokens.
 * Eventually we will also generate tokens in here.
 *
 *
 */

define(function(require) {

  var tokenUtil = {

    // Create a token
    generateToken: function() {
      var length = 32, token = '';
      for (var i = 0; i < length; i++) {
        token += Math.random().toString(36).substr(2,1);
      }
      return token;
    },

    // Get the token from the headers. If it doesn't
    // exist, or is invalid, then a value of false is returned
    tokenFromHeaders: function(headers) {
      if (!headers.Authorization) {
        return false;
      }

      var token = headers.Authorization.replace('Basic ', '');
      if (token.length !== 32) {
        return false;
      }

      return token;
    }
  };

  return tokenUtil;
});
