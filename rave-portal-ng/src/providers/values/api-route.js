define(function(require) {
  var rave = require('rave');

  var API_VERSION = 1;

  rave.value('apiRoute', '/api/v' + API_VERSION + '/');
});
