/*
 * bootstrap
 * 
 * Note: This file is to be included during development only
 *
 * This file fakes out our initial data.
 * 
 */

define(function(require) {

  var api = require('./core.js');
  require('underscore/underscore');

  function getPreferences() {
    var results = api.db.query('preferences');

    var preferences = {};

    _.each(results, function(item) {
      preferences[item.key] = item.value;
    });

    return preferences;
  }

  var initialData = {};

  initialData.preferences = getPreferences();

  window.initialData = initialData;
});
