define(function(require) {
	'use strict';

	var api = require('../../core.js');
	require('underscore/underscore');

	function categoryExists(id) {
		var results = api.db.query('categories', {
			ID: id
		});

		if (results.length === 1) {
			return true;
		}

		return false;
	}

	function deleteCategory(id) {
		api.db.deleteRows('categories', {
			ID: id
		});
		api.db.commit();
	}

	function processRequest(method, url, data, headers) {
		if (method !== 'DELETE') {
			return [405, 'Unknown request'];
		} else if (!this.requestHasToken) {
			return [401, 'A valid token is required'];
		} else if (!this.userIsAuthenticated) {
			return [401, 'Invalid token'];
		}

		// attempt to parse the category ID
		var categoryID = parseInt( url.replace( '/api/v1/category/', '' ), 10 );
		if (!_.isNumber(categoryID) || _.isNaN(categoryID)) {
			return [400, 'Invalid category ID'];
		} else if (!categoryExists(categoryID)) {
			return [404, 'Category does not exist'];
		}

		deleteCategory(categoryID);

		return [200, null];
	}

	api.register('/category/:id', 'delete', processRequest);

} );