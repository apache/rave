angular.module('rave.service', [])
    .value('rave', rave)
    .value('context')
    .service('pages', ['$http', function($http){
        return $http.get('/portal/api/rest/pages/portal/@self').then(function(response){
            return response.data.Page;
        });
    }]);