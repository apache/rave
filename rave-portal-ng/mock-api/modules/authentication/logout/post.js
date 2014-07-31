define(function(require) {
	'use strict';

	var api = require('../../../core.js');

	function processLogoutRequest(method, url, data) {
		if (method !== 'POST') {
			return [405, 'Unknown request'];
		} else if (!api.session.get('authorized')) {
			return [200, null];
		}

		api.session.set('authorized', false);
		api.session.remove('token');
		api.session.remove('currentUserId');
		return [200,null];
	}

	api.register('/auth/logout', 'post', processLogoutRequest);

});