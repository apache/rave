/*
 * widget-util
 * Utilities for widget data
 *
 */

define(function(require) {
  var api = require('../../core');

  var widgetUtil = {

    // Retrieve from the widget's ID from the URL
    idFromUrl: function(url) {
      return parseInt(url.replace('/api/v1/widget/', ''), 10);
    },

    // Whether a particular widget exists or not
    widgetExists: function(identifier) {
      return !!widgetUtil.get(identifier);
    },

    // Retrieve the widget by an identifier.
    get: function(identifier) {

      var results = api.db.query('widgets', identifier);

      var widget = results[0];

      // If we don't have a widget, then we return false
      if (!widget || results.length > 1) {
        return false;
      }

      return widget;
    },

    // Update the widget's information in the database
    updateUser: function(identifier, data) {

      api.db.update('widgets', identifier, function(row) {
        _.extend(row, data);
        return row;
      });
      api.db.commit();
    },

    // Delete a widget by identifier
    deleteUser: function(identifier) {
      api.db.deleteRows('widgets', identifier);
      api.db.commit();
    }
  };

  return widgetUtil;
});
