define(function(require) {
  // Initialize libraries
  require('bootstrap');
  require('uiRouter');
  require('angularMocks');
  require('angularCookies');
  
  // Set up our application
  require('rave');

  // The mock API
  require('./api/bootstrap');
  
  // The base route
  require('./routes');

  // Our filters
  require('./tools/filters/index');

  var angular = require('angular');

  // Manually bootstrap the Angular app (necessary because of AMD)
  angular.element(document).ready(function() {
    angular.bootstrap(document, ['rave']);
  });
});
