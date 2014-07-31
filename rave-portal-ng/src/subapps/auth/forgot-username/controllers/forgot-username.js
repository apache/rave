define(function(require) {
  var auth = require('../../auth');
  require('../providers/forgot-username-api');

  auth.controller('forgotUsernameController', [
    '$scope', 'forgotUsernameApi',
    function($scope, forgotUsernameApi) {

      // We call this method when they submit the form.
      $scope.submit = function(email) {

        // Do nothing if they haven't included an email.
        if (!email) {
          return;
        }

        forgotUsernameApi.forgotUsername(email)

          // On success, we set invalid to be false, which
          // hides any errors. Then we set success to true,
          // which reveals our success message. Lastly, we
          // persist our inputted email to the scope for the
          // purposes of displaying it.
          .then(function(res) {
            $scope.invalid = false;
            $scope.success = true;
            $scope.email = email;
          })

          // If the server errors, then we set invalid to true,
          // which shows a message.
          .catch(function(res) {
            $scope.invalid = true;
          });
      };
    }
  ]);
});
