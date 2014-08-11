/*
 * pwCheck
 * A little directive for custom error handling on the account creation form.
 *
 */

define(function(require) {
  var $ = require('jquery');
  
  return function () {
    return {
      require: 'ngModel',
      link: function (scope, elem, attrs, ctrl) {
        var firstPasswordEl = $('#' + attrs.pwCheck);
        elem = $(elem);
        var els = elem.add(firstPasswordEl);
        els.on('keyup', function () {
          scope.$apply(function () {
            var validity;
            if (!firstPasswordEl.val() || !elem.val()) {
              validity = true;
            } else {
              validity = elem.val() === firstPasswordEl.val();
            }
            ctrl.$setValidity('pwmatch', validity);
          });
        });
      }
    };
  };
});
