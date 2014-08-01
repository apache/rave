define(function(require) {
	'use strict';

	var api = require('../../core.js');
	require('underscore/underscore');

	function processRequest(method, url, data, headers) {
		if (method !== 'GET') {
			return [405, 'Unknown request'];
		} else if (!this.requestHasToken) {
			return [401, 'A valid token is required'];
		} else if (!this.userIsAuthenticated) {
			return [401, 'Invalid token'];
		}

		return [200, api.db.query('categories')];
	}

	api.register('/categories', 'get', processRequest);

} );