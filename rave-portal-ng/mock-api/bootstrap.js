define(function(require) {
	'use strict';

	// setup our application
	require('rave');

	// setup the mocked database
	require('./install-database.js');

	// The core API
	var api = require( './core.js');

	// The decorator routes
	require('./authentication/login.js');
	require('./authentication/logout.js');
	require('./authentication/verify.js');
	require('./authentication/forgot-password.js');
	require('./authentication/forgot-username.js');
	require('./authentication/create-account.js');
	require('./pages/pages.js');
	require('./status/status.js');

	// bootstrap the API
	rave.run(api.initialize);
});
