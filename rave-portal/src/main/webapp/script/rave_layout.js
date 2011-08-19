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
        $page_layout_input = $("#pageLayout"),
        $tab_content_input = $("#tab_content");
        
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
                                  successCallback: function(result) {                                      
                                      rave.viewPage(result.result.id); 
                                  } 
            });      
        }
    }
    
    function deletePage(pageId) {
        // send the rpc request to delete the page
        rave.api.rest.deletePage({pageId: pageId, successCallback: rave.viewPage});  
    }
   
    function init() {
        // add_page button to open the dialog for creating a new page
        $( "#add_page" )
                .button()
                .click(function() {
                        $dialog.dialog( "open" );
                });

        // close icon: removing the tab on click
        // TODO - move this into a common page menu in the future along with edit page, etc
        $( "#tabs span.ui-icon-close" ).live( "click", function() {
                var pageId = this.parentNode.id.replace("tab-","");            
                deletePage(pageId);                       
        });    
    }
   
    // public rave.layout API
    return {
        init: init 
    };
    
})();