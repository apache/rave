angular.module('rave.service', ['ngResource'])
    .value('rave', rave)
    .service('Pages', ['$http', '$interpolate',
        function ($http, $interpolate) {
/*
            return $http.get('/portal/api/rest/pages/portal/@self').then(function (response) {
                return response.data.Page;
            });*/


            var urlTemplate = $interpolate('/portal/api/rest/pages/{{context}}/{{identifier}}');
            var Pages = {};

            Pages.get = function(context, identifier, id) {
                return $http.get(urlTemplate({context: context, identifier: identifier})).then(function (response) {
                    return response.data.Page;
                });
            }

            return Pages;


        }
    ])
    .service('settings', ['$resource',
        function () {

        }
    ]);