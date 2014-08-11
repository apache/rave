/*
 * forgotPasswordController
 * Sends off a completed form to our forgotPasswordApi
 *
 */

define(function(require) {
  return ['$scope', 'forgotPasswordApi',
    function($scope, forgotPasswordApi) {

      // We call this method when they submit the form.
      $scope.submit = function(email) {

        forgotPasswordApi.forgotPassword(email)

          .then(function(res) {
            $scope.invalid = false;
            $scope.success = true;
            $scope.email = email;
          })

          .catch(function(res) {
            $scope.invalid = true;
          });
      };
    }
  ];
});
