define(function(require) {
	'use strict';

	var api = require('../../core.js');
	require('underscore/underscore');

	function getPreferencesByKey(key) {
		var results = api.db.query('preferences', {
			key: key
		});

		if (results.length === 1) {
			results = results[0];
			if (results.values.length === 1) {
				results.values = results.values[0];
			}
			return results;
		}
		return false;
	}

	function processRequest(method, url, data, headers) {
		if (method !== 'GET') {
			return [405, 'Unknown request'];
		} else if (!this.requestHasToken) {
			return [401, 'A valid token is required'];
		} else if (!this.userIsAuthenticated) {
			return [401, 'Invalid token'];
		}

		var preferenceKey = url.replace('/api/v1/preference/', '');
		if (!_.isString(preferenceKey)) {
			return [400, 'Invalid category ID'];
		}

		var preference = getPreferencesByKey(preferenceKey);
		if (!preference) {
			return [404, 'Preference does not exist'];
		}

		return [200, preference];
	}

	api.register('/preference/:key', 'get', processRequest);

} );