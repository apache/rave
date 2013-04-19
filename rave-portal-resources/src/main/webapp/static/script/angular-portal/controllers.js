angular.module('rave.controller', [])
    .controller('PortalController', ['$scope', 'rave', 'Pages', '$routeParams', '$q', function ($scope, rave, Pages, $routeParams, $q) {
        $scope.user;
        $scope.portalPrefs;
        $scope.pages = Pages.get('portal', '@self');
        $scope.selectedPageId = $routeParams.tabId;

        if (_.isUndefined($scope.selectedPageId)) {
            $scope.pages.then(function (pages) {
                $scope.selectedPageId = pages[0] && pages[0].id;
            });
        }
        $scope.pages.then(registerWidgetsForPages);

        $scope.$on('$routeChangeSuccess', function (oldRoute, newRoute) {
            $scope.selectedPageId = newRoute.params.tabId;
        });

        function registerWidgetsForPages(pages) {
            rave.init();
            var widgets = _.chain(pages).pluck('regions').flatten().pluck('regionWidgets').flatten().value();
            var i = 0;
            _.each(widgets, function (widget) {
                widget.metadata = JSON.parse(widget.metadata);
                rave.registerWidget(widget);
            });
        }
    }])
    .controller('WidgetController', ['$scope', 'rave',
        function ($scope, rave) {
            $scope.regionWidget;
            $scope.showPrefs = false;

            $scope.init = function (regionWidgetId) {
                $scope.regionWidget = rave.getWidget(regionWidgetId);
            }

            $scope.toggleCollapse = function () {
                if ($scope.regionWidget.collapsed) {
                    $scope.regionWidget.show();
                } else {
                    $scope.regionWidget.hide();
                }
            }
        }
    ]);