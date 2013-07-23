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

define(["jquery", "rave", "portal/rave_portal", "portal/rave_models", "bootstrap", "jqueryValidate"],
    function ($, rave, ravePortal, raveModels) {
        var MOVE_PAGE_DEFAULT_POSITION_IDX = -1;
        var $moveWidgetDialog;
        var showImportExportUI = false;
        var $movePageDialog = $("#movePageDialog");
        var $pageFormTabbed = $("#pageFormTabbed");
        var $pageFormImport = $("#pageFormImport");
        var $omdlFile = $("#omdlFile");
        var $pageForm = $("#pageForm");
        var $tab_title_input = $("#tab_title"),
            $tab_id = $("#tab_id"),
            $page_layout_input = $("#pageLayout");

        // page menu related functions
        var pageMenu = (function () {
            var $addPageButton = $("#addPageButton");
            var $menuItemEdit = $("#pageMenuEdit");
            var $menuItemDelete = $("#pageMenuDelete");
            var $menuItemMove = $("#pageMenuMove");
            var $menuItemShare = $("#pageMenuShare");
            var $menuItemRevokeShare = $("#pageMenuRevokeShare");
            var $MenuItemExport = $("#pageMenuExport");

            /**
             * Initializes the private pageMenu closure
             * - binds click event handler to menu button
             * - binds menu item click event handlers
             * - binds body click event handler to close the menu
             */
            function init(showImportExport) {
                showImportExportUI = showImportExport;
                // initialize the page form validator overriding the
                // styles to wire in bootstrap styles
                $pageForm.validate({
                    errorElement: 'span',
                    errorClass: 'help-inline',
                    highlight: function (element, errorClass) {
                        $(element).parent().parent().addClass('error');
                    },
                    unhighlight: function (element, errorClass) {
                        $(element).parent().parent().removeClass('error');
                    }
                });
                $pageFormTabbed.validate({
                    errorElement: 'span',
                    errorClass: 'help-inline',
                    highlight: function (element, errorClass) {
                        $(element).parent().parent().addClass('error');
                    },
                    unhighlight: function (element, errorClass) {
                        $(element).parent().parent().removeClass('error');
                    }
                });
                $pageFormImport.validate({
                    errorElement: 'span',
                    errorClass: 'help-inline',
                    highlight: function (element, errorClass) {
                        $(element).parent().parent().addClass('error');
                    },
                    unhighlight: function (element, errorClass) {
                        $(element).parent().parent().removeClass('error');
                    }
                });


                // initialize the close button in the page menu dialog
                $("#pageMenuCloseButton").click(closePageDialog);
                $("#pageMenuCloseButtonTab").click(closePageDialogTabbed);
                if (showImportExport) {
                    $("#pageMenuExport").removeClass('hidden');
                }

                // setup the edit page menu item
                $addPageButton.bind('click', function (event) {
                    if (showImportExport) {
                        var $pageMenuUpdateButtonTabbed = $("#pageMenuUpdateButtonTab");
                        $pageMenuUpdateButtonTabbed.html(ravePortal.getClientMessage("common.add"));
                        $("#page-tabs").tabs();
                        $pageMenuUpdateButtonTabbed.click(addOrImportPage);
                        $('#pageMenuDialogTabbed').on('shown', function () {
                            $("#tab_title").first().focus();
                        });
                        $("#pageMenuDialogTabbed").modal('show');
                    } else {
                        $("#pageMenuDialogHeader").html(ravePortal.getClientMessage("page.add"));
                        var $pageMenuUpdateButton = $("#pageMenuUpdateButton");
                        $("#pageLayoutGroup").show();
                        $pageMenuUpdateButton.html(ravePortal.getClientMessage("common.add"));
                        // unbind the previous click event since we are sharing the
                        // dialog between separate add/edit page actions
                        $pageMenuUpdateButton.unbind('click');
                        $pageMenuUpdateButton.click(addPage);
                        $('#pageMenuDialog').on('shown', function () {
                            $("#tab_title").first().focus();
                        });
                        $("#pageMenuDialog").modal('show');
                    }
                });

                // setup the edit page menu item
                $menuItemEdit.bind('click', function (event) {
                    rave.api.rpc.getPagePrefs({pageId: getCurrentPageId(),
                        successCallback: function (result) {
                            $tab_title_input.val(result.result.name);
                            $tab_id.val(result.result.id);
                            $page_layout_input.val(result.result.pageLayout.code);
                            $("#pageMenuDialogHeader").html(ravePortal.getClientMessage("page.update"));
                            var $pageMenuUpdateButton = $("#pageMenuUpdateButton");
                            $("#pageLayoutGroup").show();
                            $pageMenuUpdateButton.html(ravePortal.getClientMessage("common.save"));
                            // unbind the previous click event since we are sharing the
                            // dialog between separate add/edit page actions
                            $pageMenuUpdateButton.unbind('click');
                            $pageMenuUpdateButton.click(updatePage);
                            $('#pageMenuDialog').on('shown', function () {
                                $("#tab_title").first().focus();
                            });
                            $("#pageMenuDialog").modal('show');
                        }
                    });
                });

                // setup the delete page menu item if it is not disabled
                if (!$menuItemDelete.hasClass("menu-item-disabled")) {
                    $menuItemDelete.bind('click', function (event) {
                        // send the rpc request to delete the page
                        rave.api.rest.deletePage({pageId: getCurrentPageId(), successCallback: ravePortal.viewPage});
                    });
                }

                // setup the move page menu item if it is not disabled
                if (!$menuItemMove.hasClass("menu-item-disabled")) {
                    $menuItemMove.bind('click', function (event) {
                        $movePageDialog.modal('show');
                    });
                }

                // setup the export page menu item if it is not disabled
                if (!$MenuItemExport.hasClass("hidden")) {
                    $MenuItemExport.bind('click', function (event) {
                        window.open(rave.getContext() + "api/rest/" + "page/" + getCurrentPageId() + "/omdl");
                    });
                }

                // setup the revoke share page menu item
                if (!$menuItemRevokeShare.hasClass("menu-item-disabled")) {
                    $menuItemRevokeShare.bind('click', function (event) {
                        raveModels.currentPage.removeForSelf();
                    });
                }
            }

            return {
                init: init
            }
        })();

        // functions associated with the user search for sharing pages



        /**
         * Submits the RPC call to move the widget to a new page
         */
        function moveWidgetToPage(regionWidgetId) {
            var toPageId = $("#moveToPageId").val();
            var args = { toPageId: toPageId,
                regionWidgetId: regionWidgetId,
                successCallback: function () {
                    ravePortal.viewPage(toPageId);
                }
            };
            // send the rpc request to move the widget to new page
            rave.api.rpc.moveWidgetToPage(args);
        }

        /**
         * Submits the RPC call to add a new page if form validation passes
         */
        function addPage() {
            if (showImportExportUI) {
                if ($pageFormTabbed.valid()) {
                    var newPageTitle = $("#tab_titleTabbed1").val();
                    var newPageLayoutCode = $("#pageLayoutTabbed").val();
                    // send the rpc request to create the new page
                    rave.api.rpc.addPage({pageName: newPageTitle,
                        pageLayoutCode: newPageLayoutCode,
                        errorLabel: 'pageFormErrorsTabbed1',
                        successCallback: function (result) {
                            ravePortal.viewPage(result.result.id);
                        },
                        errorCallback: function (errorLabel) {
                            $("#" + errorLabel).html(ravePortal.getClientMessage("page.duplicate_name"));
                        }
                    });
                }
            } else {
                if ($pageForm.valid()) {
                    var newPageTitle = $tab_title_input.val();
                    var newPageLayoutCode = $page_layout_input.val();
                    // send the rpc request to create the new page
                    rave.api.rpc.addPage({pageName: newPageTitle,
                        pageLayoutCode: newPageLayoutCode,
                        errorLabel: 'pageFormErrors',
                        successCallback: function (result) {
                            ravePortal.viewPage(result.result.id);
                        },
                        errorCallback: function (errorLabel) {
                            $("#" + errorLabel).html(ravePortal.getClientMessage("page.duplicate_name"));
                        }
                    });
                }
            }
        }

        function importPage() {
            if ($.browser.msie == true) {
                alert(ravePortal.getClientMessage("import.page.not.supported"));
            }
            else {
                if ($pageFormImport.valid()) {
                    $pageFormImport.get(0).setAttribute('action', rave.getContext() + "api/rpc/page/import/omdl");
                    document.getElementById('pageFormImport').onsubmit = function () {
                        document.getElementById('pageFormImport').target = 'file_upload_frame';
                        document.getElementById("file_upload_frame").onload = processFileUploadResult;
                    }
                    $pageFormImport.submit();
                }
            }
        }

        function processFileUploadResult() {
            // chrome & firefox a <pre> has been added to the begining of the json
            var ret = frames['file_upload_frame'].document.getElementsByTagName("body")[0].firstChild.innerHTML;
            var data = eval("(" + ret + ")");
            if (data.error) {
                if (data.errorCode == 'DUPLICATE_ITEM') {
                    $("#pageFormErrorsTabbed2").html(ravePortal.getClientMessage("page.duplicate_name"));
                }
                else if (data.errorCode == 'INTERNAL_ERROR' && data.errorMessage.indexOf("BadOmdlXmlFormatException") != -1) {
                    var msg = data.errorMessage.substr(data.errorMessage.indexOf("BadOmdlXmlFormatException"), data.errorMessage.length)
                    $("#pageFormErrorsTabbed2").html(msg);
                }
                else {
                    alert(ravePortal.getClientMessage("api.rpc.error.internal"));
                }
            }
            else {
                ravePortal.viewPage(data.result.id);
            }
        }

        function addOrImportPage() {
            var selectedTab = $("#page-tabs").tabs('option', 'selected');
            if (selectedTab == 0) {
                addPage();
            } else {
                importPage();
            }
        }

        /**
         * Submits the RPC call to move the page to a new render sequence
         */
        function movePage() {
            var moveAfterPageId = $("#moveAfterPageId").val();
            var args = { pageId: $("#currentPageId").val(),
                successCallback: function (result) {
                    ravePortal.viewPage(result.result.id);
                }
            };

            if (moveAfterPageId != MOVE_PAGE_DEFAULT_POSITION_IDX) {
                args["moveAfterPageId"] = moveAfterPageId;
            }

            // send the rpc request to move the new page
            rave.api.rpc.movePage(args);
        }

        function updatePage() {
            if ($pageForm.valid()) {
                // send the rpc request to update the page
                rave.api.rpc.updatePagePrefs({pageId: $tab_id.val(),
                    title: $tab_title_input.val(),
                    layout: $page_layout_input.val(),
                    errorLabel: 'pageFormErrors',
                    successCallback: function (result) {
                        ravePortal.viewPage(result.result.id);
                    },
                    errorCallback: function (errorLabel) {
                        $("#" + errorLabel).html(ravePortal.getClientMessage("page.duplicate_name"));
                    }
                });
            }
        }

        function closePageDialog() {
            $pageForm[0].reset();
            $tab_id.val('');
            $("#pageFormErrors").text('');
            $("#pageMenuDialog").modal("hide");
        }

        function closePageDialogTabbed() {
            $pageForm[0].reset();
            $("#pageFormErrors").text('');
            $("#pageFormErrorsTabbed1").text('');
            $("#pageFormErrorsTabbed2").text('');
            $pageFormTabbed[0].reset();
            $pageFormImport[0].reset();
            $("#pageMenuDialogTabbed").modal("hide");
        }

        /**
         * Returns the pageId of the currently viewed page
         */
        function getCurrentPageId() {
            return rave.getPage().id
        }

        /**
         * Determines if the widget should be rendered as part of the initial page view.  This can be used to help optimize
         * page loads as widgets located on non-active sub pages will not be rendered initially.  The function will return
         * false if one of the following criteria is met, and true otherwise:
         *
         * 1) the widget is on a top level page (not a sub page)
         * 2) the widget is on a sub page that is currently requested in the URL via the hash tag
         *    ie /app//person/canoniocal#My%20Activity
         * 3) the widget is on the default sub page and the page request URL doesn't contain a sub-page hash tag
         *    ie /app/person/canonical
         *
         * @return boolean
         */
        function isWidgetOnHiddenTab(widget) {
            var hash = decodeURIComponent(location.hash);
            var subPage = widget.subPage;
            return !(subPage.id === null ||
                ("#" + subPage.name) === hash ||
                (hash === "" && subPage.isDefault));
        }

        function init(){
            pageMenu.init(rave.getExportEnabled());
        }

        rave.registerOnInitHandler(init);

        // public rave.layout API
        return {
            addPage: addPage,
            addOrImportPage: addOrImportPage,
            updatePage: updatePage,
            movePage: movePage,
            importPage: importPage,
            closePageDialog: closePageDialog,
            closePageDialogTabbed: closePageDialogTabbed,
            moveWidgetToPage: moveWidgetToPage,
            isWidgetOnHiddenTab: isWidgetOnHiddenTab
        };
    })
