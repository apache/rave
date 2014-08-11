/*
 * routes
 * Routes for this subapp. Rave uses the Angular-UI UI-Router
 * library for routing, so be sure to familiarize yourself
 * with that library.
 *
 */

define(function(require) {
  return ['$stateProvider', '$urlRouterProvider',
    function($stateProvider, $urlRouterProvider) {
      $stateProvider
        .state('portal.admin.widgets', {
          url: '/widgets',
          templateUrl: '/subapps/admin/widgets/templates/widgets.html',
          authenticate: true,
          controller: function($scope) {
            $scope.currentPage = 0;
            $scope.listSize = 5;
            $scope.firstItem = function() {
              return $scope.currentPage * $scope.listSize + 1;
            };
            $scope.lastItem = function() {
              return $scope.firstItem() + $scope.listSize - 1;
            };
            $scope.numberOfPages = function() {
              return Math.ceil( $scope.widgets.length / $scope.listSize );                
            };
            $scope.widgets = [
              {name:'one', id:28},
              {name:'two', id:29},
              {name:'three', id:30},
              {name:'four', id:31},
              {name:'five', id:31},
              {name:'six', id:31},
              {name:'seven', id:31},
              {name:'eight', id:31},
              {name:'nine', id:31},
              {name:'ten', id:31},
              {name:'eleven', id:31},
              {name:'twelve', id:31},
            ];
          }
        })
        .state('portal.admin.widgets.detail', {
          url: '/widgets/detail-:id',
          templateUrl: '/subapps/admin/widgets/templates/detail.html',
          authenticate: true
        });
    }
  ];
});
