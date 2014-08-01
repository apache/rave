define(function(require) {
	'use strict';

	// dependencies
	require('underscore/underscore');
	var Storage = require('localStorageDB');

	// setup vars
	var database = new Storage('rave', localStorage);
	var dataImport = {
		users: require('./import-data/users.js'),
		categories: require( './import-data/categories.js' )
	};

	function importTables() {
		var tables = _.keys(dataImport);
		_.each(tables, function(tableName) {
			database.createTableWithData(tableName, dataImport[tableName]);
			database.commit();
		});
	}

	if (database.isNew()) {
		importTables();
	}

});