define(function(require) {
	'use strict';

	var api = require('../core.js');

	function retrieveUser(username, password) {
		var results = api.db.query('users', {
			username: username,
			password: password
		});

		if (results.length === 1) {
			return results[0];
		}

		return false;
	}

	function generateSessionToken() {
		var length = 32, token = '';
		for (var i = 0; i < length; i++) {
			token += Math.random().toString(36).substr(2,1);
		}
		return token;
	}

	function processLoginRequest(method, url, data) {
		if (method !== 'POST') {
			return [405, 'Unknown request'];
		}

		data = angular.fromJson(data);

		// now attempt to login the user
		if (!data.hasOwnProperty('username') || !data.hasOwnProperty('password')) {
			return [401, 'Missing username and/or password field(s)'];
		}

		var user = retrieveUser(data.username, data.password);
		if (!user) {
			return [401, 'Invalid login.'];
		}

		var token = generateSessionToken();
		api.session.set('token', token);
		api.session.set('currentUserId', user.id);
		api.session.set('authorized', true);
		return [200, {
			authorized: true,
			user: {
				id: user.id,
				password: user.password,
				authLevel: 'admin',
				name: user.nameSeenByOthers,
				token: token
			}
		}];
	}

	api.register('/auth/login', 'post', processLoginRequest);

});
