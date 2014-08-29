/*
 * createCtrl
 * Handles submission of our form
 *
 */

define(function(require) {
  return ['$scope', 'categoriesResource', 'categoriesMessages',
  function($scope, categoriesResource, categoriesMessages) {
    $scope.onCreate = function() {
      var newResource = categoriesResource.save({
        text: $scope.newText
      });

      newResource.$promise
        .then(function() {
          categoriesMessages.createMessage($scope.newText);
          $scope.newText = '';
          $scope.categories.push(newResource);
        })
        .catch(function(err) {
          categoriesMessages.errorMessage(err.data.message);
        });
    };
  }];
});
