define(function(require) {
	'use strict';

	var api = require('../core.js');

	function processPagesRequest(method, url, data) {
		var responseData = {
			status: 'ok'
		};
		return [200, responseData];
	}

	api.register('/pages', 'get', processPagesRequest);

});