define(function(require) {
  require('../admin');
  var angular = require('angular');

  angular.module('admin').config([
    '$stateProvider', '$urlRouterProvider',
    function($stateProvider, $urlRouterProvider) {
      $stateProvider
        .state('portal.admin.users', {
          url: '/app/admin/users',
          templateUrl: '/modules/admin/users/users.html',
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
              return Math.ceil( $scope.users.length / $scope.listSize );                
            };
            $scope.users = [
              {name:'Jmeas', id:28},
              {name:'Carl', id:29},
              {name:'M.Franklin', id:30},
              {name:'Beth', id:31},
              {name:'Greg', id:31},
              {name:'Boaz', id:31},
              {name:'Tkellen', id:31},
              {name:'Obama', id:31},
              {name:'Santa', id:31},
              {name:'The Captain', id:31}
            ];
          }
        })
        .state('portal.admin.users.detail', {
          url: '/app/admin/users/detail-:id',
          templateUrl: '/modules/admin/users/detail/detail.html',
          authenticate: true
        });
    }
  ]);
});
