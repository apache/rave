require.config({
    baseUrl:'src/',
    paths:{
        "osapi": "/src/test/dependencies/osapi",
        "jquery": "/src/test/dependencies/jquery",
        "underscore": "/src/test/dependencies/underscore"
    },
    shim: {
        underscore:{
            exports: '_'
        },
        jquery:{
            exports: '$'
        }
    }
});
