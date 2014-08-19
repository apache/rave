/*
 * preferences-util
 * Methods for getting and setting preferences data. Anytime
 * you need to hit the DB in reference to preferences it should
 * go through this file.
 *
 */

define(function(require) {
  var api = require('../../core');

  var preferencesUtil = {

    // Retrieve all of your preferences from the database.
    // Although they're stored in the DB an array of objects with two
    // properties each, key and value, this method transforms that data into an object.
    getAll: function() {
      var results = api.db.query('preferences');
      var preferences = {};

      _.each(results, function(item) {
        preferences[item.key] = item.value;
      });

      return preferences;
    },

    // Get a specific preference by name.
    // Returns null if it doesn't exist.
    getPreference: function(preferenceName) {

      // Get all of our preferences
      var prefs = preferencesUtil.getAll();

      // Find the one that matches the name
      var preference = prefs[preferenceName];

      // Return it if it exists; otherwise, null
      return (typeof preference !== 'undefined') ? preference : null;
    },

    // Update the data in the database
    put: function(data) {
      var searchParams;

      // Loop through each value, updating it in the database
      _.each(data, function(value, key) {
        searchParams = {
          key: key
        };
        api.db.update('preferences', searchParams, function(row) {
          row.value = value;
          return row;
        });
      });

      // Save our data
      api.db.commit();
    }
  };

  return preferencesUtil;
});
