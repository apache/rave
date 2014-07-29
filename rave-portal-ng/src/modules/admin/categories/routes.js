define(function(require) {
  var angular = require('angular');

  angular.module('admin').config([
    '$stateProvider', '$urlRouterProvider',
    function($stateProvider, $urlRouterProvider) {
      $stateProvider
        .state('portal.admin.categories', {
          url: '/app/admin/categories',
          templateUrl: '/modules/admin/categories/categories.html'
        })
        .state('portal.admin.categories.category', {
          url: '/app/admin/categories/category',
          templateUrl: '/modules/admin/categories/category/category.html'
        });
    }
  ]);
});
