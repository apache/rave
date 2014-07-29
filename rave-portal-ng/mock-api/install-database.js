define(function(require) {
	'use strict';

	var Storage = require('localStorageDB');
	var database = new Storage('rave', localStorage);

	function setupUsersTable() {
		var users = [
			{
				'id': 1,
				'username': 'carldanley',
				'password': 'carldanley',
				'email': 'carl@bocoup.com',
				'openIdUrl': '',
				'firstName': 'Carl',
				'lastName': 'Danley',
				'nameSeenByOthers': 'Carl',
				'relationshipStatus': 'Single',
				'description': 'I like JS.'
			},
			{
				'id': 34,
				'username': 'jmeas',
				'password': 'jmeas',
				'email': 'james@bocoup.com',
				'openIdUrl': '',
				'firstName': 'James',
				'lastName': 'Smith',
				'nameSeenByOthers': 'James',
				'relationshipStatus': 'Single',
				'description': 'I like JS.'
			}
		];
		// create the table and load the data now
		database.createTableWithData('users', users);
		database.commit();
	}

	if (database.isNew()) {
		setupUsersTable(); 
	}

});