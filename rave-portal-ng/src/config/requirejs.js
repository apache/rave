require.config({
  baseUrl: '../',

  // map bower components to nice paths
  paths: {
    rave: 'rave',
    jquery: 'jquery/dist/jquery',
    angular: 'angular/angular',
    angularMocks: 'angular-mocks/angular-mocks',
    uiRouter: 'angular-ui-router/release/angular-ui-router',
    bootstrap: '//cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/2.3.2/js/bootstrap.min',
    localStorageDB: 'localStorageDB/localstoragedb',
    angularResource: 'angular-resource/angular-resource',
    angularCookie: 'angular-cookie/angular-cookie',
    moment: 'moment'
  },

  // load non-amd dependencies
  shim: {
    jquery: {
      exports: '$'
    },
    angular: {
      exports: 'angular'
    },
    bootstrap: {
      deps: ['jquery']
    },
    uiRouter: {
      deps: ['angular']
    },
    angularMocks: {
      deps: ['angular']
    },
    angularCookie: {
      deps: ['angular']
    },
    angularResource: {
      deps: ['angular']
    },
    localStorageDB: {
      exports: 'localStorageDB'
    }
  }
});
