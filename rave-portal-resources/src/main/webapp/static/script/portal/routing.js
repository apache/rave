define(['app', 'core/rave_core'], function (app, rave) {

    app.config([function () {
            rave.RegionWidget.defaultView = 'home';
            rave.api.setContext('/portal/app/');
        }]).
        config(['$routeProvider', '$locationProvider', '$httpProvider', function ($routeProvider, $locationProvider, $httpProvider) {

            //The routes that our angular app will handle
            $routeProvider
                .when('/', {
                    controller: 'PortalController',
                    resolve: {
                        pages: ['PagesLoader', function (PagesLoader) {
                            return PagesLoader();
                        }]
                    },
                    templateUrl: "/portal/static/html/portal/tabs.html"
                })
                .when('/:tabId', {
                    controller: 'PortalController',
                    resolve: {
                        pages: ['PagesLoader', function (PagesLoader) {
                            return PagesLoader();
                        }]
                    },
                    templateUrl: "/portal/static/html/portal/tabs.html"
                })
                .otherwise({ templateUrl: '/portal/static/html/portal/404.html'});


            //gets rid of the # in urls
            $locationProvider.html5Mode(true);

            /*
             Set up an interceptor to watch for 401 errors.
             The server, rather than redirect to a login page (or whatever), just returns  a 401 error
             if it receives a request that should have a user session going.  Angular catches the error below
             and says what happens - in this case, we just redirect to a login page.  You can get a little more
             complex with this strategy, such as queueing up failed requests and re-trying them once the user logs in.
             Read all about it here: http://www.espeo.pl/2012/02/26/authentication-in-angularjs-application
             */
            var interceptor = ['$q', '$location', '$rootScope', function ($q, $location, $rootScope) {
                function success(response) {
                    return response;
                }

                function error(response) {
                    var status = response.status;
                    if (status == 401) {
                        $rootScope.redirect = $location.url(); // save the current url so we can redirect the user back
                        $rootScope.user = {}
                        $location.path('/login');
                    }
                    return $q.reject(response);
                }

                return function (promise) {
                    return promise.then(success, error);
                }
            }];
            $httpProvider.responseInterceptors.push(interceptor);
        }])
        .run(['$route', '$rootScope', function ($route, $rootScope) {
//            $rootScope.$on('$routeChangeSuccess', function (oldRoute, newRoute) {
//                $rootScope.templateUrl = newRoute.templateUrl;
//            });
        }]);

})