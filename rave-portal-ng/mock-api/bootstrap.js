define(function(require) {
	'use strict';

	// setup our application
	require('rave');

	// setup the mocked database
	require('./database/install.js');

	// The core API
	var api = require( './core.js');

	// The decorator routes:
	
	// auth/login
	require('./modules/authentication/login/post.js');

	// auth/logout
	require('./modules/authentication/verify/post.js');

	// auth/forgot-password
	require('./modules/authentication/forgot-password/post.js');

	// auth/forgot-username
	require('./modules/authentication/forgot-username/post.js');

	// auth/create-account
	require('./modules/authentication/create-account/post.js');

	// categories
	require('./modules/categories/get.js');
	require('./modules/categories/post.js');

	// category
	require('./modules/category/get.js');
	require('./modules/category/put.js');
	require('./modules/category/delete.js');

	// preferences
	require('./modules/preferences/get.js');
	require('./modules/preferences/put.js');

	// pages
	require('./modules/pages/get.js');

	// status
	require('./modules/status/get.js');

	// bootstrap the API
	rave.run(api.initialize);
});
