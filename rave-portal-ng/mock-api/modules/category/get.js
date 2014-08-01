define(function(require) {
	'use strict';

	var api = require('../../core.js');
	require('underscore/underscore');

	function getCategory(id) {
		var results = api.db.query('categories', {
			ID: id
		});

		if (results.length === 1) {
			return results[0];
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

		// attempt to parse the category ID
		var categoryID = parseInt(url.replace('/api/v1/category/', ''), 10);
		if (!_.isNumber(categoryID) || _.isNaN(categoryID)) {
			return [400, 'Invalid category ID'];
		}

		var category = getCategory(categoryID);
		if (!category) {
			return [404, 'Category does not exist'];
		}

		return [200, category];
	}

	api.register('/category/:id', 'get', processRequest);

} );