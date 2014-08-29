define(function(require) {
  var ng = require('angular');

  var API_VERSION = 1;

  var api = ng.module('api', []);

  api.value('apiRoute', '/api/v' + API_VERSION + '/');

  return api;
});
