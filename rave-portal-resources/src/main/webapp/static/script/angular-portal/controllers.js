angular.module('rave.controller', ['ui.bootstrap'])
    .config(['$dialogProvider',
        function ($dialogProvider) {
            $dialogProvider.options({
                backdrop: true,
                keyboard: true,
                backdropClick: true
            });
        }
    ])
    .controller('PortalController', ['$scope', 'rave', 'Pages', '$routeParams', '$q', '$dialog',
        function ($scope, rave, Pages, $routeParams, $q, $dialog) {
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

            //TODO: this does not fire on first load?
            $scope.$on('$routeChangeSuccess', function (oldRoute, newRoute) {
                if (_.isUndefined(newRoute.params.tabId)) {
                    $scope.pages.then(function (pages) {
                        $scope.selectedPageId = pages[0] && pages[0].id;
                    });
                } else {
                    $scope.selectedPageId = newRoute.params.tabId;
                }
            });

            function registerWidgetsForPages(pages) {
                //TODO
                rave.init();
                var widgets = _.chain(pages).pluck('regions').flatten().pluck('regionWidgets').flatten().value();
                var i = 0;
                _.each(widgets, function (widget) {
                    widget.metadata = JSON.parse(widget.metadata);
                    rave.registerWidget(widget);
                });
            }

            $scope.addPage = function () {
                $dialog.dialog({
                    templateUrl: 'editPageModal',
                    controller: 'EditPageController',
                    resolve: {
                        page: function () {
                            return {};
                        }
                    }
                }).open();
            }
        }
    ])
    .controller('CurrentPageController', ['$scope', 'Pages', '$dialog', '$location',
        function ($scope, Pages, $dialog, $location) {


            $scope.edit = function () {
                $dialog.dialog({
                    templateUrl: 'editPageModal',
                    controller: 'EditPageController',
                    resolve: {
                        page: function () {
                            return angular.copy($scope.page);
                        }
                    }
                }).open();
            }
            $scope.move = function () {

            }
            $scope.delete = function () {
                Pages.delete($scope.page.id).then(function () {
                    $location.path('/');
                });
            }
            $scope.export = function () {

            }
            $scope.share = function () {

            }
            $scope.unshare = function () {

            }
        }
    ])
    .controller('WidgetController', ['$scope', 'rave',
        function ($scope, rave) {
            //Grab a handle on rave's registered regionWidget object to get that functionality in scope.
            $scope.regionWidget = rave.getWidget($scope.regionWidget.id);
            $scope.showPrefs = false;

            $scope.toggleCollapse = function () {
                if ($scope.regionWidget.collapsed) {
                    $scope.regionWidget.show();
                } else {
                    $scope.regionWidget.hide();
                }
            }
        }
    ])
    .controller('EditPageController', ['$scope', 'Pages', 'dialog', 'page',
        function ($scope, Pages, dialog, page) {
            //TODO: layouts needs to come from a service
            $scope.layouts = [
                {label: 'One Column',
                    value: 'columns_1' },
                {label: 'Three Columns (new user)',
                    value: 'columns_3_newuser' },
                {label: 'Two Columns',
                    value: 'columns_2' },
                {label: 'Two Columns (wide/narrow)',
                    value: 'columns_2wn' },
                {label: 'Three Columns',
                    value: 'columns_3' },
                {label: 'Three Columns (narrow/wide/narrow)',
                    value: 'columns_3nwn' },
                {label: ' Four Columns',
                    value: 'columns_4' },
                {label: 'Four Columns (narrow/wide/narrow/bottom)',
                    value: 'columns_3nwn_1_bottom' }
            ];
            //$scope.page = page;
            $scope.errorMsg = '';
            $scope.page = _.defaults(page, {
                pageLayoutCode: $scope.layouts[0].value
            });

            $scope.close = function () {
                dialog.close();
            }

            if ($scope.page.id) {
                $scope.savePage = function () {
                    Pages.edit($scope.page.id, $scope.page.name, $scope.page.pageLayoutCode).then(function (result) {
                        var newPage = result.result;
                        dialog.close(newPage);
                    }, function () {
                        $scope.errorMsg = 'A page with that name already exists';
                    });
                }
            }
            else {
                $scope.savePage = function () {
                    Pages.add($scope.page.name, $scope.page.pageLayoutCode).then(function (result) {
                        dialog.close();
                    }, function () {
                        $scope.errorMsg = 'A page with that name already exists';
                    });
                }
            }

        }
    ]);