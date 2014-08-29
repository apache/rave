/*
 * error
 * A convenience class to generate a JSON error.
 *
 */

define(function(require) {
  var ErrorResponse = function(code, msg) {
    return [code, {
      message: msg
    }];
  };

  return ErrorResponse;
});
