define(function(require) {
  'use strict';

  var api = require('../../core.js');
  require('underscore/underscore');

  function updatePreferences(data) {
    var searchParams;

    // Loop through each data point, updating the corresponding preference
    // in the database.
    _.each(data, function(value, key) {
      searchParams = {
        key: key
      };
      api.db.update('preferences', searchParams, function(row) {
        row.value = value;
        return row;
      });
    });
    api.db.commit();

    // This is exactly what we do in the GET method. We might
    // want to DRY this up, but it's a mock API so no big deal.
    var results = api.db.query('preferences');
    var preferences = {};

    _.each(results, function(item) {
      preferences[item.key] = item.value;
    });

    return preferences;
  }

  function processRequest(method, url, data, headers) {
    if (method !== 'PUT') {
      return [405, 'Unknown request'];
    } else if (!this.requestHasToken) {
      return [401, 'A valid token is required'];
    } else if (!this.userIsAuthenticated) {
      return [401, 'Invalid token'];
    }

    data = angular.fromJson(data);

    var updatedPreferences = updatePreferences(data);
    if (!updatedPreferences) {
      return [500, 'An internal database error has occurred'];
    }

    return [200, updatedPreferences];
  }
  
  api.register('/preferences', 'put', processRequest);

} );
