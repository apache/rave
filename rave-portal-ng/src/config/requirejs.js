require.config({
  baseUrl: '../',

  // map bower components to nice paths
  paths: {
    // bower components
    rave: 'rave',
    jquery: 'jquery/dist/jquery',
    underscore: 'lodash/dist/lodash',
    angular: 'angular/angular',
    angularMocks: 'angular-mocks/angular-mocks',
    uiRouter: 'angular-ui-router/release/angular-ui-router',
    bootstrap: '//cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/2.3.2/js/bootstrap.min',
    localStorageDB: 'localStorageDB/localstoragedb',
    angularCookies: 'angular-cookies/angular-cookies'
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
    angularCookies: {
      deps: ['angular']
    },
    localStorageDB: {
      exports: 'localStorageDB'
    }
  }
});
