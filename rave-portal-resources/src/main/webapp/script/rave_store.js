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
        }).click(function() {
        	
        	//check if like radio button is not checked already
        	if(this.getAttribute("checked") != "true") {
        		
        		//retrieve widget id
        		var widgetId = this.id.substring("like-".length);
        		
        		//update the widget score in database
        		rave.api.rest.updateWidgetRating({widgetId: widgetId, score: 10});
        		
        		//call update widget rating handler function
        		var widgetRating = {
        				widgetId: widgetId, 
					    widgetLikeButton: this, 
					    widgetDislikeButton: $("#dislike-"+widgetId),
						isLike: true
        		};
        		
        		//update the widget ratings on web page
        		rave.api.handler.widgetRatingHandler(widgetRating);
            	
        	}
        });

        $(".widgetDislikeButton").button( {
            icons: {primary: "ui-icon-minus"}
        }).click(function() {
        	
        	//check if dislike radio button is not checked already
        	if(this.getAttribute("checked") != "true") {
            	
        		//retrieve widget id
        		var widgetId = this.id.substring("dislike-".length);
        		
        		//update the widget score in database
        		rave.api.rest.updateWidgetRating({widgetId: widgetId, score: 0});
        		
        		//call update widget rating handler function
        		var widgetRating = {
        				widgetId: widgetId, 
					    widgetLikeButton: $("#like-"+widgetId), 
					    widgetDislikeButton: this,
						isLike: false
        		};
        		
        		//update the widget ratings on web page
        		rave.api.handler.widgetRatingHandler(widgetRating);
        	}
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
        });
        
        $(".commentDeleteButton").button( {
            icons: {primary: "ui-icon-close"},
            text: false
        }).click(function() {
            var commentId = this.id.substring("comment-delete-".length);
            var widgetId = this.getAttribute('data-widgetid');
            rave.api.rest.deleteWidgetComment({widgetId: widgetId, 
                                                commentId: commentId,
                                                successCallback: function() { window.location.reload(); }});
        });
        
        $(".commentEditButton").button( {
            icons: {primary: "ui-icon-pencil"},
            text: false
        }).click(function() {
            var commentId = this.id.substring("comment-edit-".length);
            var widgetId = this.getAttribute('data-widgetid');
            var commentText = $(this).parent().find(".commentText").text();
            $("#editComment").text(commentText);
            $("#editComment-dialog").dialog({
               autoOpen: true,
               height: 150,
               width: 350,
               modal: true,
               buttons : [
                    {
                        text : rave.getClientMessage("common.update"),
                        click : function() {
                           rave.api.rest.updateWidgetComment({widgetId: widgetId,
                                                            commentId: commentId,
                                                            text: $("#editComment").get(0).value,
                                                            successCallback: function() { window.location.reload(); }
                                                        })
                        }
                    }
               ]
            });
        });
    }

    function initTags(widgetId) {

        $(".tagNewButton").button({
            icons:{primary:"ui-icon-disk"},
            text:false
        }).click(function () {
                var widgetId = this.id.substring("tag-new-".length);
                rave.api.rest.createWidgetTag({widgetId:widgetId,
                    text:$("#tags").get(0).value,
                    successCallback:function () {
                        window.location.reload();
                    }});
            });
//    load the tag by widgetId
        rave.api.rest.getTags({ widgetId:widgetId,
            successCallback:function (data) {
                var result = ($.map(data, function (tag) {
                    return {
                        label:tag.keyword,
                        value:tag.keyword
                    }
                }));

                $("#tags").autocomplete({
                    source:result
                })
            }
        })
    }

    function initWidgetsTag(referringPageId) {
           // tag list box
           $("#tagList").change(function() {
               var selected = $("#tagList option:selected").text();
               selected=$.trim(selected)
               if (selected.length > 1) {
                   document.location.href = rave.getContext() + "store/tag?keyword=" + selected
                           +"&referringPageId="+referringPageId;
               }
           });

       }

    return {
        init: initRatings,
        initComments: initComments,
        initTags: initTags,
        initWidgetsTag:initWidgetsTag
    };                             
    
}());