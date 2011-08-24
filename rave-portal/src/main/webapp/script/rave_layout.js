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
    var $tab_title_input = $("#tab_title"),
        $page_layout_input = $("#pageLayout");
        
    // modal dialog init: custom buttons and a "close" callback reseting the form inside
    var $dialog = $( "#dialog" ).dialog({
            autoOpen: false,
            modal: true,
            buttons: {
                    Add: function() {
                            addPage();                            
                    },
                    Cancel: function() {
                            $( this ).dialog( "close" );
                    }
            },
            open: function() {
                    $tab_title_input.focus();
            },
            close: function() {
                    $form[ 0 ].reset();
                    $("#pageFormErrors").html("");
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
                alert("Edit not yet implemented!");
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

            // setup the edit page menu item
            $menuItemMove.bind('click', function(event) {
                alert("Move not yet implemented!");
                pageMenu.hide();
                // prevent the menu button click event from bubbling up to parent 
                // DOM object event handlers such as the page tab click event
                event.stopPropagation();
            });       

            // close the page menu if the user clicks outside of it           
            $("html").click(pageMenu.hide);
        }
        
        return {
            init: init,
            hide: hideMenu,
            show: showMenu
        }
    })();

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
                                      rave.viewPage(result.result.id); 
                                  } 
            });      
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
    }
   
    // public rave.layout API
    return {
        init: init,
        getCurrentPageId: getCurrentPageId
    };    
})();