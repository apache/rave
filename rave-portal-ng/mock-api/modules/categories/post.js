define(function(require) {
	'use strict';

	var api = require('../../core.js');
	require('underscore/underscore');
	require('moment/moment');

	function categoryExists(text) {
		var results = api.db.query('categories', {
			text: text
		});

		if (results.length >= 1) {
			return true;
		}
		return false;
	}

	function createCategory(text, user) {
		var newData = {
			text: text,
			createdUserId: user.ID,
			createdUserName: user.username,
			createdDate: moment().format(),
			lastModifiedUserId: user.ID,
			lastModifiedUserName: user.username,
			lastModifiedDate: moment().format()
		};
		api.db.insert('categories', newData);
		api.db.commit();
		var result = api.db.query('categories', {
			text: text
		});
		return result[0];
	}

	function processRequest(method, url, data, headers) {
		if (method !== 'POST') {
			return [405, 'Unknown request'];
		} else if (!this.requestHasToken) {
			return [401, 'A valid token is required'];
		} else if (!this.userIsAuthenticated) {
			return [401, 'Invalid token'];
		}

		data = angular.fromJson(data);

		if (!data.text) {
			return [400, 'Missing text field'];
		} else if (!_.isString(data.text)) {
			return [422, 'Text must be a string'];
		}

		if (categoryExists(data.text)) {
			return [409, 'This category already exists'];
		}

		return [200, createCategory(data.text, this.currentUser)];
	}

	api.register('/categories', 'post', processRequest);

} );
