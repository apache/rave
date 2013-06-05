angular.module('rave.portal', ['rave.controller', 'rave.directive', 'rave.service', 'rave.routing'])
    .config(function(){
        rave.RegionWidget.defaultView = 'home';
    })

//TODO: drop it!
rave.api.setContext('/portal/app/');
