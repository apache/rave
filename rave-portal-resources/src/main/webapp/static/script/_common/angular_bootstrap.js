define(['angular', 'domReady'], function(angular, domReady){
    var app = angular.module('rave');

    domReady(function(){
        angular.bootstrap(document, ['rave'])
    });

    return app;
});