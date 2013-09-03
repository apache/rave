/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
 define(['underscore'], function(_) {

    /* Controller Definition */
    return ['$scope', '$dialog', '$location', 'Pages', 'PageLayouts', function($scope, $dialog, $location, Pages, PageLayouts) {

        $scope.page = _.findWhere($scope.pages, {id: $scope.currentPageId});

        $scope.$watch('pages.length', function(val){
            if(val === 1) {
                $scope.onlyOnePage= true;
            } else {
                $scope.onlyOnePage= false;
            }
        })

        var notTheOnlyPage = function() { return !$scope.onlyOnePage; };

        $scope.edit = function() {
            $dialog
                .dialog({
                    backdrop: true,
                    keyboard: true,
                    backdropClick: true,
                    templateUrl:  'addOrEditPageModal',
                    controller : 'AddOrEditNewPageModalCtrl',
                    resolve : {
                        page: function() { return $scope.page; },
                        pageLayouts : function() { return PageLayouts.query(); }
                    }
                 })
                 .open();
        }

        $scope.delete= function() {
            if( notTheOnlyPage() ) {
               $scope.page.$delete(function() {
                   deleteCurrentPageFromPagesArray($scope);
                   $location.path('/');
               });
            }
        }

        $scope.move= function() {
            if( notTheOnlyPage() ) {
                $dialog
                    .dialog({
                        backdrop: true,
                        keyboard: true,
                        backdropClick: true,
                        templateUrl:  'movePageModal',
                        controller: 'MovePageModalCtrl'
                    })
                    .open();
            }
        }

        $scope.export = function() {
            //TODO: Implement
            console.log('Export');
        }

        $scope.share = function() {
            //TODO: Implement
            console.log('Share');
        }

        $scope.unshare = function() {
            //TODO: Implement
            console.log('Unshare');
        }
    }]

    /* Helpers */
    function deleteCurrentPageFromPagesArray($scope) {
        var pageId = $scope.page.id,
            currentPage = _.find($scope.pages, function(page) { return page.id === pageId; }),
            indexOfCurrentPage = _.indexOf($scope.pages, currentPage);

        $scope.pages.splice(indexOfCurrentPage, 1);
    }
 });
