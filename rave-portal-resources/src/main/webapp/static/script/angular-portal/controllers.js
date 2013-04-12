angular.module('rave.controller', [])
    .controller('tabsController', ['$scope', 'rave', 'pages', '$routeParams', function ($scope, rave, pages, $routeParams) {
        $scope.pages;
        $scope.currentPage;

        $scope.$on('$routeChangeSuccess', function(oldRoute, newRoute){
            setCurrentPage(newRoute.params.tabId);
        });

        pages.then(function(pages){
            $scope.pages = pages;
            setCurrentPage($routeParams.tabId);
        });

        function setCurrentPage(pageId) {
            if(_.isUndefined(pageId)) {
                pageId = $scope.pages[0].id;
            }
            _.each($scope.pages, function(page) {
                page.template = 'otherTab'
                page.isCurrent = false;
                if(page.id == pageId) {
                    page.isCurrent = true;
                    page.template = 'currentTab'
                    $scope.currentPage = page;
                }
            })
        }
    }]);