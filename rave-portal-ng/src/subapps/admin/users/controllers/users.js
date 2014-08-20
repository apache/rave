/*
 * usersController
 * This controller sets up our initial data.
 *
 */

define(function(require) {
  return ['$scope', '$stateParams', 'pagination', 'usersList', 'usersResource', 'usersMessages',
  function($scope, $stateParams, pagination, usersList, usersResource, usersMessages) {
    $scope.currentPage = +$stateParams.page || 0;

    usersList.$promise.then(function() {
      $scope.users = usersList.data;

      var usersMeta = usersList.metadata;

      // Coerce each piece of metadata to a number.
      _.each(usersMeta, function(val, key) {
        usersMeta[key] = +val;
      });

      $scope.usersMeta = usersMeta;

      $scope.showMessage = usersMessages.showMessage;
      $scope.messageHtml = usersMessages.messageHtml;
      $scope.messageClassName = usersMessages.messageClassName;
    });
  }];
});
