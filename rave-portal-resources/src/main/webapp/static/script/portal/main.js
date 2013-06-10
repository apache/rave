require.config({
    baseUrl:'/portal/static/script',
    paths: {
        angular: 'http://code.angularjs.org/1.1.5/angular',
        angularBootstrap: '/portal/static/script/_common/ui-bootstrap-0.3.0',
        angularResource: 'http://code.angularjs.org/1.1.5/angular-resource',
        angularUi:'//cdnjs.cloudflare.com/ajax/libs/angular-ui/0.4.0/angular-ui.min',
        app:'_common/app',
        rave: 'core/rave_core',
        bootstrap: '//cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/2.0.3/bootstrap.min',
        domReady: 'https://raw.github.com/requirejs/domReady/latest/domReady',
        jquery: 'http://code.jquery.com/jquery-1.9.1.min',
        jqueryUi: 'http://code.jquery.com/ui/1.10.2/jquery-ui',
        opensocial: '/gadgets/js/container:pubsub-2:open-views.js?c=1&container=default&debug=1',
        underscore: '//cdnjs.cloudflare.com/ajax/libs/underscore.js/1.4.4/underscore-min'
    }, shim: {
        angular: {
            deps: ['jquery'],
            exports: 'angular'
        },
        angularBootstrap: {
            deps: ['angular']
        },
        angularResource: {
            deps: ['angular']
        },
        angularUi: {
            deps: ['angular']
        },
        bootstrap: {
            deps: ['jquery']
        },
        jquery: {
            exports: 'jQuery'
        },
        jqueryUi: {
            deps: ['jquery']
        },
        underscore: {
            exports: '_'
        }
    }
});

require(['angular', 'app', 'portal/services', 'portal/routing', 'portal/directives', 'portal/controllers'], function(angular, app){
    angular.bootstrap(document, [app.name]);
});