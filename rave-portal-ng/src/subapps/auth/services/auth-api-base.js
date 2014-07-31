/*
 * auth-api-base
 * The base route for API routes
 *
 */

define(function(require) {
  var auth = require('../auth');

  auth.value('authApiBase', 'auth/');
});
