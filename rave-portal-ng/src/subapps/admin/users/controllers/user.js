/*
 * user
 * Handles submission of our form
 *
 */

define(function(require) {
  var $ = require('jquery');

  return ['$scope', 'userResource', '$state', '$stateParams', 'user',
  function($scope, userResource, $state, $stateParams, user) {

    $scope.user = user;

    // The values to merge onto this scope once the promise resolves
    var keys = [
      'email',
      'openIdUrl',
      'enabled',
      'expired',
      'locked',
      'authorities'
    ];

    user.$promise.then(function(res) {
      _.extend($scope, _.pick(res, keys));

      $scope.roleUser = _.contains($scope.authorities, 'ROLE_USER');
      $scope.roleAdmin = _.contains($scope.authorities, 'ROLE_ADMIN');

      $scope.isCurrentUser = $scope.user.ID === $scope.currentUser.ID;
    }).catch(function(err) {
    });

    // Remove the user from the list of user in the scope
    this.removeFromList = function() {
      var oldUser = _.findWhere($scope.user, {ID:+$stateParams.id});
      var oldIndex = _.indexOf($scope.user, oldUser);
      $scope.users.splice(oldIndex, 1);
    };

    // Replace the old item in the list with the new
    this.updateList = function(newResource) {
      var oldUser = _.findWhere($scope.users, {ID:+$stateParams.id});
      var oldIndex = _.indexOf($scope.users, oldUser);
      $scope.users[oldIndex] = newResource;
      $scope.user = newResource;
    };

    var ctrl = this;

    var getAuthorities = function() {
      var authorities = [];
      if ($scope.roleUser) {
        authorities.push('ROLE_USER');
      }
      if ($scope.roleAdmin) {
        authorities.push('ROLE_ADMIN');
      }
      return authorities;
    };

    $scope.onSave = function() {
      var data = _.pick($scope, keys);
      data.authorities = getAuthorities();
      data.id = $stateParams.id;
      var savedResource = userResource.update(data);
      
      savedResource.$promise
        .then(ctrl.updateList)
        .catch(function(err) {
        });
    };

    $scope.onDelete = function() {
      var deletedResource = userResource.delete({
        id: $stateParams.id
      });

      deletedResource.$promise
        .then(function() {
          ctrl.removeFromList();
          $('#confirm-modal').modal('hide');
          $state.transitionTo('portal.admin.users', {page:1});
        })
        .catch(function() {
        });
    };
  }];
});
