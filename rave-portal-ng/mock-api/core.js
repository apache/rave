define(function(require) {
	'use strict';

	var Storage = require('localStorageDB');
	var database = new Storage('rave', localStorage);
	var baseApiUrl = '/api/v1';
	var registeredApiMethods = {};

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

	function setSessionStorage(key, value) {
		try {
			value = JSON.stringify(value);
			sessionStorage.setItem(key, value);
		} catch (err) {}
	}

	function getSessionStorage(key) {
		var data = sessionStorage.getItem(key);
		try {
			data = JSON.parse(data);
		} catch (err) {
			data = null;
		}

		return data;
	}

	function removeSessionStorage(key) {
		return sessionStorage.removeItem(key);
	}

	// expose methods we want publicly available
	return {
		initialize: initializeApiModule,
		register: registerApiMethod,
		db: database,
		session: {
			set: setSessionStorage,
			get: getSessionStorage,
			remove: removeSessionStorage
		}
	};

});
