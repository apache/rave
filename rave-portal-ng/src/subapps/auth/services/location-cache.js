define(function(require) {
  var auth = require('../auth');

  auth.factory('locationCache', [
    '$cacheFactory',
    function($cacheFactory) {
      return $cacheFactory('location-cache');
    }
  ]);
});
