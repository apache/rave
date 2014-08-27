/*
 * routes
 * Routes for this subapp. Rave uses the Angular-UI UI-Router
 * library for routing, so be sure to familiarize yourself
 * with that library.
 *
 */

define(function(require) {
  var widgetsCtrl = require('./controllers/widgets');
  var widgetCtrl = require('./controllers/widget');

  return ['$stateProvider', '$urlRouterProvider',
    function($stateProvider, $urlRouterProvider) {
      $stateProvider
        .state('portal.admin.widgets', {
          url: '/widgets?page&filter',
          templateUrl: '/subapps/admin/widgets/templates/widgets.html',
          authenticate: true,
          controller: widgetsCtrl,
          resolve: {
            widgetsList: ['widgetsResource', '$stateParams',
              function(widgetsResource, $stateParams) {
                return widgetsResource.get({
                  page: $stateParams.page,
                  filter: $stateParams.filter
                });
              }
            ]
          }
        })
        .state('portal.admin.widgets.detail', {
          url: '/widget/:id',
          templateUrl: '/subapps/admin/widgets/templates/widget.html',
          authenticate: true,
          controller: widgetCtrl,
          resolve: {
            widget: ['widgetResource', '$stateParams',
              function(widgetResource, $stateParams) {
                return widgetResource.get({id: $stateParams.id});
              }]
          }
        });
    }
  ];
});
