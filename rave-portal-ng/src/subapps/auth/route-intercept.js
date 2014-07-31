
/*
 * route-intercept
 * Intercepts our state changes and checks our authenticity rules.
 *
 */

define(function(require) {
  var auth = require('./auth');

  require('./services/security');
  require('./services/location-cache');

  return auth.run([
    '$rootScope', '$state', 'security', 'locationCache',
    function($rootScope, $state, security, locationCache) {

      // Intercept every $stateChangeStart event. This is fired by the router
      // whenever the user attempts to change the state.
      $rootScope.$on('$stateChangeStart', function(event, toState, toParams){

        // If there's nothing specified for auth, then we do nothing. By default
        // they're always allowed to proceed to the next state.
        if (toState.authenticate === undefined) {
          return;
        }

        // However, if authenticate **is** set then we need to ask security if
        // we're verified.
        security.verify()

          // If we *are* verified, *and* the state isn't allowed when you're verified,
          // then we take you home. A common state to say 'no' on is the login page.
          .then(function() {
            if (toState.authenticate === 'no') {
              $state.transitionTo('portal.home');
              event.preventDefault();
            }
          })

          // The most common case. We're not verified and we need to be. In this situation,
          // we set the page they were trying to access in a cache, then send them to the login
          // page. Later, when they login, we'll take them back to the location they intended to go
          // to.
          .catch(function() {
            if (toState.authenticate === true) {

              locationCache.put('toState', toState);
              locationCache.put('toParams', toParams);

              $state.transitionTo('portal.login');
              event.preventDefault();
            }
          });
      });
    }
  ]);
});
