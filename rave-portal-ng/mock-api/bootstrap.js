define(function(require) {
	'use strict';

	// setup our application
	require('rave');

	// setup the mocked database
	require('./database/install.js');
	var api = require('./core');

	// auth/login
	api.registerEndpoint(require('./modules/authentication/login/endpoint'));

	// auth/logout
	api.registerEndpoint(require('./modules/authentication/verify/endpoint'));

	// auth/forgot-password
	api.registerEndpoint(require('./modules/authentication/forgot-password/endpoint'));

	// auth/forgot-username
	api.registerEndpoint(require('./modules/authentication/forgot-username/endpoint'));

	// auth/create-account
	api.registerEndpoint(require('./modules/authentication/create-account/endpoint'));

	// categories & category
	api.registerEndpoint(require('./modules/categories/endpoint'));
	api.registerEndpoint(require('./modules/category/endpoint'));

	// preferences
	api.registerEndpoint(require('./modules/preferences/endpoint'));

	// users & user
	api.registerEndpoint(require('./modules/users/endpoint'));
	api.registerEndpoint(require('./modules/user/endpoint'));

	// widgets
	api.registerEndpoint(require('./modules/widget/endpoint'));
	api.registerEndpoint(require('./modules/widgets/endpoint'));

	// status
	api.registerEndpoint(require('./modules/status/endpoint'));

	// bootstrap the API
	rave.run(api.initialize);
});
