/*
 * locationCache
 * Creates a cache for our location
 * 
 */

define(function(require) {
  return ['$cacheFactory',
    function($cacheFactory) {
      return $cacheFactory('location-cache');
    }
  ];
});
