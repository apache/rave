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

//All set!

define(["jquery", "core/rave_api", "./rave_portal", "./rave_context"], function($, api, ravePortal, raveContext){
    function initRatings() {
        $('.ratingButtons').button();

        //Adjust width's
        $('.widgetRating').each(function(){
            var $likeBtn = $(this).find(".widgetLikeButton"),
                $likeCount = $(this).find(".widgetLikeCount"),
                $dislikeBtn = $(this).find(".widgetDislikeButton"),
                $dislikeCount = $(this).find(".widgetDislikeCount");

            if($likeBtn.outerWidth() >= $likeCount.outerWidth()){
                $likeCount.css( "width", $likeBtn.outerWidth() +"px" ); }
            else{
                $likeBtn.css( "width", $likeCount.outerWidth()  +"px" );
            }

            if($dislikeBtn.outerWidth() >= $dislikeCount.outerWidth()){
                $dislikeCount.css( "width", $dislikeBtn.outerWidth()  +"px" );
            }
            else{ $dislikeBtn.css( "width", $dislikeCount.outerWidth()  +"px"); }
        });

        $('.widgetLikeButton').click(function() {
            // If not already active
            if (!$(this).hasClass('active')){
                //retrieve widget id
                var widgetId = this.id.substring("like-".length);

                //update the widget score in database
                api.rest.updateWidgetRating({widgetId: widgetId, score: 10});

                //call update widget rating handler function
                var widgetRating = {
                    widgetId: widgetId,
                    widgetLikeButton: this,
                    widgetDislikeButton: $("#dislike-" + widgetId),
                    isLike: true
                };

                //update the widget ratings on web page
                api.handler.widgetRatingHandler(widgetRating);

                $(this).addClass('btn-success');
                $(this).siblings('.btn').removeClass('btn-danger');
            }
        });

        $('.widgetDislikeButton').click(function() {
            // If not already active
            if (!$(this).hasClass('active')){
                //retrieve widget id
                var widgetId = this.id.substring("dislike-".length);

                //update the widget score in database
                api.rest.updateWidgetRating({widgetId: widgetId, score: 0});

                //call update widget rating handler function
                var widgetRating = {
                    widgetId: widgetId,
                    widgetLikeButton: $("#like-" + widgetId),
                    widgetDislikeButton: this,
                    isLike: false
                };

                //update the widget ratings on web page
                api.handler.widgetRatingHandler(widgetRating);

                $(this).addClass('btn-danger');
                $(this).siblings('.btn').removeClass('btn-success');

            }
        });
    }

    function initComments() {

        $(".commentNewButton").button( {
            icons: {primary: "ui-icon-disk"},
            text: false
        }).click(function() {
                var widgetId = this.id.substring("comment-new-".length);
                api.rest.createWidgetComment({widgetId: widgetId,
                    text: $("#newComment-"+widgetId).get(0).value,
                    successCallback: function() { window.location.reload(); }});
            });

        $(".commentDeleteButton").button( {
            icons: {primary: "ui-icon-close"},
            text: false
        }).click(function() {
                var commentId = this.id.substring("comment-delete-".length);
                var widgetId = this.getAttribute('data-widgetid');
                api.rest.deleteWidgetComment({widgetId: widgetId,
                    commentId: commentId,
                    successCallback: function() { window.location.reload(); }});
            });

        $(".commentEditButton").click(function() {
            var commentId = this.id.substring("comment-edit-".length);
            var widgetId = this.getAttribute('data-widgetid');
            var commentText = $(this).parents(".comment").find(".commentText").text();
            $("#editComment").html(commentText);
            $("#editComment-dialog #updateComment").click( function(){
                api.rest.updateWidgetComment({widgetId: widgetId,
                    commentId: commentId,
                    text: $("#editComment").get(0).value,
                    successCallback: function() { window.location.reload(); }
                });
            }).html(ravePortal.getClientMessage("common.update"));
        });
    }

    function initTags(widgetId) {
        $('*[data-toggle="basic-slide"]').click(function(){
            var target;

            if($(this).attr("data-target")){
                target = $(this).attr("data-target");
                console.log($(this).attr("data-target"));
            }
            else{
                target = $(this).attr("href");
                console.log("else");
            }

            if($(this).attr('data-toggle-text')){
                var oldcontent = $(this).html();
                $(this).html($(this).attr('data-toggle-text'));
                $(this).attr('data-toggle-text',oldcontent);
            }
            $(target).slideToggle();
        });
        $(".tagNewButton").click(function () {
            var widgetId = this.id.substring("tag-new-".length);
            api.rest.createWidgetTag({widgetId:widgetId,
                text:$("#tags").get(0).value,
                successCallback:function () {
                    window.location.reload();
                }});
        });
        //    load the tag by widgetId
        api.rest.getTags({ widgetId:widgetId,
            successCallback:function (data) {
                var result = ($.map(data, function (tag) {
                    return {
                        label:tag.keyword,
                        value:tag.keyword
                    }
                }));
                $("#tags").typeahead({
                    source:result
                })
            }
        })
    }

    function initWidgetsTag(referringPageId) {
        if (referringPageId != null){
            // tag list box
            $("#tagList").change(function() {
                var selected = $("#tagList option:selected").text();
                selected=$.trim(selected)
                if (selected.length > 1) {
                    document.location.href = raveContext.getContext() + "store/tag?keyword=" + selected
                        +"&referringPageId="+referringPageId;
                }
            });
        }
    }

    function initWidgetsCategory(referringPageId) {
        if (referringPageId != null){
            // category list box
            $("#categoryList").change(function() {
                var selected = $("#categoryList option:selected").val();
                selected = parseInt(selected);
                if (!isNaN(selected) && selected != "0") {
                    document.location.href = raveContext.getContext() + "store/category?categoryId=" + selected
                        +"&referringPageId="+referringPageId;
                } else {
                    document.location.href = raveContext.getContext() + "store?referringPageId="+referringPageId;
                }
            });
        }
    }

    function initMarketplaceCategory(referringPageId) {
        if (referringPageId != null){
            // category list box
            $("#marketplaceCategoryList").change(function() {
                var selectedCategory = $("#marketplaceCategoryList option:selected").val();
                document.location.href = raveContext.getContext() + "marketplace/category/"+selectedCategory
                    +"?referringPageId="+referringPageId;
            });
        }
    }

    function confirmAddFromMarketplace(uri, pType){
        var answer = confirm(ravePortal.getClientMessage("confirm.add.from.marketplace"));
        if(answer){
            api.rpc.addWidgetFromMarketplace({
                url: uri,
                providerType: pType,
                successCallback: function(widget){
                    if(widget.result == null){
                        alert(ravePortal.getClientMessage("failed.add.from.marketplace"));
                    }else{
                        alert("(" + widget.result.title + ") " + ravePortal.getClientMessage("success.add.from.marketplace"));
                    }
                }
            });
        }
    }

    function init(referringPageId){
        initRatings();
        initComments();
        initWidgetsTag(referringPageId);
        initWidgetsCategory(referringPageId);
        initMarketplaceCategory(referringPageId);
    }

    return {
        init: init,
        initTags: initTags,
        confirmAddFromMarketplace : confirmAddFromMarketplace
    };
})

