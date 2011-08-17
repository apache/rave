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
$(function() {
    var $tab_title_input = $( "#tab_title"),
        $page_layout_input = $("#pageLayout"),
        $tab_content_input = $( "#tab_content" );
        
    var tab_counter = 2;
    // tabs init with a custom tab template and an "add" callback filling in the content
    var $tabs = $( "#tabs").tabs({
            tabTemplate: "<li><a href='#{href}'>#{label}</a> <span class='ui-icon ui-icon-close'>Remove Tab</span></li>",
            add: function( event, ui ) {
                    var tab_content = $tab_content_input.val() || "Tab " + tab_counter + " content.";
                    $( ui.panel ).append( "<p>" + tab_content + "</p>" );
            }
    });

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

    function addPage() {
        // if the form has passed validation submit the request
        if ($("#pageForm").valid()) {        
            var newPageTitle = $tab_title_input.val();
            var newPageLayoutCode = $page_layout_input.val();

            // send the rpc request to create the new page
            rave.api.rpc.addPage({pageName: newPageTitle, 
                                  pageLayoutCode: newPageLayoutCode,
                                  successCallback: function() { addPageCallback(newPageTitle); } 
                                 });      
        }
    }
        
    function addPageCallback(newPageTitle) {
        // add the new page tab to the list of pages
        // TODO - in the future this should be changed to redirect to the new page after creating it
        var pageTitle = newPageTitle;
        $tabs.tabs("add", "#tabs-" + tab_counter, pageTitle);
        tab_counter++;
        
        $dialog.dialog( "close" );
    }

    // addTab button: just opens the dialog
    $( "#add_tab" )
            .button()
            .click(function() {
                    $dialog.dialog( "open" );
            });

    // close icon: removing the tab on click
    // note: closable tabs gonna be an option in the future - see http://dev.jqueryui.com/ticket/3924
    $( "#tabs span.ui-icon-close" ).live( "click", function() {
            var index = $( "li", $tabs ).index( $( this ).parent() );
            $tabs.tabs( "remove", index );
    });
});