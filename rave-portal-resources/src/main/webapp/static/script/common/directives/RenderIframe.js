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
 *                         registerView
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

/**
 * The RenderRegionWidget directive takes a regionWidget to be rendered in the region-widget="" attribute
 * and renders that regionWidget at the location of the directive.
 */

define(['angular', 'rave'], function(angular, rave){
    return [function(){
        var directive = {
            restrict: 'EA',
            replace: true,
            scope:{
                regionWidget: '=renderIframe'
            },
            link: function (scope, element, attrs){
                scope.$watch('regionWidget', function(){

                    if(scope.regionWidget){
                        var regionWidget = rave.getWidget(scope.regionWidget.id);

                        //Render the widget
                        regionWidget.render(element[0]);
                    }
                }, true);
            }
        }
        return directive;
    }]
})

