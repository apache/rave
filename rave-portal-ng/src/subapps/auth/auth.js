define(function(require) {
  var angular = require('angular');

  var auth = angular.module('auth', [
    'ipCookie'
  ]);

  return auth;
});
