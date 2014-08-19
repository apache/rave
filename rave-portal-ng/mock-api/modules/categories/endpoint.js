/*
 * categories-endpoint
 * The endpoint for /categories
 *
 */

define(function(require) {
  var Endpoint = require('../../util/endpoint');
  var ErrorResponse = require('../../util/error-response');
  var categoriesUtil = require('./categories-util');
  var categoryUtil = require('../category/category-util');

  var categoriesEndpoint = new Endpoint({

    url: '/categories',

    // Returns all of the categories
    get: function(url, data, headers, params, currentUser) {
      return [200, categoriesUtil.getAllCategories()];
    },

    // Creates a new category
    post: function(url, data, headers, params, currentUser) {

      var text = data.text;

      // Error out if we don't have any text
      if (!text) {
        return new ErrorResponse(400, 'Missing text field.');
      }

      // Error out if the text isn't a string
      else if (!_.isString(text)) {
        return new ErrorResponse(422, 'Text must be a string.');
      }

      // Also error out if it already exists
      else if (categoryUtil.categoryExists({text: text})) {
        return new ErrorResponse(409, 'The category already exists.');
      }

      return [200, categoriesUtil.createCategory(data, currentUser)];
    }
  });

  return categoriesEndpoint;
});
