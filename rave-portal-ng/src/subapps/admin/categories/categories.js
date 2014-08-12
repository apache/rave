/*
 * categories
 * The categories module for the admin section of the app.
 *
 */

define(function(require) {
  var ng = require('angular');

  // Our module dependencies
  require('uiRouter');

  // The array of names for Angular's dependency injection
  var categoriesDependencies = [
    'ui.router'
  ];

  // Create our module
  var categories = ng.module('admin.categories', categoriesDependencies);

  // Register the providers for categories
  var categoriesResource = require('./resources/categories');
  categories.factory('categoriesResource', categoriesResource);

  var categoryResource = require('./resources/category');
  categories.factory('categoryResource', categoryResource);

  var categoriesCtrl = require('./controllers/categories');
  categories.controller('categoriesCtrl', categoriesCtrl);

  var categoryCtrl = require('./controllers/category');
  categories.controller('categoryCtrl', categoryCtrl);

  var createCtrl = require('./controllers/create');
  categories.controller('createCtrl', createCtrl);

  // Register the routes
  var routes = require('./routes');
  categories.config(routes);

  // Export the module
  return categories;
});
