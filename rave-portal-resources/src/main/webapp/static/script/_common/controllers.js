angular.module('rave.controller', ['ui.bootstrap', 'ui'])
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
            $scope.dragging = false;
            $scope.sortingOpts = {
                connectWith: '.region', // defines which regions are dnd-able
                scroll: true, // whether to scroll the window if the user goes outside the areas
                opacity: 0.5, // the opacity of the object being dragged
                revert: true, // smooth snap animation
                cursor: 'move', // the cursor to show while dnd
                handle: '.widget-title-bar-draggable', // the draggable handle
                tolerance: 'pointer', // change dnd drop zone on mouse-over
                start: function (e, ui) {
                    ui.placeholder.height(ui.item.height());
                    $scope.dragging = true;
                    $scope.$apply();
                },
                stop: function (e, ui) {
                    $scope.dragging = false;
                    $scope.$apply();
                }
            }

            if (_.isUndefined($scope.selectedPageId)) {
                $scope.pages.then(function (pages) {
                    $scope.selectedPageId = pages[0] && pages[0].id;
                });
            }
            $scope.pages.then(registerWidgetsForPages);
            $scope.pages.then(watchForMove)


            //TODO: this does not fire on first load - why?
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
                rave.init();
                _.each(pages, function (page) {
                    _.each(page.regions, function (region) {
                        _.each(region.regionWidgets, function (regionWidget, i) {
                            regionWidget.metadata = JSON.parse(regionWidget.metadata);
                            region.regionWidgets[i] = rave.registerWidget(regionWidget);
                        });
                    });
                })
            }

            //TODO: this might not be the best way to handle moving widgets
            function watchForMove(pages) {
                $scope.$watch(function () {
                    return _.chain(pages)
                        .pluck('regions').flatten()
                        .map(function (region, regionIndex) {
                            return _.map(region.regionWidgets, function (widget, index) {
                                return  {
                                    id: widget.id,
                                    //TODO: it's dumb that region index is 1-based
                                    regionId: regionIndex + 1,
                                    index: index
                                }
                            });
                        })
                        .flatten().value();
                }, function (newValue, oldValue) {
                    var oldRegion;

                    var movedWidget = _.find(newValue, function (widget) {
                        var isChanged = !_.any(oldValue, function (val) {
                            return _.isEqual(widget, val);
                        });

                        if (isChanged) {
                            return widget;
                        }
                    });

                    if (movedWidget) {
                        oldRegion = _.findWhere(oldValue, {id: movedWidget.id}).regionId;
                        rave.getWidget(movedWidget.id).moveToRegion(oldRegion, movedWidget.regionId, movedWidget.index);
                    }
                }, true)
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

            $scope.hasWidgets = function (page) {
                return _.chain(page.regions).pluck('regionWidgets').flatten().value().length > 0;
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
                $dialog.dialog({
                    templateUrl: 'movePageModal',
                    controller: 'MovePageController',
                    resolve: {
                        pages: function () {
                            return angular.copy($scope.pages);
                        },
                        page: function () {
                            return angular.copy($scope.page);
                        }
                    }
                }).open();
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
    .controller('WidgetController', ['$scope', 'rave', 'Pages',
        function ($scope, rave, Pages) {
            //Grab a handle on rave's registered regionWidget object to get that functionality in scope.
            $scope.showPrefs = false;
            $scope.regionWidgetPrefs = $scope.regionWidget.getPrefs();

            $scope.menu = {
                editPrefs: {
                    disable: _.isUndefined(regionWidget.getPrefs())
                },
                maximize: {
                },
                move: {
                    disable: $scope.pages.length < 2
                },
                del: {},
                about: {},
                comment: {},
                rate: {}
            }
            $scope.toggleCollapse = function () {
                if ($scope.regionWidget.collapsed) {
                    $scope.regionWidget.show();
                } else {
                    $scope.regionWidget.hide();
                }
            }
            $scope.togglePrefs = function () {
                $scope.showPrefs = !$scope.showPrefs
            }
            $scope.savePrefs = function () {
                $scope.regionWidget.setPrefs($scope.regionWidgetPrefs);
                $scope.togglePrefs();
            }
            $scope.maximize = function () {
                $scope.regionWidget.navigate({view: 'canvas'});
            }
            $scope.minimize = function () {
                $scope.regionWidget.navigate({view: 'home'});
            }
            $scope.delete = function () {
                Pages.deleteRegionWidget($scope.regionWidget);
            }
        }
    ])
    .controller('EditPageController', ['$scope', 'Pages', 'dialog', 'page', '$location',
        function ($scope, Pages, dialog, page, $location) {
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
                        $location.path(result.id);
                    }, function () {
                        $scope.errorMsg = 'A page with that name already exists';
                    });
                }
            }

        }
    ])
    //TODO: try drag&drop for moving pages?
    .controller('MovePageController', [ '$scope', 'Pages', 'dialog', 'page', 'pages',
        function ($scope, Pages, dialog, page, pages) {
            $scope.pages = pages;
            $scope.page = page;
            $scope.locations = [
                {label: '', value: ''}
            ];
            $scope.newLocation = $scope.locations[0].value;

            $scope.close = function () {
                dialog.close();
            }

            $scope.movePage = function () {
                Pages.move(page.id, newLocationId);
                dialog.close();
            }

        }
    ])
    .controller('SharePageController', ['$scope', 'Pages',
        function () {

        }
    ]);