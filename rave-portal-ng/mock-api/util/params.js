/*
 * params
 * A function to generate the query parameters from a URL
 *
 */

define(function(require) {

  // Pass in the URL; get back your parameters.
  return function(str) {
    str = str.split('?')[1];
    if(typeof str !== 'string' || str.length === 0) {
      return {};
    }
    var s = str.split('&');
    var sLength = s.length;
    var bit, query = {}, first, second;
    for (var i = 0; i < sLength; i++) {
      bit = s[i].split('=');
      first = decodeURIComponent(bit[0]);
      if(first.length === 0) {
        continue;
      }
      second = decodeURIComponent(bit[1]);
      if(typeof query[first] === 'undefined') {
        query[first] = second;
      }
      else if(query[first] instanceof Array) {
        query[first].push(second);
      }
      else {
        query[first] = [query[first], second];
      }
    }
    return query;
  };
});
