( function( window, rave, undefined ) {

	'use strict';
	var baseApiUrl = '/api/v1';
	var apiRoutes = {
		'/status': {
			'get': 'status'
		},
		'/pages': {
			'get': 'pages',
			'post': 'pages'
		},
		'/pages/:id': {
			'get': 'pagesId',
			'post': 'pagesId',
			'delete': 'pagesId'
		}
	};
	var API = {
		'status': function( method, url, data ) {
			return [ 200, { status: 'ok' } ];
		},
		'pages': function( method, url, data ) {
			return [ 200, { status: 'ok' } ];
		},
		'pagesId': function( method, url, data ) {
			return [ 200, { status: 'ok' } ];
		}
	};

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

	// handles setting up this angular module so we can fake API methods
	function raveAPIModule( $httpBackend ) {

		function handleUnrelatedRequests() {
			var matchAllRegex = /^\w+.*/;

			$httpBackend.whenGET( matchAllRegex ).passThrough();
			$httpBackend.whenDELETE( matchAllRegex ).passThrough();
			$httpBackend.whenPOST( matchAllRegex ).passThrough();
			$httpBackend.whenPUT( matchAllRegex ).passThrough();
			$httpBackend.whenJSONP( matchAllRegex ).passThrough();
		}

		function registerAPIMethodsForRoute( route, methods ) {
			route = convertRouteToRegex( baseApiUrl + route );
			var callback, registerMethod;
			for( var method in methods ) {
				if( methods.hasOwnProperty( method ) ) {
					callback = methods[ method ];
					registerMethod = $httpBackend[ 'when' + method.toUpperCase() ];
					registerMethod.call( undefined, route )
						.respond( API[ callback ] );
				}
			}
		}

		function defineAPIMethods() {
			for( var route in apiRoutes ) {
				if( apiRoutes.hasOwnProperty( route ) ) {
					registerAPIMethodsForRoute( route, apiRoutes[ route ] );
				}
			}
		}

		function initializeRaveAPI() {
			defineAPIMethods();
			handleUnrelatedRequests();
		}

		initializeRaveAPI();
	}

	rave.run( raveAPIModule );

} )( window, rave );