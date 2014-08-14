define(function(require) {
	'use strict';

	var api = require('../../../core.js');

	// The keys we want to send back when we're verified
	var userKeys = [
    'ID',
    'username',
    'description',
    'firstName',
    'lastName',
    'locked',
    'enabled',
    'expired',
    'authorities',
    'openIdUrl',
    'email'
  ];

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

	function updateUserSessionToken(username, token) {
		var searchParams = {
			username: username
		};
		api.db.update('users', searchParams, function(row) {
			row.sessionToken = token;
			return row;
		});
		api.db.commit();
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

		user = _.pick(user, userKeys);

		// update the user's token in the database
		var token = generateSessionToken();
		updateUserSessionToken(user.username, token);

		// return a record for the user
		return [200, {
			authorized: true,
			user: user,
			token: token
		}];
	}

	api.register('/auth/login', 'post', processLoginRequest);

});
