define(function(require) {
  var rave = require('rave');
  rave.config([
    '$stateProvider', '$urlRouterProvider',
    function($stateProvider, $urlRouterProvider) {
      $stateProvider
        .state('portal.admin.categories', {
          url: '/app/admin/categories',
          templateUrl: '/admin/categories/categories.html'
        })
        .state('portal.admin.categories.category', {
          url: '/app/admin/categories/category',
          templateUrl: '/admin/categories/category/category.html'
        });
    }
  ]);
});
