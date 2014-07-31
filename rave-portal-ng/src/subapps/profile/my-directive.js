define(function(require) {
  require('./profile');
  
  var angular = require('angular');
  
  return angular.module('profile').directive('myDir', function() {
    return {
      restrict: 'E',
      template: 'Directive loaded!'
    };
  });
});
