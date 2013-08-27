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
    /* Local Variables */
    var addNewPageTitle = "ADD A NEW PAGE",
        updatePageTitle = "UPDATE PAGE";

    /* Controller Definition */
    return ['$scope', 'dialog', 'page', 'pageLayouts', 'Pages', function ($scope, dialog, page, pageLayouts, Pages) {

        $scope.page = angular.copy(page);

        $scope.title = resolveModalTitle($scope.page);

        $scope.layouts = pageLayouts;

        $scope.isUserSelectable = function(pageLayout) {
            return pageLayout.userSelectable;
        }

        $scope.close = function() {
            dialog.close();
        }

        $scope.savePage = function() {
            $scope.error = null;
            Pages.save($scope.page).$then(function(page) {
                $scope.page = page;
                addNewPageToOrUpdatePageInPagesArray($scope);
                dialog.close();
            }, function(response) {
                //TODO: When REST API is fully implemented, set error msg to custom response from server
                $scope.error = "Server-side ERROR";
            });
        }
    }];

    /* Helpers */
    function resolveModalTitle(page) {
       if(page) {
        return updatePageTitle;
       } else {
        return addNewPageTitle;
       }
    }

    function addNewPageToOrUpdatePageInPagesArray($scope) {
        var pageEntry = _.findWhere($scope.pages, $scope.page);
        if(pageEntry) {
             _.extend(pageEntry, $scope.page);
        } else {
            $scope.pages.push($scope.page);
        }
    }
});
