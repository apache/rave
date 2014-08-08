define(function(require) {
  require('../admin');
  var angular = require('angular');
  var categoriesCtrl = require('./controllers/categories');
  var categoryCtrl = require('./controllers/category');

  angular.module('admin').config([
    '$stateProvider', '$urlRouterProvider',
    function($stateProvider, $urlRouterProvider) {
      $stateProvider
        .state('portal.admin.categories', {
          url: '/categories',
          templateUrl: '/subapps/admin/categories/categories.html',
          authenticate: true,
          controller: categoriesCtrl
        })
        .state('portal.admin.categories.category', {
          url: '/categories/category-:id',
          templateUrl: '/subapps/admin/categories/category/category.html',
          authenticate: true,
          controller: categoryCtrl
        });
    }
  ]);
});
