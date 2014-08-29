define(function(require) {
  require('./filters');
  
  var angular = require('angular');

  angular.module('filters').filter('startFrom', function() {
    return function(input, start) {
      return input.slice(+start);
    };
  });
});


