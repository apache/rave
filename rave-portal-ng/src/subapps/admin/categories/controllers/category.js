define(function(require) {
  return ['$scope', 'categoryResource', '$state', '$stateParams',
  function($scope, categoryResource, $state, $stateParams) {
    $scope.category = categoryResource.get({id: $stateParams.id});

    $scope.category.$promise.then(function(res) {
      $scope.text = res.text;
    }).catch(function(err) {
    });

    $scope.onSave = function() {
      var savedResource = categoryResource.update({
        id: $stateParams.id,
        text: $scope.text
      });
      
      savedResource.$promise
        .then(function() {
          $scope.category = savedResource;
        })
        .catch(function() {
        });
    };

    $scope.onDelete = function() {
      var deletedResource = categoryResource.delete({
        id: $stateParams.id
      });

      deletedResource.$promise
        .then(function() {
          $state.transitionTo('portal.admin.categories');
        })
        .catch(function() {
        });
    };
  }];
});
