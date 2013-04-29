angular.module('rave.directive', [])
    .directive('renderWidget', ['rave', '$parse',
        function (rave, $parse) {
            return function postLink(scope, el, attrs) {
                var fn = $parse(attrs.renderWidget);
                var renderWidgetId = fn(scope);
                if (!_.isUndefined(renderWidgetId)) {
                    rave.getWidget(renderWidgetId).render(el[0]);
                }
            }
        }
    ])
    .directive('sortable', [
        function(){
            return {
                link: function(scope, element, attrs, ngModel) {
                    var opts = scope.$eval(attrs.sortable);

                    element.sortable(opts);
                }
            }
        }
    ]);