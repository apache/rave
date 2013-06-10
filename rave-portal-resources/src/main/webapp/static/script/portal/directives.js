define(['app', 'core/rave_core', 'bootstrap', 'jqueryUi', 'angularUi'], function (app, rave) {

    app.directive('raveViewSurface', ['$compile',
            function ($compile) {
                var directive = {
                    restrict: 'A',
                    link: function (scope, el, attrs) {
                        var regionWidget = scope.$eval(attrs.raveViewSurface);

                        scope.$watch(function () {
                            return regionWidget._surface;
                        }, doRender)

                        function doRender() {
                            var template = rave.renderView(regionWidget._surface);

                            if (template) {
                                el.html(template);
                                $compile(el.contents())(scope);
                            }
                        }

                    }
                }

                return directive;
            }
        ])
        .directive('raveRenderWidget', [ '$compile', '$rootScope',
            function ($compile, $rootScope) {
                var directive = {
                    restrict: 'A',
                    link: function postLink(scope, el, attrs) {

                        var regionWidget = scope.$eval(attrs.raveRenderWidget);

                        regionWidget.render(el[0]);

                        regionWidget.on('navigate', function () {
                            //conditionally apply if not already in a digest cycle
                            if ($rootScope.$$phase != '$apply') {
                                scope.$apply();
                            }
                        })
                    }
                }

                return directive;
            }
        ])
        .directive('raveRegisterView', ['$compile',
            function ($compile) {
                var directive = {
                    restrict: 'AE',
                    scope: {},
                    terminal: true,
                    priority: -1,
                    controller: ['$scope', '$element',
                        function ($scope, $element) {
                            this.customRender = function () {
                            };
                            this.customDestroy = function () {
                            };
                        }
                    ],
                    link: function (scope, el, attrs, controller) {

                        var viewName = attrs.raveRegisterView;

                        var template = el.html();
                        el.remove();

                        rave.registerView(viewName, {
                            render: function (widget) {
                                return template;
                            }
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
                        controller.customRender = function (el) {
                            var opts = attrs.opts || {};

                            el.modal(_.extend(opts, {show: true}));
                        }

                        controller.customDestroy = function (el) {
                            el.modal('hide');
                        };
                    }
                }

                return directive;
            }
        ]);
})
