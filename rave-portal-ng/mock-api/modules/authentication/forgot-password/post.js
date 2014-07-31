define(function(require) {
	'use strict';

	var api = require('../../../core.js');

	function userExists(email) {
		var results = api.db.query('users', {
			email: email
		});

		if (results.length === 1) {
			return true;
		}

		return false;
	}

	function processForgotPasswordRequest(method, url, data) {
		if (method !== 'POST') {
			return [405, 'Unknown request'];
		}

		data = angular.fromJson(data);

		if (!data.hasOwnProperty('email')) {
			return [400, 'Missing email address field'];
		} else if (!userExists(data.email)) {
			return [422, 'User with that email does not exist'];
		}

		return [200, 'Email sent'];
	}

	api.register('/auth/forgot-password', 'post', processForgotPasswordRequest);

});