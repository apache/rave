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

                    scope.$watch(function(){
                        return regionWidget.userPrefs;
                    }, doRender);

                    function doRender() {
                        regionWidget.render(el[0], opts);
                    }
                }
            }

            return directive;
        }
    ])
    //TODO: these are hacky - think of a better more declarative approach for register
    .directive('raveModal', [
        function(){
            var directive = {
                limit: 'A',
                replace: true,
                transclude: true,
                template: '<div ng-transclude></div>',
                link: function(scope, el, attrs) {
                    var modal = el.find('.popup');
                    rave.registerView('modal_dialog', {
                        render: function(){
                            var cfg = {
                                keyboard: false,
                                backdrop: 'static',
                                show: true
                            };
                            modal.modal(cfg);
                        },
                        getWidgetSite: function(){
                            return el.find('.site')[0];
                        },
                        destroy: function(){
                            modal.modal('hide');
                        }
                    });

                }
            }
            return directive;
        }
    ])
    .directive('raveDialog', [
        function(){
            var directive = {
                limit: 'A',
                replace: true,
                transclude: true,
                template: '<div ng-transclude></div>',
                link: function(scope, el, attrs) {
                    var modal = el.find('.popup');
                    rave.registerView('dialog', {
                        render: function(){
                            modal.modal();
                        },
                        getWidgetSite: function(){
                            return el.find('.site')[0];
                        },
                        destroy: function(){
                            modal.modal('hide');
                        }
                    });

                }
            }
            return directive;
        }
    ])
    .directive('raveSidebar', [
        function(){
            var directive = {
                limit: 'A',
                replace: true,
                transclude: true,
                template: '<div ng-transclude></div>',
                link: function(scope, el, attrs) {
                    var popup = el.find('.popup');

                    rave.registerView('sidebar', {
                        render: function(){
                            var self =this;
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
                        getWidgetSite: function(){
                            return popup.find('.site')[0];
                        },
                        destroy: function(){
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
