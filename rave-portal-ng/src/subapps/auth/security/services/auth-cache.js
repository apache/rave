
/*
 * authCache
 * The auth cache stores a single value: whether our authenticity has been approved by the server
 * for this session.
 *
 * This is important because the user must be authenticated before they can take any secure actions.
 * This includes navigating to secure sections of the webapp, in addition to taking secure actions
 * like deleting a resource. It goes without saying that the server verifies these actions, too, but
 * we don't always want to check with our server. That'd be a waste of bandwidth and a slower user
 * experience. Instead, we check with the server a single time to ensure that our token (a cookie)
 * is valid. If it is, we assume it is **on the client** for the rest of the user's session.
 * This way, the user gets to navigate around the app as if they were authenticated, without
 * requiring server approval. But, of course, whenever they take an action (requesting or posting secure
 * information) the server will once again be called in to verify their authenticity.
 *
 * There is NEVER a need to use this service directly. Instead, all external auth-related
 * activities should go through the securityService.
 *
 *
 */

define(function(require) {
  return ['$cacheFactory',
    function($cacheFactory) {
      return $cacheFactory('auth-cache');
    }
  ];
});
