/*
 * pagination
 * ----------
 * Gives you an array of the values that represent the
 * values to display in a pagination menu.
 * To use it, pass your currentPage and the pageCount.
 * 
 *
 */

define(function(require) {
  var ng = require('angular');

  var pagination = ng.module('pagination', []);

  pagination.factory('pagination', function() {
    return {
      paginationPages: function(currentPage, pageCount) {
        // The total number of page links. It must be an odd number.
        var rangeSize = 11;

        // An array that represents each page in the page links.
        var pages = [];

        // The page numbers our pagination starts and ends at
        var start, end;

        // The earliest possible page we could navigate to
        var earliestPage = 1;

        // The maximum possible page we could navigate to
        var maxPage = pageCount;

        // The maximum number of page links that could appear
        // to the right and left of the current page
        var maxLeft = (rangeSize - 1) / 2;
        var maxRight = (rangeSize - 1) / 2;

        // Naive start and ends that don't take into account
        // our limitations.
        var potentialEnd = currentPage + maxRight;
        var potentialStart = currentPage - maxLeft;

        // How far our naive bounds overshot our limitations.
        // Positive values here mean that we've overshot it.
        var leftOvershoot = earliestPage - potentialStart;
        var rightOvershoot = potentialEnd - maxPage;

        // Booleans representing whether we've overshot or not.
        var overshotLeft = leftOvershoot >= 0;
        var overshotRight = rightOvershoot >= 0;

        // If we're not bound on either side, then it's easy. Our start
        // and end are just the maximum we can go in that direction
        if (!overshotLeft && !overshotRight) {
          start = potentialStart;
          end = potentialEnd;
        }

        // The other easy case. If both have overshot, then our range is just
        // the min and max page.
        else if (overshotLeft && overshotRight) {
          start = earliestPage;
          end = maxPage;
        }

        // If we're bound to the right, then our end is just the maximum page.
        // We then calculate our start, adding on the extra items.
        else if (!overshotLeft && overshotRight) {
          end = maxPage;
          start = currentPage - maxLeft - rightOvershoot;
        }

        // In the same way, if we overshot the left,
        // then we set our earliest page and calculate our end.
        else if (overshotLeft && !overshotRight) {
          start = earliestPage;
          end = currentPage + maxRight + leftOvershoot;
        }

        // Loop from start to end, adding more pages.
        // We ignore values that are too large or small.
        for (start; start <= end; start++) {
          if (start >= earliestPage && start <= maxPage) {
            pages.push(start);
          }
        }
        return pages;
      }
    };
  });

  return pagination;
});
