define(function(require) {
  // Initialize libraries
  require('bootstrap');
  require('uiRouter');
  require('angularMocks');

  // Set up our application
  require('rave');

  // The mock API
  require('./api/bootstrap');
  
  // The base route
  require('./routes');

  // The individual modules
  require('./modules/home/index');
  require('./modules/admin/index');
  require('./modules/log-in/index');
  require('./modules/log-out/index');
  require('./modules/profile/index');
  require('./modules/widget-store/index');

  // Our filters
  require('./tools/filters/index');

  var angular = require('angular');

  // Manually bootstrap the Angular app (necessary because of AMD)
  angular.element(document).ready(function() {
    angular.bootstrap(document, ['rave']);
  });
});
