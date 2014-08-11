/*
 * auth-Api
 * The base API services for the auth module
 *
 */

define(function(require) {
  var ng = require('angular');

  var api = require('../../../providers/api/api-route');

  var authApiDependencies = [
    api.name
  ];

  // Create the API as an Angular module
  var authApi = ng.module('authApi', authApiDependencies);

  var authApiBase = require('./services/auth-api-base');
  authApi.value('authApiBase', authApiBase);

  var authApiRoutes = require('./services/auth-api-routes');
  authApi.factory('authApiRoutes', authApiRoutes);

  var authApiService = require('./services/auth-api');
  authApi.factory('authApi', authApiService);

  // Return the module
  return authApi;
});
