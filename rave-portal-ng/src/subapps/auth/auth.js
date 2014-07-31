define(function(require) {
  var angular = require('angular');

  var auth = angular.module('auth', [
    'ngCookies'
  ]);

  return auth;
});
