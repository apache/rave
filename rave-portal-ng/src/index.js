/*
 * index
 * Bootstraps our app, loading libraries (like Bootstrap) that aren't Angular modules,
 * but need to be booted up separately.
 * It also pulls in the Rave Angular module and manually bootstraps it.
 *
 */

define(function(require) {

  // Initialize any non-Angular Javascript libraries here
  require('bootstrap');
  
  // Create our application
  require('rave');

  // Load the mock API (development only)
  require('./api/bootstrap');
  // Load the mock bootstrap response (development only)
  require('./api/bootstrap-data');
  
  // Our things
  require('./providers/filters/index');

  // Manually bootstrap the Angular app (necessary because of AMD)
  var ng = require('angular');
  ng.element(document).ready(function() {
    ng.bootstrap(document, ['rave']);
  });
});
