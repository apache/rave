/**
 * Created with IntelliJ IDEA.
 * User: DGORNSTEIN
 * Date: 6/11/13
 * Time: 1:08 PM
 * To change this template use File | Settings | File Templates.
 */
requirejs.config({
    shim: {
        'backbone': {
            //These script dependencies should be loaded before loading
            //backbone.js
            deps: ['underscore', 'jquery'],
            //Once loaded, use the global 'Backbone' as the
            //module value.
            exports: 'Backbone'
        },
        'underscore': {
            exports: '_'
        },
        'handlebars':{
            exports:'Handlebars'
        },
        'jqueryUi':{
            deps: ['jquery']
        },
        jqueryValidate:{
            deps:['jquery']
        },
        jqueryHashChange:{
            deps:['jquery']
        }
    },
    "paths":{
        "core": "./core",
        "portal": "./portal",
        "event_bindings": "./portal",
        "jquery": "//ajax.aspnetcdn.com/ajax/jquery/jquery-1.7.2.min",
        "underscore": "//cdnjs.cloudflare.com/ajax/libs/underscore.js/1.4.4/underscore-min",
        "handlebars":"//cdnjs.cloudflare.com/ajax/libs/handlebars.js/1.0.rc.2/handlebars.min",
        "backbone":"//cdnjs.cloudflare.com/ajax/libs/backbone.js/0.9.10/backbone-min",
        "osapi":"/gadgets/js/container:pubsub-2:open-views.js?c=1&container=default&debug=1",
        jqueryUi:"//ajax.aspnetcdn.com/ajax/jquery.ui/1.8.17/jquery-ui.min",
        "jqueryValidate":"//ajax.aspnetcdn.com/ajax/jquery.validate/1.8.1/jquery.validate.min",
        "jqueryHashChange":'//cdnjs.cloudflare.com/ajax/libs/jquery-hashchange/v1.3/jquery.ba-hashchange.min'
}
})

requirejs(["core/rave_core", "portal/portalRool"]);
