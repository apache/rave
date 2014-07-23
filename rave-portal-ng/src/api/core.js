window.api = ( function( window, undefined ) {

	'use strict';
	var baseApiUrl = '/api/v1';
	var registeredApiMethods = {};
	var storage = {};

	// route conversion utility pulled from backbone:
	// https://github.com/jashkenas/backbone/blob/master/backbone.js#L1353 and
	// https://github.com/jashkenas/backbone/blob/master/backbone.js#L1289-L1292
	function convertRouteToRegex( route ) {
		var optionalParam = /\((.*?)\)/g;
		var namedParam    = /(\(\?)?:\w+/g;
		var splatParam    = /\*\w+/g;
		var escapeRegExp  = /[\-{}\[\]+?.,\\\^$|#\s]/g;

		route = route.replace( escapeRegExp, '\\$&' )
					.replace( optionalParam, '(?:$1)?' )
					.replace( namedParam, function( match, optional ) {
						return optional ? match : '([^/?]+)';
					} )
					.replace( splatParam, '([^?]*?)' );
		return new RegExp( '^' + route + '(?:\\?([\\s\\S]*))?$' );
	}

	function initializeApiModule($httpBackend) {

		function registerRouteMethods(routeData) {
			var pattern = routeData.pattern;
			var methods = routeData.methods;
			for (var method in methods) {
				if (methods.hasOwnProperty(method)) {
					var registerMethod = $httpBackend[ 'when' + method];
					var callback = methods[method];
					registerMethod.call( undefined, pattern )
						.respond( callback );
				}
			}
		}

		// register our API methods
		for (var route in registeredApiMethods) {
			if (registeredApiMethods.hasOwnProperty(route)) {
				registerRouteMethods(registeredApiMethods[route]);
			}
		}

		// register catch-alls for non-api related URLs
		var matchAllRegex = /(.*)+/;
		$httpBackend.whenGET(matchAllRegex).passThrough();
		$httpBackend.whenDELETE(matchAllRegex).passThrough();
		$httpBackend.whenPOST(matchAllRegex).passThrough();
		$httpBackend.whenPUT(matchAllRegex).passThrough();
		$httpBackend.whenJSONP(matchAllRegex).passThrough();
	}

	function registerApiMethod(route, method, callback) {
		if (!(/^(get|delete|post|put|jsonp)$/.test(method.toLowerCase()))) {
			throw new Error('Invalid API method.');
		} else if (typeof callback !== 'function') {
			throw new Error('Callback must be a valid function.');
		}

		if (!registeredApiMethods.hasOwnProperty(route)) {
			registeredApiMethods[route] = {
				pattern: convertRouteToRegex(baseApiUrl+route),
				methods: {}
			};
		}
		registeredApiMethods[route].methods[method.toUpperCase()] = callback;
		return true;
	}

	function setStorage(name, value) {
		storage[name] = value;
	}

	function getStorage(name) {
		return storage[name];
	}

	// expose methods we want publicly available
	return {
		initialize: initializeApiModule,
		register: registerApiMethod,
		set: setStorage,
		get: getStorage
	};

} )( window );