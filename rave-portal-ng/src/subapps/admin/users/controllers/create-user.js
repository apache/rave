/*
 * createAccountController
 * Passes our form off to the createAccountApi when the form is submitted
 *
 */

define(function(require) {
  return ['$scope', 'createAccountApi', 'usersMessages', '$state',
    function($scope, createAccountApi, usersMessages, $state) {

      // We call this method when they submit the form.
      $scope.submit = function(accountInfo) {

        createAccountApi.createAccount(accountInfo)

          .then(function(res) {
            usersMessages.createMessage(accountInfo.username);
            $state.transitionTo('portal.admin.users');
          })

          .catch(function(res) {
            $scope.error = res.data.message;
          });
      };
    }
  ];
});
