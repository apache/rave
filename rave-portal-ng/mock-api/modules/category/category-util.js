/*
 * categoryUtil
 * Convenience methods for working with single category resources
 *
 */

define(function(require) {
  var api = require('../../core');
  var moment = require('moment');

  var categoryUtil = {

    // Pull out the ID from the endpoint
    idFromEndpoint: function(url) {
      return parseInt(url.replace('/api/v1/category/', ''), 10);
    },

    // Whether or not a category with the given text exists
    categoryExists: function(identifier) {
      return !!categoryUtil.getCategory(identifier);
    },

    // Get a category by an identifier object.
    // It can either be text or an ID.
    getCategory: function(identifier) {
      var results = api.db.query('categories', identifier);

      if (results.length === 1) {
        return results[0];
      }

      return false;
    },

    updateCategory: function(userID, categoryID, text) {
      var searchParams = {ID: categoryID};
      api.db.update('categories', searchParams, function(row) {
        row.text = text;
        row.lastModifiedUserId = userID;
        row.lastModifiedDate = moment().format();
        return row;
      });
      api.db.commit();
    },

    deleteCategory: function(identifier) {
      api.db.deleteRows('categories', identifier);
      api.db.commit();
    }
  };

  return categoryUtil;
});
