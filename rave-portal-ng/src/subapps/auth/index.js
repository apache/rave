define(function(require) {
  require('./auth');

  require('./directives/password-check');

  require('./controllers/login-form');

  require('./services/security');
  require('./services/auth-token');
  require('./services/location-cache');
  require('./services/auth-cache');
  require('./services/auth-api');
  require('./services/auth-api-base');
  require('./services/auth-api-routes');

  require('./forgot-password/providers/forgot-password-api-routes');
  require('./forgot-password/providers/forgot-password-api');
  require('./forgot-password/controllers/forgot-password');

  require('./forgot-username/providers/forgot-username-api-routes');
  require('./forgot-username/providers/forgot-username-api');
  require('./forgot-username/controllers/forgot-username');

  require('./create-account/providers/create-account-api-routes');
  require('./create-account/providers/create-account-api');
  require('./create-account/controllers/create-account');
  
  require('./route-intercept');
  require('./routes');
  require('./create-account/routes');
  require('./forgot-password/routes');
  require('./forgot-username/routes');
});
