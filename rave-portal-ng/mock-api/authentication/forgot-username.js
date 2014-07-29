define(function(require) {
	'use strict';

	var api = require('../core.js');

	function processForgotUsernameRequest(method, url, data) {
		if (method !== 'POST') {
			return [405, 'Unknown request'];
		}

		data = angular.fromJson(data);

		if (!data.hasOwnProperty('email')) {
			return [400, 'Missing email address field'];
		}

		return [200, 'Email sent'];
	}

	api.register('/auth/forgot-username', 'post', processForgotUsernameRequest);

});