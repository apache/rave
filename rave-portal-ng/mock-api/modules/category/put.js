define(function(require) {
	'use strict';

	var api = require('../../core.js');
	require('underscore/underscore');
	require('moment/moment');

	function categoryExists(id) {
		var results = api.db.query('categories', {
			ID: id
		});

		if (results.length === 1) {
			return true;
		}

		return false;
	}

	function updateCategory(userID, categoryID, text) {
		var searchParams = {
			ID: categoryID
		};
		api.db.update('categories', searchParams, function(row) {
			row.text = text;
			row.lastModifiedUserId = userID;
			row.lastModifiedDate = moment().format();
			return row;
		});
		api.db.commit();

		var results = api.db.query('categories', searchParams);
		if (results.length === 1) {
			return results[0];
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

		// attempt to parse the category ID
		var categoryID = parseInt( url.replace( '/api/v1/category/', '' ), 10 );
		if (!_.isNumber(categoryID) || _.isNaN(categoryID)) {
			return [400, 'Invalid category ID'];
		}

		data = angular.fromJson(data);

		if (!data.text) {
			return [400, 'Missing text field'];
		} else if (!_.isString(data.text)) {
			return [422, 'Text must be a string'];
		}

		if (!categoryExists(categoryID)) {
			return [404, 'Category does not exist'];
		}

		var updatedCategory = updateCategory(this.currentUser.ID, categoryID, data.text);
		if (!updatedCategory) {
			return [500, 'An internal database error has occurred'];
		}

		return [200, updatedCategory];
	}

	api.register('/category/:id', 'put', processRequest);

} );
