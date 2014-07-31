define(function(require) {
	'use strict';

	var api = require('../../../core.js');

	// All of these 'helper' methods should be abstracted into (a) separate module(s).
	function userById(id) {
		var results = api.db.query('users', {
			id: id
		});

		return results.length === 1 ? results[0] : false;
	}

	function verify(method, url, data) {
		if (method !== 'POST') {
			return [405, 'Unknown request'];
		}

		data = angular.fromJson(data);

		var token = data.token;
		var storedToken = api.session.get('token');
		var success = token === storedToken;
		var res, statusCode;

		statusCode = success ? 200 : 401;

		if (!token) {
			res = 'A token is required';
		} else if (!success) {
			res = 'Invalid token.';
		} else if (success) {

			var user = userById(api.session.get('currentUserId'));

			if (!user) {
				res = 'An internal server error has occurred.';
			} else {
				res = {
					authorized: true,
					user: {
						id: user.id,
						password: user.password,
						authLevel: 'admin',
						name: user.nameSeenByOthers
					}
				};
			}
		}

		return [statusCode, res];
	}

	api.register('/auth/verify', 'post', verify);
});