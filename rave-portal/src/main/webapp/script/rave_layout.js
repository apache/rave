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
    					addTab();
    					$( this ).dialog( "close" );
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
    			}
    		});

    		// addTab form: calls addTab function on submit and closes the dialog
    		var $form = $( "form", $dialog ).submit(function() {
    			addTab();
    			$dialog.dialog( "close" );
    			return false;
    		});

    		// actual addTab function: adds new tab using the title input from the form above
    		function addTab() {
    			var tab_title = $tab_title_input.val() || "Tab " + tab_counter;
    			$tabs.tabs( "add", "#tabs-" + tab_counter, tab_title );
    			tab_counter++;
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