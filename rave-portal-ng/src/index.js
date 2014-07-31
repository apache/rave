define(function(require) {
  // Initialize libraries
  require('bootstrap');
  require('uiRouter');
  require('angularMocks');
  require('angularCookies');
  require('angularResource');
  
  // Set up our application
  require('rave');

  // The mock API
  require('./api/bootstrap');
  
  // The base route
  require('./routes');

  // Our things
  require('./providers/filters/index');
  require('./providers/values/api-route');

  var angular = require('angular');

  // Manually bootstrap the Angular app (necessary because of AMD)
  angular.element(document).ready(function() {
    angular.bootstrap(document, ['rave']);
  });
});
