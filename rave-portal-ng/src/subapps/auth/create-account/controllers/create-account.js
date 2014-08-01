define(function(require) {
  var auth = require('../../auth');
  require('../providers/create-account-api');

  auth.controller('createAccountController', [
    '$scope', 'createAccountApi',
    function($scope, createAccountApi) {

      // We call this method when they submit the form.
      $scope.submit = function(accountInfo) {

        createAccountApi.createAccount(accountInfo)

          .then(function(res) {
            console.log('success');
            $scope.invalid = false;
            $scope.success = true;
            $scope.username = accountInfo.username;
            $scope.email = accountInfo.email;
          })

          .catch(function(res) {
            $scope.invalid = true;
            $scope.error = res.data;
          });
      };
    }
  ]);
});
