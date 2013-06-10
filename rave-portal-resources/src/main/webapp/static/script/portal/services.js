define(['app', 'core/rave_core', 'angularResource'], function (app, rave) {
    app.requires.push('ngResource');

    app.factory('Pages', ['$resource',
            function ($resource) {
                return $resource('/portal/api/rest/pages/:context/:identifier/:id', {})
            }
        ])
        .factory('PagesLoader', ['Pages', '$q',
            function (Pages, $q) {
                return function () {
                    var deferred = $q.defer();

                    Pages.get({context: 'portal', identifier: '@self'}, function (pages) {
                        deferred.resolve(pages.Page);
                    });

                    return deferred.promise;
                }

            }
        ])
//        .service('Pages', ['$http', '$interpolate', '$q', '$rootScope',
//            function ($http, $interpolate, $q, $rootScope) {
//                var urlTemplate = $interpolate('/portal/api/rest/pages/{{context}}/{{identifier}}'),
//                    pages;
//                var Pages = {};
//
//                /*
//                 TODO: this works for now but I dont like the get() approach
//                 Probably a better approach is autodetecting context and requesting the data object - return that promise
//                 and attach modification functions to the promise
//                 */
//                Pages.get = function (context, identifier, id) {
//                    var url = urlTemplate({context: context, identifier: identifier});
//                    if (id) {
//                        url += '/' + id
//                    }
//                    pages = $http.get(url).then(function (response) {
//                        return response.data.Page;
//                    });
//                    return pages;
//                }
//
//                Pages.add = function (name, layout) {
//                    var deferred = $q.defer();
//
//                    rave.api.rpc.addPage({
//                        pageName: name,
//                        pageLayoutCode: layout,
//                        successCallback: function (result) {
//                            if (result == 'DUPLICATE_ITEM') {
//                                $rootScope.$apply(deferred.reject('DUPLICATE_ITEM'));
//                            }
//                            else {
//                                var newPage = result.result;
//                                pages.then(function (pages) {
//                                    /*
//                                     TODO: newPage object returned by rpc api is different format from rest api,
//                                     screws up pagelayout code and possibly more
//                                     */
//                                    newPage.pageLayoutCode = newPage.pageLayout.code;
//                                    pages.push(newPage);
//                                });
//                                $rootScope.$apply(deferred.resolve(newPage));
//                            }
//                        }
//                    });
//
//                    return deferred.promise;
//                }
//
//                Pages.move = function (pageId, afterPageId) {
//                    var deferred = $q.defer();
//
//                    rave.api.rpc.movePage({
//                        pageId: pageId,
//                        moveAfterPageId: afterPageId,
//                        successCallback: function (result) {
//                            pages.then(function (pages) {
//                                var movingPage = _.findWhere(pages, {id: pageId})
//                                var fromIdx = _.indexOf(pages, movingPage);
//                                var toIdx = _.indexOf(pages, _.findWhere(pages, {id: afterPageId}));
//                                pages.splice(fromIdx, 1)
//                                pages.splice(toIdx, 0, movingPage);
//                            });
//                            $rootScope.$apply(deferred.resolve(result));
//                        }
//                    });
//
//                    return deferred.promise;
//                }
//
//                Pages.edit = function (pageId, title, layout) {
//                    var deferred = $q.defer();
//
//                    rave.api.rpc.updatePagePrefs({
//                        pageId: pageId,
//                        title: title,
//                        layout: layout,
//                        successCallback: function (result) {
//                            var newPage = result.result;
//                            //TODO: this probably will not end up working - we are not getting rendered context back
//                            pages.then(function (pages) {
//                                var edited = _.findWhere(pages, {id: pageId});
//                                var idx = _.indexOf(pages, edited);
//                                pages.splice(idx, 1, newPage);
//                            });
//                            $rootScope.$apply(deferred.resolve(result));
//                        }
//                    });
//
//                    return deferred.promise;
//                }
//
//                Pages.delete = function (pageId) {
//                    var deferred = $q.defer();
//
//                    rave.api.rest.deletePage({
//                        pageId: pageId,
//                        successCallback: function (result) {
//                            pages.then(function (pages) {
//                                var deleted = _.findWhere(pages, {id: pageId});
//                                var idx = _.indexOf(pages, deleted);
//                                pages.splice(idx, 1);
//                            });
//                            $rootScope.$apply(deferred.resolve(result));
//                        }
//                    });
//
//
//                    return deferred.promise;
//                }
//
//                Pages.deleteRegionWidget = function (regionWidget) {
//                    //TODO: we need a better way to represent & keep data in sync
//                    pages.then(function (pages) {
//                        _.find(pages, function (page) {
//                            return _.find(page.regions, function (region) {
//                                return _.find(region.regionWidgets, function (widget, i) {
//                                    if (widget.id == regionWidget.id) {
//                                        region.regionWidgets.splice(i, 1);
//                                        regionWidget.close();
//                                        return true;
//                                    }
//                                })
//                            })
//                        })
//                    });
//                }
//
//                Pages.share = function (pageId, userId, role) {
//                }
//
//                return Pages;
//            }
//        ])
        .service('user', ['$q',
            function ($q) {
                var deferred = $q.defer();

                deferred.resolve({

                });

                return deferred.promise;
            }
        ])
        .service('Users', [
            function () {

            }
        ])
        .service('settings', [
            function () {

            }
        ])
        .service('views', [
            function () {
                //TODO: write a view service to be more robust than ng-include, probably with a corresponding directive
            }
        ]);
});
