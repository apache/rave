(function(window, api, undefined) {

	function processStatusRequest(method, url, data) {
		var responseData = {
			status: 'ok'
		};
		return [200, responseData];
	}

	api.register('/status', 'get', processStatusRequest);

})(window, api);