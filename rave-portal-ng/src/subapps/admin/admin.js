/*
 * routes
 * The containing module for the admin section of Rave.
 *
 */

define(function(require) {
  var ng = require('angular');

  // Our dependencies
  require('uiRouter');
  require('../../providers/pagination/pagination');

  // Our sub-subapps
  var categories = require('./categories/categories');
  var home = require('./home/home');
  var preferences = require('./preferences/preferences');
  var widgets = require('./widgets/widgets');
  var users = require('./users/users');

  // An array of our dependencies for Angular's DI
  var adminDependencies = [
    'ui.router',
    'ngResource',
    'pagination',
    preferences.name,
    categories.name,
    home.name,
    widgets.name,
    users.name
  ];

  // Create our admin subapp as a module
  var admin = ng.module('admin', adminDependencies);

  // Register our routes
  var routes = require('./routes');
  admin.config(routes);

  // Export the admin module
  return admin;
});
