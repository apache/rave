define(function(require) {
	'use strict';

	require('underscore/underscore');
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

	// All of these 'helper' methods should be abstracted into (a) separate module(s).
	function retrieveUserByToken(token) {
		var results = api.db.query('users', {
			sessionToken: token
		});

		return (results.length === 1) ? results[0] : false;
	}

	function verify(method, url, data) {
		data = angular.fromJson(data);

		if (method !== 'POST') {
			return [405, 'Unknown request'];
		} else if (!data.hasOwnProperty('token')) {
			return [400, 'Missing token field'];
		} else if (!data.token || !_.isString(data.token) || data.token.length !== 32) {
			return [401, 'A valid token is required'];
		}

		var clientToken = data.token;
		var user = retrieveUserByToken(clientToken);

		// now, compare the session tokens
		if (!user) {
			return [401, 'Invalid token'];
		}

		user = _.pick(user, userKeys);

		// return the user object
		return [200, {
			authorized: true,
			user: user
		}];
	}

	api.register('/auth/verify', 'post', verify);
});
