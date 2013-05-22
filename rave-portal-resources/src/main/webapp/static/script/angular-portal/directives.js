angular.module('rave.directive', [])
    .directive('raveRenderWidget', [
        function () {
            var directive = {
                restrict: 'A',
                scope: {
                    raveRenderWidget: '&',
                    raveRenderOpts: '&'
                },
                link: function postLink(scope, el, attrs) {
                    var regionWidget = scope.raveRenderWidget(),
                        opts = scope.raveRenderOpts();

                    scope.$watch(function () {
                        return regionWidget && regionWidget.userPrefs;
                    }, doRender);

                    function doRender() {
                        if (regionWidget) {
                            regionWidget.render(el[0], opts);
                        }
                    }
                }
            }

            return directive;
        }
    ])
    .directive('raveRegisterView', [ 'rave', '$compile',
        function (rave, $compile) {
            var directive = {
                restrict: 'AE',
                scope: {},
                priority: -1,
                controller: ['$scope', '$element',
                    function ($scope, $element) {
                        this.render = function (opts) {
                            $scope.show = true;
                            _.extend($scope, opts);
                            $scope.$apply();
                        }

                        this.getWidgetSite = function () {
                            return $element.find('[rave-render-widget]')[0];
                        }

                        this.destroy = function () {
                            $scope.show = false;
                            $scope.$apply();
                        }
                    }
                ],
                link: function (scope, el, attrs, controller) {

                    var viewName = attrs.raveRegisterView;

                    rave.registerView(viewName, {
                        render: controller.render,
                        getWidgetSite: controller.getWidgetSite,
                        destroy: controller.destroy
                    });

                }
            }

            return directive;
        }
    ])
    .directive('dialog', [
        function () {
            var directive = {
                restrict: 'A',
                require: 'raveRegisterView',
                link: function (scope, el, attrs, controller) {
                    el.on('hidden', function(){
                        //TODO: close gadget to clean up after ourselves, but we need a ref to the site?
                    })

                    controller.render = function () {
                        var opts = attrs.opts || {};

                        el.modal(_.extend(opts, {show: true}));
                    }

                    controller.destroy = function () {
                        el.modal('hide');
                    }
                }
            }

            return directive;
        }
    ])
    /*


     */
    .directive('raveModal', [
        function () {
            var directive = {
                limit: 'A',
                replace: true,
                transclude: true,
                template: '<div ng-transclude></div>',
                link: function (scope, el, attrs) {
                    var modal = el.find('.popup');
                    rave.registerView('modal_dialog', {
                        render: function () {
                            var cfg = {
                                keyboard: false,
                                backdrop: 'static',
                                show: true
                            };
                            modal.modal(cfg);
                        },
                        getWidgetSite: function () {
                            return el.find('.site')[0];
                        },
                        destroy: function () {
                            modal.modal('hide');
                        }
                    });

                }
            }
            return directive;
        }
    ])
    .directive('raveDialog', [
        function () {
            var directive = {
                limit: 'A',
                replace: true,
                transclude: true,
                template: '<div ng-transclude></div>',
                link: function (scope, el, attrs) {
                    var modal = el.find('.popup');
                    rave.registerView('dialog', {
                        render: function () {
                            modal.modal();
                        },
                        getWidgetSite: function () {
                            return el.find('.site')[0];
                        },
                        destroy: function () {
                            modal.modal('hide');
                        }
                    });

                }
            }
            return directive;
        }
    ])
    .directive('raveSidebar', [
        function () {
            var directive = {
                limit: 'A',
                replace: true,
                transclude: true,
                template: '<div ng-transclude></div>',
                link: function (scope, el, attrs) {
                    var popup = el.find('.popup');

                    rave.registerView('sidebar', {
                        render: function () {
                            var self = this;
                            popup.find('.close').click(function () {
                                self.destroy();
                            });
                            popup.show("slide", { direction: "right" }, 'fast');
                            $('body').addClass('modal-open');
                            $('body').append('<div class="modal-backdrop fade in"></div>');
                            // hide the main browser window's scrollbar to prevent possible "double scrollbar" rendering
                            // between it and an iframe vertical scrollbar
                            $('body').addClass('no-scroll');
                        },
                        getWidgetSite: function () {
                            return popup.find('.site')[0];
                        },
                        destroy: function () {
                            popup.hide("slide", { direction: "right" }, 'fast', function () {
                                $('body').removeClass('modal-open');
                                $('.modal-backdrop').remove();
                                // restore the main browser window's scrollbar
                                $('body').removeClass('no-scroll');
                            });
                        }
                    });

                }
            }
            return directive;
        }
    ]);
