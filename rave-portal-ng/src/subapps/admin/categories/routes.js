/*
 * routes
 * UI-router states for this section
 *
 */

define(function(require) {
  var categoriesCtrl = require('./controllers/categories');
  var categoryCtrl = require('./controllers/category');

  return ['$stateProvider', '$urlRouterProvider',
    function($stateProvider, $urlRouterProvider) {

      // Configure our routes as ui-router states
      $stateProvider

        // The state that lists the categories
        .state('portal.admin.categories', {
          url: '/categories',
          templateUrl: '/subapps/admin/categories/templates/categories.html',
          authenticate: true,
          controller: categoriesCtrl
        })

        // The detail page for a single category
        .state('portal.admin.categories.category', {
          url: '/categories/category-:id',
          templateUrl: '/subapps/admin/categories/templates/category.html',
          authenticate: true,
          controller: categoryCtrl
        });
    }
  ];
});
