(function(rave) {

rave.controller( 'homeController', function( $http ) {
	$http.get( '/api/v1/status' ).success( function( data, responseCode, headers, config ) {
		// `data` contains the response from the API
	} );
} );

})(rave);
