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
rave.store = rave.store || (function() {
    
    function initRatings() {
        $(".ratingButtons").buttonset();

        $(".widgetLikeButton").button( {
            icons: {primary: "ui-icon-plus"}
        }).change(function() {
            var widgetId = this.id.substring("like-".length);
            rave.api.rest.updateWidgetRating({widgetId: widgetId, score: 10});
            $(this).button("option", "label", parseInt($(this).button("option", "label")) + 1);

            //if the other button in this pair was checked then ajdust its total, except in IE where
            //the button has already toggled BEFORE the 'change' event in which case we have to assume
            //that the user had a contrary selection prior to the change event
            var dislikeButton = $("#dislike-"+widgetId);
            if (dislikeButton.get(0).getAttribute("checked") == "true" || this.checked == true) {
                dislikeButton.get(0).setAttribute("checked", "false");
                var dislikes = parseInt(dislikeButton.button("option", "label")) - 1;
                if (dislikes > -1) {
                    $(dislikeButton).button("option", "label", dislikes);
                }
            }
            
            //flag this element as the currently checked one
            this.setAttribute("checked", "true");

        });

        $(".widgetDislikeButton").button( {
            icons: {primary: "ui-icon-minus"}
        }).change(function() {
            var widgetId = this.id.substring("dislike-".length);
            rave.api.rest.updateWidgetRating({widgetId: widgetId, score: 0});
            $(this).button("option", "label", parseInt($(this).button("option", "label")) + 1);

            //if the other button in this pair was checked then ajdust its total, except in IE where
            //the button has already toggled BEFORE the 'change' event in which case we have to assume
            //that the user had a contrary selection prior to the change event
            var likeButton = $("#like-"+widgetId);
            if (likeButton.get(0).getAttribute("checked") == "true" || this.checked == true) {
                likeButton.get(0).setAttribute("checked", "false");
                var likes = parseInt(likeButton.button("option", "label")) - 1;
                if (likes > -1) {
                    $("#like-"+widgetId).button("option", "label", likes);
                }
            }
            
            //flag this element as the currently checked item
            this.setAttribute("checked", "true");
        });
    }
    
    function initComments() {
        
        $(".commentNewButton").button( {
            icons: {primary: "ui-icon-disk"},
            text: false
        }).click(function() {
            var widgetId = this.id.substring("comment-new-".length);
            rave.api.rest.createWidgetComment({widgetId: widgetId, 
                                                text: $("#newComment-"+widgetId).get(0).value,
                                                successCallback: function() { window.location.reload(); }});
        })
        
        $(".commentDeleteButton").button( {
            icons: {primary: "ui-icon-close"},
            text: false
        }).click(function() {
            var commentId = this.id.substring("comment-delete-".length);
            var widgetId = this.parentNode.id.substring("comment-widget-".length);
            rave.api.rest.deleteWidgetComment({widgetId: widgetId, 
                                                commentId: commentId,
                                                successCallback: function() { window.location.reload(); }});
        });
        
        $(".commentEditButton").button( {
            icons: {primary: "ui-icon-pencil"},
            text: false
        }).click(function() {
            var commentId = this.id.substring("comment-edit-".length);
            var widgetId = this.parentNode.id.substring("comment-widget-".length);
            $("#editComment-dialog").dialog({
               autoOpen: true,
               height: 150,
               width: 350,
               modal: true,
               buttons: {
                   "Update" : function() {
                       rave.api.rest.updateWidgetComment({widgetId: widgetId,
                                                            commentId: commentId,
                                                            text: $("#editComment").get(0).value,
                                                            successCallback: function() { window.location.reload(); }
                                                        });
                   }
               }
            });
        });
    }
    
    return {
        init: initRatings,
        initComments: initComments
    };
    
}());