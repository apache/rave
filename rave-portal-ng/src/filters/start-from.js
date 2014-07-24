define(function(require) {
  require('rave');

  rave.filter('startFrom', function() {
    return function(input, start) {
      return input.slice(+start);
    };
  });
});


