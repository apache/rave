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

define(["jquery"], function ($) {

    function datatableClick() {
        $('.datatable tr').bind('click', function () {
            var link = $(this).attr('data-detaillink');
            if (link != undefined && link != '') {
                window.location = link;
            }
        });
    }

    //Resize bootstrap modal & adjust margins to size of image.
    function resizeImageModal() {
        $('#thumbnailModal, #screenshotModal').on('shown', function () {
            if (!$(this).hasClass("sized")) {
                var imageWidth = $(this).find("img").width(),
                    imageHeight = $(this).find("img").height(),
                    footerHeight = $(this).find(".modal-footer").outerHeight(),
                    headerHeight = $(this).find(".modal-header").outerHeight(),
                    totalHeight = imageHeight + footerHeight + headerHeight;

                $(this).css({
                    width: imageWidth + "px",
                    'margin-top': "-" + Math.round(totalHeight / 2) + "px",
                    'margin-left': "-" + Math.round(imageWidth / 2) + "px"
                }).addClass("sized");
            }
        });
    }

    function init() {
        datatableClick();
        resizeImageModal();
    }

    return {
        init: init
    }
})