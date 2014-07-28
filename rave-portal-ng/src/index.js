define(function(require) {
  // Initialize libraries
  require('bootstrap');
  require('uiRouter');

  // Set up our application
  require('rave');
  
  // The base route
  require('./routes');

  // The individual modules
  require('./home/index');
  require('./admin/index');
  require('./login/index');
  require('./logout/index');
  require('./profile/index');
  require('./widget-store/index');

  var angular = require('angular');

  // Manually bootstrap the Angular app (necessary because of AMD)
  angular.element(document).ready(function() {
    angular.bootstrap(document, ['rave']);
  });
});
