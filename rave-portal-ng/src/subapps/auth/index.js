define(function(require) {
  require('./auth');

  require('./controllers/login-form');
  require('./services/security');
  require('./services/auth-token');
  require('./services/location-cache');
  require('./services/auth-cache');
  require('./services/auth-api');
  require('./services/api-routes');
  
  require('./route-intercept');
  require('./routes');
  require('./create-account/routes');
  require('./forgot-password/routes');
  require('./forgot-username/routes');
});
