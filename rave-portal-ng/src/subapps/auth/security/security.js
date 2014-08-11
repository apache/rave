/*
 * security
 * A module for security-related features
 *
 */

define(function(require) {
  var ng = require('angular');

  var api = require('../api/api');

  var securityDependencies = [
    api.name
  ];

  // Create the Security module
  var security = ng.module('security', securityDependencies);

  // Register our services
  var authCache = require('./services/auth-cache');
  security.factory('authCache', authCache);

  var authToken = require('./services/auth-token');
  security.factory('authToken', authToken);

  var locationCache = require('./services/location-cache');
  security.factory('locationCache', locationCache);

  var securityService = require('./services/security');
  security.factory('security', securityService);

  // Configure route interception
  var routeIntercept = require('./route-intercept');
  security.run(routeIntercept);

  // Return the module
  return security;
});
