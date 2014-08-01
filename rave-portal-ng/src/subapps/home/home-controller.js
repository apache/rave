define(function(require) {
  require('./home');
  
  var angular = require('angular');

  angular.module('home').controller('homeController', function($http) {
    /*
    var data = {
    	headers: {
    		'Authorization': 'Basic 9pe3labzk09v718atvivjnmy8b513k4u'
    	},
      data: {
        values: 20
      },
      url: '/api/v1/preference/pageSize',
      method: 'PUT'
    };
    $http(data).then( function(data, responseCode, headers, config) {
      console.log('data', data);
    }).catch( function(err) {
      console.log('err', err);
    } );
    */
  });
});
