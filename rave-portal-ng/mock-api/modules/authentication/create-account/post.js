define(function(require) {
	'use strict';

	var api = require('../../../core.js');

	function usernameAlreadyExists(username) {
		var results = api.db.query('users', {
			username: username
		});

		if (results.length === 0) {
			return false;
		}

		return true;
	}

	function emailAlreadyExists(email) {
		var results = api.db.query('users', {
			email: email
		});

		if (results.length === 0) {
			return false;
		}

		return true;
	}

	function requiredFieldIsMissing(data) {
		var requiredFields = [
			'username',
			'password',
			'confirmPassword',
			'email'
		];
		for (var i = 0, len = requiredFields.length; i < len; i++) {
			var requiredField = requiredFields[i];
			if (!data.hasOwnProperty(requiredField)) {
				return [400, 'Missing ' + requiredField + ' field'];
			}
		}

		return false;
	}

	function isValidEmail(email) {
		var emailRegex = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
		return emailRegex.test(email);
	}

	function insertNewUser(data) {
		var newUserData = {
			username: data.username,
			password: data.password,
			email: data.email,
			openIdUrl: (data.hasOwnProperty('openIdUrl') ? data.openIdUrl : ''),
			firstName: (data.hasOwnProperty('firstName') ? data.firstName : ''),
			lastName: (data.hasOwnProperty('lastName') ? data.lastName : '' ),
			nameSeenByOthers: (data.hasOwnProperty('nameSeenByOthers') ? data.nameSeenByOthers : ''),
			relationshipStatus: (data.hasOwnProperty('relationshipStatus') ? data.relationshipStatus: ''),
			description: (data.hasOwnProperty('description') ? data.description: '')
		};

		api.db.insert('users', newUserData);
		api.db.commit();
	}

	function processCreateAccount(method, url, data) {
		if (method !== 'POST') {
			return [405, 'Unknown request'];
		}

		data = angular.fromJson(data);

		// validate that require fields exist
		var error = requiredFieldIsMissing(data);
		if (error !== false) {
			return error;
		}

		// now make sure this username doesn't exist
		if (usernameAlreadyExists(data.username)) {
			return [422, 'Username is not available'];
		}

		// now make sure this username doesn't exist
		else if (emailAlreadyExists(data.email)) {
			return [422, 'Email is not available'];
		}

		// else...
		else if (data.password.length < 8) {
			return [422, 'Password is not long enough'];
		}

		else if (!isValidEmail(data.email)) {
			return [422, 'Email address is not valid'];
		}

		// now build out the data that will be stored for this new user
		insertNewUser(data);

		// assume they need to click the verify email link to sign in (or something)
		return [200, 'Email sent'];
	}

	api.register('/auth/create-account', 'post', processCreateAccount);

});
