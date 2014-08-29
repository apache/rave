/*
 * endpoint
 * The /widget endpoint
 *
 */

define(function(require) {
  var Endpoint = require('../../util/endpoint');
  var ErrorResponse = require('../../util/error-response');
  var widgetUtil = require('./widget-util');
  var categoriesUtil = require('../categories/categories-util');

  var endpoint = new Endpoint({

    url: '/widget/:id',

    // Return our paginated data
    get: function(url, data, headers, params, currentUser) {

      // Get the widget's ID from the url
      var widgetId = widgetUtil.idFromUrl(url);

      // If the ID is invalid, throw an error
      if (!_.isNumber(widgetId) || _.isNaN(widgetId)) {
        return new ErrorResponse(400, 'Invalid widget ID');
      }

      // Retrieve the widget from the database
      var widget = widgetUtil.get({ID:widgetId});

      // If there's no widget then we return the 404
      if (!widget) {
        return new ErrorResponse(404, 'User does not exist.');
      }

      widget.categoriesList = categoriesUtil.getAllCategories();

      // Return the widget
      return [200, widget];
    },

    put: function(url, data, headers, params, currentUser) {

      // Get the widget's ID from the url
      var widgetId = widgetUtil.idFromUrl(url);

      // If the ID is invalid, throw an error
      if (!_.isNumber(widgetId) || _.isNaN(widgetId)) {
        return new ErrorResponse(400, 'Invalid widget ID.');
      }

      // Return a 404 if the widget doesn't exist
      if (!widgetUtil.widgetExists({ID:widgetId})) {
        return new ErrorResponse(404, 'User does not exist.');
      }

      // Actually make the update
      widgetUtil.updateUser({ID: widgetId}, data);

      // We want to return the widget, so we retrieve them from the database
      var widget = widgetUtil.get({ID:widgetId});

      // If there's no widget then we return the 404
      if (!widget) {
        return new ErrorResponse(404, 'User does not exist.');
      }

      // Make the return
      return [200, widget];
    },

    delete: function(url, data, headers, params, currentUser) {
      
      // Get the widget's ID from the url
      var widgetId = widgetUtil.idFromUrl(url);

      // If the ID is invalid, throw an error
      if (!_.isNumber(widgetId) || _.isNaN(widgetId)) {
        return new ErrorResponse(400, 'Invalid widget ID.');
      }

      // Return a 404 if the widget doesn't exist
      if (!widgetUtil.widgetExists({ID:widgetId})) {
        return new ErrorResponse(404, 'User does not exist.');
      }

      // Actually delete the widget
      widgetUtil.deleteUser({ID:widgetId});

      // Share that the widget was deleted
      return [200, null];
    }
  });

  return endpoint;
});
