/*
 * category-endpoint
 * The endpoint for /category
 *
 */

define(function(require) {
  var Endpoint = require('../../util/endpoint');
  var categoryUtil = require('../category/category-util');
  var ErrorResponse = require('../../util/error-response');
  var _ = require('underscore');

  var categoryEndpoint = new Endpoint({

    url: '/category/:id',

    // Get a single category by ID
    get: function(url, data, headers, params, currentUser) {

      // Attempt to parse the category ID
      var categoryID = categoryUtil.idFromEndpoint(url);

      // Return an error if the ID isn't valid
      if (!_.isNumber(categoryID) || _.isNaN(categoryID)) {
        return new ErrorResponse(400, 'Invalid category ID');
      }

      // Attempt to retrieve a category with that ID
      var category = categoryUtil.getCategory({ID: categoryID});

      // If it doesn't exist, then we throw an error
      if (!category) {
        return new ErrorResponse(404, 'Category does not exist.');
      }

      // Otherwise, we return the retrieved category
      return [200, category];
    },

    // Update a category
    put: function(url, data, headers, params, currentUser) {

      // attempt to parse the category ID
      var categoryID = categoryUtil.idFromEndpoint(url);

      if (!_.isNumber(categoryID) || _.isNaN(categoryID)) {
        return new ErrorResponse(400, 'Invalid category ID');
      }

      var text = data.text;

      // Error out if we don't have any text
      if (!text) {
        return new ErrorResponse(400, 'Missing text field.');
      }

      // Error out if the text isn't a string
      else if (!_.isString(text)) {
        return new ErrorResponse(422, 'Text must be a string.');
      }

      // Also error out if it doesn't exist
      else if (!categoryUtil.categoryExists({ID: categoryID})) {
        return new ErrorResponse(409, 'The category does not exist.');
      }
      
      // Update our category
      categoryUtil.updateCategory(currentUser.ID, categoryID, text);

      // Return the updated category
      return [200, categoryUtil.getCategory({ID: categoryID})];
    },

    delete: function(url, data, headers, params, currentUser) {

      // attempt to parse the category ID
      var categoryID = categoryUtil.idFromEndpoint(url);

      if (!_.isNumber(categoryID) || _.isNaN(categoryID)) {
        return new ErrorResponse(400, 'Invalid category ID');
      }

      // Also error out if it doesn't exist
      else if (!categoryUtil.categoryExists({ID: categoryID})) {
        return new ErrorResponse(409, 'The category does not exist.');
      }

      // Otherwise, we delete the category and send back a success response
      categoryUtil.deleteCategory({ID: categoryID});

      return [200, null];
    }
  });

  return categoryEndpoint;
});
