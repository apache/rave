define(function(require) {
  var angular = require ('angular');
  var profile = angular.module('profile', []);

  return profile.directive('myDir', function() {
    return {
      restrict: 'E',
      template: 'Directive loaded'
    };
  });
});
