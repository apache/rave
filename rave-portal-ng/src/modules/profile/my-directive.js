define(function(require) {
  var angular = require('angular');
  
  return angular.module('profile').directive('myDir', function() {
    return {
      restrict: 'E',
      template: 'Directive loaded!'
    };
  });
});