/*
var rave = rave || {};
rave.store = rave.store || (function() {

    function initRatings() {
        $('.ratingButtons').button();

        //Adjust width's 
        $('.widgetRating').each(function(){
            var $likeBtn = $(this).find(".widgetLikeButton"),
                $likeCount = $(this).find(".widgetLikeCount"),
                $dislikeBtn = $(this).find(".widgetDislikeButton"),
                $dislikeCount = $(this).find(".widgetDislikeCount");

            if($likeBtn.outerWidth() >= $likeCount.outerWidth()){
                $likeCount.css( "width", $likeBtn.outerWidth() +"px" ); }
            else{
                $likeBtn.css( "width", $likeCount.outerWidth()  +"px" );
            }

            if($dislikeBtn.outerWidth() >= $dislikeCount.outerWidth()){
                $dislikeCount.css( "width", $dislikeBtn.outerWidth()  +"px" );
            }
            else{ $dislikeBtn.css( "width", $dislikeCount.outerWidth()  +"px"); }
        });

        $('.widgetLikeButton').click(function() {
            // If not already active
            if (!$(this).hasClass('active')){
                //retrieve widget id
                var widgetId = this.id.substring("like-".length);

                //update the widget score in database
                rave.api.rest.updateWidgetRating({widgetId: widgetId, score: 10});

                //call update widget rating handler function
                var widgetRating = {
                    widgetId: widgetId,
                    widgetLikeButton: this,
                    widgetDislikeButton: $("#dislike-" + widgetId),
                    isLike: true
                };

                //update the widget ratings on web page
                rave.api.handler.widgetRatingHandler(widgetRating);

                $(this).addClass('btn-success');
                $(this).siblings('.btn').removeClass('btn-danger');
            }
        });

        $('.widgetDislikeButton').click(function() {
            // If not already active
            if (!$(this).hasClass('active')){
                //retrieve widget id
                var widgetId = this.id.substring("dislike-".length);

                //update the widget score in database
                rave.api.rest.updateWidgetRating({widgetId: widgetId, score: 0});

                //call update widget rating handler function
                var widgetRating = {
                    widgetId: widgetId,
                    widgetLikeButton: $("#like-" + widgetId),
                    widgetDislikeButton: this,
                    isLike: false
                };

                //update the widget ratings on web page
                rave.api.handler.widgetRatingHandler(widgetRating);

                $(this).addClass('btn-danger');
                $(this).siblings('.btn').removeClass('btn-success');

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

        $(".commentEditButton").click(function() {
            var commentId = this.id.substring("comment-edit-".length);
            var widgetId = this.getAttribute('data-widgetid');
            var commentText = $(this).parents(".comment").find(".commentText").text();
            $("#editComment").html(commentText);
            $("#editComment-dialog #updateComment").click( function(){
                rave.api.rest.updateWidgetComment({widgetId: widgetId,
                    commentId: commentId,
                    text: $("#editComment").get(0).value,
                    successCallback: function() { window.location.reload(); }
                });
            }).html(rave.getClientMessage("common.update"));
        });
    }

    function initTags(widgetId) {
        $('*[data-toggle="basic-slide"]').click(function(){
            var target;

            if($(this).attr("data-target")){
                target = $(this).attr("data-target");
                console.log($(this).attr("data-target"));
            }
            else{
                target = $(this).attr("href");
                console.log("else");
            }

            if($(this).attr('data-toggle-text')){
                var oldcontent = $(this).html();
                $(this).html($(this).attr('data-toggle-text'));
                $(this).attr('data-toggle-text',oldcontent);
            }
            $(target).slideToggle();
        });
        $(".tagNewButton").click(function () {
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
                $("#tags").typeahead({
                    source:result
                })
            }
        })
    }

    function initWidgetsTag(referringPageId) {
        if (referringPageId != null){
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
    }

    function initWidgetsCategory(referringPageId) {
        if (referringPageId != null){
            // category list box
            $("#categoryList").change(function() {
                var selected = $("#categoryList option:selected").val();
                selected = parseInt(selected);
                if (!isNaN(selected) && selected != "0") {
                    document.location.href = rave.getContext() + "store/category?categoryId=" + selected
                        +"&referringPageId="+referringPageId;
                } else {
                    document.location.href = rave.getContext() + "store?referringPageId="+referringPageId;
                }
            });
        }
    }

    function initMarketplaceCategory(referringPageId) {
        if (referringPageId != null){
            // category list box
            $("#marketplaceCategoryList").change(function() {
                var selectedCategory = $("#marketplaceCategoryList option:selected").val();
                document.location.href = rave.getContext() + "marketplace/category/"+selectedCategory
                    +"?referringPageId="+referringPageId;
            });
        }
    }

    function confirmAddFromMarketplace(uri, pType){
        var answer = confirm(rave.getClientMessage("confirm.add.from.marketplace"));
        if(answer){
            rave.api.rpc.addWidgetFromMarketplace({
                url: uri,
                providerType: pType,
                successCallback: function(widget){
                    if(widget.result == null){
                        alert(rave.getClientMessage("failed.add.from.marketplace"));
                    }else{
                        alert("(" + widget.result.title + ") " + rave.getClientMessage("success.add.from.marketplace"));
                    }
                }
            });
        }
    }

    function init(referringPageId){
        initRatings();
        initComments();
        initWidgetsTag(referringPageId);
        initWidgetsCategory(referringPageId);
        initMarketplaceCategory(referringPageId);
    }

    return {
        init: init,
        initTags: initTags,
        confirmAddFromMarketplace : confirmAddFromMarketplace
    };

}());*/
