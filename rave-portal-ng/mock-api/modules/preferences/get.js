define(function(require) {
	'use strict';

	var api = require('../../core.js');
	require('underscore/underscore');

	function getPreferences() {
		var results = api.db.query('preferences');
		results = _.map(results, function(item) {
			if (item.values.length === 1) {
				item.values = item.values[0];
			}
			return item;
		});
		return results;
	}

	function processRequest(method, url, data, headers) {
		if (method !== 'GET') {
			return [405, 'Unknown request'];
		} else if (!this.requestHasToken) {
			return [401, 'A valid token is required'];
		} else if (!this.userIsAuthenticated) {
			return [401, 'Invalid token'];
		}

		return [200, getPreferences()];
	}

	api.register('/preferences', 'get', processRequest);

} );