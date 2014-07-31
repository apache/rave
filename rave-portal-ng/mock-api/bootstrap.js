define(function(require) {
	'use strict';

	// setup our application
	require('rave');

	// setup the mocked database
	require('./database/install.js');

	// The core API
	var api = require( './core.js');

	// The decorator routes:
	require('./modules/authentication/login/post.js');
	require('./modules/authentication/logout/post.js');
	require('./modules/authentication/verify/post.js');
	require('./modules/authentication/forgot-password/post.js');
	require('./modules/authentication/forgot-username/post.js');
	require('./modules/authentication/create-account/post.js');
	require('./modules/pages/get.js');
	require('./modules/status/get.js');

	// bootstrap the API
	rave.run(api.initialize);
});
