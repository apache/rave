define(function(require) {
	'use strict';

	var api = require('../../core.js');
	require('underscore/underscore');

	function preferenceExists(key) {
		var results = api.db.query('preferences', {
			key: key
		});

		if (results.length === 1) {
			return true;
		}
		return false;
	}

	function updatePreference(key, values) {
		var searchParams = {
			key: key
		};

		if (!_.isArray(values)) {
			values = [values];
		}

		api.db.update('preferences', searchParams, function(row) {
			row.values = values;
			return row;
		});

		var results = api.db.query('preferences', searchParams);
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
		if (method !== 'PUT') {
			return [405, 'Unknown request'];
		} else if (!this.requestHasToken) {
			return [401, 'A valid token is required'];
		} else if (!this.userIsAuthenticated) {
			return [401, 'Invalid token'];
		}

		var preferenceKey = url.replace('/api/v1/preference/', '');
		if (!_.isString(preferenceKey)) {
			return [400, 'Invalid category ID'];
		} else if (!preferenceExists(preferenceKey)) {
			return [404, 'Preference does not exist'];
		}

		data = angular.fromJson(data);

		if (!data.values) {
			return [400, 'Missing values field'];
		}

		var updatedPreference = updatePreference(preferenceKey, data.values);
		if (!updatedPreference) {
			return [500, 'An internal database error has occurred'];
		}

		return [200, updatedPreference];
	}

	api.register('/preference/:key', 'put', processRequest);

} );