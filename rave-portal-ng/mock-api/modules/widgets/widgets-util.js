/*
 * widgetsUtil
 * Methods for interacting with the widgets data
 *
 */

define(function(require) {
  var api = require('../../core');
  var preferencesUtil = require('../preferences/preferences-util');

  var widgetsUtil = {

    // Get every single user from the database.
    getAll: function() {
      return api.db.query('widgets');
    },

    // Get the list of all users
    getPage: function(currentPage, filter) {

      // Get the page size from the database
      var pageSize = preferencesUtil.getPreference('pageSize');

      var allWidgets = widgetsUtil.getAll();

      // Filter them, if filter is passed
      if (filter) {
        allWidgets = _.filter(allWidgets, function(widget) {
          return widget.title.toLowerCase().indexOf(filter.toLowerCase()) > -1;
        });
      }

      // The first index to start from
      var startIndex = (currentPage - 1) * pageSize;

      // The naive end index. We may have overshot this.
      var endIndex = currentPage * pageSize;

      // Truncate our end index if it's too long. Slice only goes UP TO
      // this index, which is why we don't use (length - 1)
      if (endIndex > allWidgets.length) {
        endIndex = allWidgets.length;
      }

      var results = allWidgets.slice(startIndex, endIndex);

      var returnObj = {};

      returnObj.data = results;
      returnObj.metadata = {
        pageSize: pageSize,
        currentPage: currentPage,
        start: startIndex+1,
        end: endIndex,
        pageCount: Math.ceil(allWidgets.length / pageSize),
        totalUsers: allWidgets.length
      };

      return returnObj;
    }
  };

  return widgetsUtil;
});
