require.config({
  baseUrl: '../',

  // map bower components to nice paths
  paths: {
    // bower components
    rave: 'rave',
    jquery: 'jquery/dist/jquery',
    underscore: 'lodash/dist/lodash',
    angular: 'angular/angular',
    uiRouter: 'angular-ui-router/release/angular-ui-router',
    bootstrap: '//cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/2.3.2/js/bootstrap.min'
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
    }
  }
});
