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

var rave = rave || {};
rave.layout = rave.layout || (function() {
    var MOVE_PAGE_DEFAULT_POSITION_IDX = -1;
    
    var $tab_title_input = $("#tab_title"),
        $tab_id = $("#tab_id"),
        $page_layout_input = $("#pageLayout");
        
    // modal dialog init: custom buttons and a "close" callback reseting the form inside
    var $dialog = $( "#dialog" ).dialog({
            autoOpen: false,
            modal: true,
            buttons: {
                    Add: function() {
                            addPage();                            
                    },

                    Update: function() {
                            updatePage();
                    },

                    Cancel: function() {
                            $( this ).dialog( "close" );
                    }
            },
            open: function(event, ui) {
                    if ($tab_id.val() == '') {
                        $("#"+this.id).dialog("option", "title", "Add a New Page");
                        $(":button:contains('Update')").hide();
                        $(":button:contains('Add')").show();
                    }
                    else {
                        $("#"+this.id).dialog("option", "title", "Update Page");
                        $(":button:contains('Update')").show();
                        $(":button:contains('Add')").hide();
                    }
                    $tab_title_input.focus();
            },
            close: function() {
                    $form[ 0 ].reset();
                    $tab_id.val('');
                    $("#pageFormErrors").html("");
            }
    });

    // the modal dialog for moving a page
    var $movePageDialog = $("#movePageDialog").dialog({
            autoOpen: false,
            modal: true,
            buttons: {
                    Move: function() {
                            movePage();                            
                    },
                    Cancel: function() {
                            $( this ).dialog( "close" );
                    }
            },
            open: function() {
                    $("#moveAfterPageId").focus();
            },
            close: function() {
                    $("#movePageForm")[0].reset();                    
            }
    });        
   
    // define the form object    
    var $form = $("#pageForm", $dialog);  
    
    // page menu related functions
    var pageMenu = (function() {
        var $button = $("#pageMenuButton");
        var $menu = $("#pageMenu");
        var $menuItemEdit = $("#pageMenuEdit");
        var $menuItemDelete = $("#pageMenuDelete");
        var $menuItemMove = $("#pageMenuMove");
                        
        function hideMenu() {
            $menu.hide();
        }
        
        function showMenu() {
            $menu.show();
        }
        
        /**
         * Initializes the private pageMenu closure
         * - binds click event handler to menu button
         * - binds menu item click event handlers
         * - binds body click event handler to close the menu
         */
        function init() {
            // initialize the page menu and button
            $button.bind('click', function(event) {  
                $menu.toggle();
                // prevent the menu button click event from bubbling up to parent 
                // DOM object event handlers such as the page tab click event
                event.stopPropagation();
            });         

            // setup the edit page menu item
            $menuItemEdit.bind('click', function(event) {
                 rave.api.rpc.getPagePrefs({pageId: getCurrentPageId(),
                                        successCallback: function(result) {
                                            $tab_title_input.val(result.result.name);
                                            $tab_id.val(result.result.entityId);
                                            $page_layout_input.val(result.result.pageLayout.code);
                                            $dialog.dialog( "open" );
                                        }
                });

                pageMenu.hide();
                // prevent the menu button click event from bubbling up to parent 
                // DOM object event handlers such as the page tab click event
                event.stopPropagation();
            });

            // setup the delete page menu item if it is not disabled
            if (!$menuItemDelete.hasClass("page-menu-item-disabled")) {                            
                $menuItemDelete.bind('click', function(event) {
                    // send the rpc request to delete the page
                    rave.api.rest.deletePage({pageId: getCurrentPageId(), successCallback: rave.viewPage});  
                    pageMenu.hide();
                    // prevent the menu button click event from bubbling up to parent 
                    // DOM object event handlers such as the page tab click event
                    event.stopPropagation();
                });
            }

            // setup the edit page menu item if it is not disabled
            if (!$menuItemDelete.hasClass("page-menu-item-disabled")) {
                $menuItemMove.bind('click', function(event) {
                    pageMenu.hide();                 
                    $movePageDialog.dialog("open");
                    // prevent the menu button click event from bubbling up to parent 
                    // DOM object event handlers such as the page tab click event
                    event.stopPropagation();
                });      
            }

            // close the page menu if the user clicks outside of it           
            $("html").click(pageMenu.hide);
        }
        
        return {
            init: init,
            hide: hideMenu,
            show: showMenu
        }
    })();


    // the modal dialog for moving a widget
    var $moveWidgetDialog = $("#moveWidgetDialog").dialog({
            autoOpen: false,
            modal: true,
            buttons: {
                    Move: function() {
                            moveWidgetToPage($(this).data('regionWidgetId'));
                    },
                    Cancel: function() {
                            $( this ).dialog( "close" );
                    }
            },
            open: function() {
                    $("#moveToPageId").focus();
            },
            close: function() {
                    $("#moveWidgetForm")[0].reset();
            }
    });


    // widget menu related functions
    var widgetMenu = (function() {
        var $menu ;
        var $menuItemMove ;

        function hideMenu() {
            $(".widget-menu-button").each(function(index, element) {
                if (!$(element).hasClass("widget-menu-item-disabled")) {
                    $menu = $("#widget-" + rave.getObjectIdFromDomId(this.id) + "-menu");
                    $menu.hide();
                }
            });
        }

        function showMenu() {
            $menu.show();
        }

        /**
         * Initializes the private widgetMenu closure
         * - binds click event handler to menu button
         * - binds menu item click event handlers
         * - binds body click event handler to close the menu
         */
        function init() {
            // initialize the widget menu and button
            $(".widget-menu-button").each(function(index, element) {
                if (!$(element).hasClass("widget-menu-item-disabled")) {
                    $(element).bind('click', function(event) {
                        $menu = $("#widget-" + rave.getObjectIdFromDomId(this.id) + "-menu");                        
                        $menu.toggle();
                        // prevent the menu button click event from bubbling up to parent
                        // DOM object event handlers such as the page tab click event
                        event.stopPropagation();
                    });
                }
            });

            $(".widget-menu-item").each(function(index, element) {
                if (!$(element).hasClass("widget-menu-item-disabled")) {
                    $(element).bind('click', function(event) {
                        $menu = $("#widget-" + rave.getObjectIdFromDomId(this.id) + "-menu");
                        $menuItemMove = $("#widget-" + rave.getObjectIdFromDomId(this.id) + "-menu-move-item") ;
                        $moveWidgetDialog
                            .data('regionWidgetId', rave.getObjectIdFromDomId(this.id))
                            .dialog("open");
                        // prevent the menu button click event from bubbling up to parent
                        // DOM object event handlers such as the page tab click event
                        event.stopPropagation();
                    });
                }
            });

            // close the widget menu if the user clicks outside of it
            $("html").click(widgetMenu.hide);
        }

        return {
            init: init,
            hide: hideMenu,
            show: showMenu
        }
    })();

    /**
     * Submits the RPC call to move the widget to a new page
     */
    function moveWidgetToPage(regionWidgetId) {
        var toPageId = $("#moveToPageId").val();
        var args = { toPageId: toPageId,
                     regionWidgetId: regionWidgetId,
                     successCallback: function() { rave.viewPage(toPageId); }
                   };
        // send the rpc request to move the widget to new page
        rave.api.rpc.moveWidgetToPage(args);
    }

    /**
     * Submits the RPC call to add a new page if form validation passes
     */        
    function addPage() {
        // if the form has passed validation submit the request
        if ($("#pageForm").valid()) {        
            var newPageTitle = $tab_title_input.val();
            var newPageLayoutCode = $page_layout_input.val();

            // send the rpc request to create the new page
            rave.api.rpc.addPage({pageName: newPageTitle, 
                                  pageLayoutCode: newPageLayoutCode,
                                  successCallback: function(result) {                                      
                                      rave.viewPage(result.result.entityId); 
                                  } 
            });      
        }
    }      

    /**
     * Submits the RPC call to move the page to a new render sequence
     */        
    function movePage() {
        var moveAfterPageId = $("#moveAfterPageId").val();
        var args = { pageId: $("#currentPageId").val(),
                     successCallback: function(result) { rave.viewPage(result.result.entityId); }
                   };
       
        if (moveAfterPageId != MOVE_PAGE_DEFAULT_POSITION_IDX) {
            args["moveAfterPageId"] = moveAfterPageId;
        }
                          
        // send the rpc request to move the new page
        rave.api.rpc.movePage(args);              
    }

    function updatePage() {
        if ($("#pageForm").valid()) {
            // send the rpc request to update the page
            rave.api.rpc.updatePagePrefs({pageId: $tab_id.val(),
                                            title: $tab_title_input.val(),
                                            layout: $page_layout_input.val(),
                                            successCallback: function(result) {
                                                rave.viewPage(result.result.entityId);
                                            }});
        }
    }
    
    /**
     * Returns the pageId of the currently viewed page
     */
    function getCurrentPageId() {
        return $("#currentPageId").val();
    }
       
   /***
    * initializes the rave.layout namespace code
    */
    function init() {
        // add_page button to open the dialog for creating a new page
        $( "#add_page" )
                .button({ 
                    text: false,
                    icons: {primary:'ui-icon-plusthick'} 
                })
                .click(function() {
                    $dialog.dialog( "open" );
                })
                .show();

        pageMenu.init();
        widgetMenu.init();
    }
   
    // public rave.layout API
    return {
        init: init,
        getCurrentPageId: getCurrentPageId
    };    
})();
