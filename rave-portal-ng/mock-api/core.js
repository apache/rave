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

	function parseQueryString(str) {
		str = str.split('?')[1];
    if(typeof str !== 'string' || str.length === 0) {
    	return {};
    }
    var s = str.split('&');
    var sLength = s.length;
    var bit, query = {}, first, second;
    for (var i = 0; i < sLength; i++) {
      bit = s[i].split('=');
      first = decodeURIComponent(bit[0]);
      if(first.length === 0) {
      	continue;
      }
      second = decodeURIComponent(bit[1]);
      if(typeof query[first] === 'undefined') {
      	query[first] = second;
      }
      else if(query[first] instanceof Array) {
      	query[first].push(second);
      }
      else {
      	query[first] = [query[first], second];
      }
    }
    return query;
  }

	function buildApiScope(method, url, data, headers) {

		function requestHasToken() {
			if (!headers.Authorization) {
				return false;
			}

			var token = headers.Authorization.replace( 'Basic ', '' );
			if (token.length !== 32) {
				return false;
			}

			return true;
		}

		function getCurrentUserFromToken() {
			if (!requestHasToken(headers)) {
				return false;
			}

			var searchParams = {
				sessionToken: headers.Authorization.replace( 'Basic ', '' )
			};
			var results = database.query('users', searchParams);
			if (results.length === 1) {
				return results[0];
			}

			return false;
		}

		return {
			requestHasToken: requestHasToken(),
			userIsAuthenticated: ((getCurrentUserFromToken() !== false) ? true : false),
			currentUser: getCurrentUserFromToken(),
			parseQueryString: parseQueryString
		};
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

		// this is a really convenient hack to ensure that every HTTP callback
		// gets our utility methods for authentication
		var moddedCallback = function(method, url, data, headers) {
			var scope = buildApiScope(method, url, data, headers);
			return callback.bind(scope)(method, url, data, headers);
		};
		registeredApiMethods[route].methods[method.toUpperCase()] = moddedCallback;
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
