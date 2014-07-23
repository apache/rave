(function(window, api, undefined) {

	function processPagesRequest(method, url, data) {
		var responseData = {
			status: 'ok'
		};
		return [200, responseData];
	}

	api.register('/pages', 'get', processPagesRequest);

})(window, api);