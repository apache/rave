(function(window, api, angular, undefined) {

	var validLogins = {
		'carldanley': {
			id: 1,
			authLevel: 'admin',
			password: 'carldanley',
			name: 'carl'
		},
		'jmeas': {
			id: 34,
			authLevel: 'admin',
			password: 'jmeas',
			name: 'jmeas'
		}
	};

	function getSessionData() {
		var session = api.get('session');
		if (!session) {
			session = {
				authorized: false,
				user: null,
				nav: null
			};
			api.set('session', session);
		}

		return session;
	}

	function isValidUser(username, password) {
		if (!validLogins.hasOwnProperty(username)) {
			return false;
		} else if (validLogins[username].password !== password) {
			return false;
		}

		return true;
	}

	function setCurrentSessionUser( username ) {
		var session = getSessionData();
		session.authorized = true;
		session.user = validLogins[username];
		api.set('session', session);
	}

	function processLoginRequest(method, url, data) {
		if (method !== 'POST') {
			return [405, 'Unknown request'];
		}

		var session = getSessionData();
		data = angular.fromJson(data);

		// now attempt to login the user
		if (!data.hasOwnProperty('username') || !data.hasOwnProperty('password')) {
			return [401,'Missing username and/or password field(s).'];
		} else if (!isValidUser(data.username, data.password)) {
			return [401,'Invalid login.'];
		}

		setCurrentSessionUser(data.username);
		return [200,getSessionData()];
	}

	function processLogoutRequest(method, url, data) {
		if (method !== 'POST') {
			return [405,'Unknown request'];
		}

		var session = getSessionData();
		if (!session.authorized) {
			return [200,null];
		}

		session.authorized = false;
		session.user = null;
		api.set('session', session);
		return [200,null];
	}

	api.register('/auth/login', 'post', processLoginRequest);
	api.register('/auth/logout', 'post', processLogoutRequest);

})(window, api, angular);