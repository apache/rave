/*
 * categoriesUtil
 * Convenience methods for working with the categories data
 * stored in the database
 *
 */

define(function(require) {
  var api = require('../../core');
  var categoryUtil = require('../category/category-util');
  var moment = require('moment');

  var categoriesUtil = {

    // Retrieve all of the categories
    getAllCategories: function() {
      return api.db.query('categories');
    },

    // Create a new category in the database
    createCategory: function(data, user) {

      // Build the data for this category
      var newData = {
        text: data.text,
        createdUserId: user.ID,
        createdUserName: user.username,
        createdDate: moment().format(),
        lastModifiedUserId: user.ID,
        lastModifiedUserName: user.username,
        lastModifiedDate: moment().format()
      };

      // Insert and commit
      api.db.insert('categories', newData);
      api.db.commit();

      // Return the newly-created category
      return categoryUtil.getCategory({text: data.text});
    }
  };

  return categoriesUtil;
});
