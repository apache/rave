define(function(require) {
  require('./home');
  
  var angular = require('angular');

  angular.module('home').controller('homeController', function($http) {
    /*
    var data = {
    	headers: {
    		'Authorization': 'Basic p996b5k20s9dywdaipjenfddynnu55tl'
    	},
      data: {},
      url: '/api/v1/category/6',
      method: 'DELETE'
    };
    $http(data).then( function(data, responseCode, headers, config) {
      console.log('data', data);
    }).catch( function(err) {
      console.log('err', err);
    } );
    */
  });
});
