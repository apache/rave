(function() {
  var profile = angular.module('profile', []);

  profile.directive('myDir', function() {
    return {
      restrict: 'E',
      template: 'Directive loaded'
    };
  });
})();
