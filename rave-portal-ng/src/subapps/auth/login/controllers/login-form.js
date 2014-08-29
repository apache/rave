/*
 * loginForm
 * Processes our login form by delegating to the security module
 *
 */


define(function(require) {
  return ['$scope', 'security',
    function($scope, security) {

      // We call this method when they submit the form.
      $scope.submit = function(credentials) {

        // Otherwise, we attempt to log them in by delegating to the
        // securityService. If it's successful, the security service
        // handles the rest for us.
        security.login(credentials)

          // And if it errors, we catch it here. 
          .catch(function() {
            $scope.invalid = true;
          });
      };
    }
  ];
});
