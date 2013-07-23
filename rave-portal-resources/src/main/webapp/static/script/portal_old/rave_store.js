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

define(["jquery", "rave", "portal/rave_portal"], function($, rave, ravePortal){
    function initRatings() {

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
                widgetRatingHandler(widgetRating);

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
                widgetRatingHandler(widgetRating);

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
            }).html(ravePortal.getClientMessage("common.update"));
        });
    }

    function initTags(widgetId) {
        $('*[data-toggle="basic-slide"]').click(function(){
            var target;

            if($(this).attr("data-target")){
                target = $(this).attr("data-target");
            }
            else{
                target = $(this).attr("href");
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
        var answer = confirm(ravePortal.getClientMessage("confirm.add.from.marketplace"));
        if(answer){
            rave.api.rpc.addWidgetFromMarketplace({
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

    function widgetRatingHandler(widgetRating) {

        //retrieving the current total likes
        var likeTotalLabel = document.getElementById("totalLikes-" + widgetRating.widgetId);
        var likeTotal = likeTotalLabel.getAttribute("data-rave-widget-likes");

        //retrieving the current total dislikes
        var dislikeTotalLabel = document.getElementById("totalDislikes-" + widgetRating.widgetId);
        var dislikeTotal = dislikeTotalLabel.getAttribute("data-rave-widget-dislikes");

        //initializing temporary variables
        var incrementingTotal = -1;
        var incrementingTotalLabel;
        var decrementingTotal = -1;
        var decrementingTotalLabel;
        var curButton = "";
        var prevButton = "";
        var prevRating = -1;

        //check if like rating needs to be updated
        if (widgetRating.isLike) {

            //set incrementing total to like total
            incrementingTotal = likeTotal;

            //set the incrementing total label to like total label
            incrementingTotalLabel = likeTotalLabel;

            //set decrementing total to dislike total
            decrementingTotal = dislikeTotal;

            //set the decrementing total label to dislike total label
            decrementingTotalLabel = dislikeTotalLabel;

            //set the current clicked button to like button
            curButton = widgetRating.widgetLikeButton;

            //set the previous clicked button to dislike button
            prevButton = widgetRating.widgetDislikeButton;

            //set the previous rating to 0 to check if dislike was clicked earlier
            prevRating = 0;
        }

        //check if dislike rating needs to be updated
        else {

            //set incrementing total to dislike total
            incrementingTotal = dislikeTotal;

            //set the incrementing total label to dislike total label
            incrementingTotalLabel = dislikeTotalLabel;

            //set decrementing total to like total
            decrementingTotal = likeTotal;

            //set the decrementing total label to like total label
            decrementingTotalLabel = likeTotalLabel;

            //set the current clicked button to dislike button
            curButton = widgetRating.widgetDislikeButton;

            //set the previous clicked button to like button
            prevButton = widgetRating.widgetLikeButton;

            //set the previous rating to 10 to check if like was clicked earlier
            prevRating = 10;
        }

        //update incrementing total
        incrementingTotal = parseInt(incrementingTotal) + 1;
        if (incrementingTotalLabel == likeTotalLabel) {
            incrementingTotalLabel.setAttribute("data-rave-widget-likes", incrementingTotal);
            incrementingTotalLabel.innerHTML = incrementingTotal;
        }
        else {
            incrementingTotalLabel.setAttribute("data-rave-widget-dislikes", incrementingTotal);
            incrementingTotalLabel.innerHTML = incrementingTotal;
        }

        //get the value of hidden user rating
        var hiddenButton = document.getElementById("rate-" + widgetRating.widgetId);
        var userPrevRate = hiddenButton.value;

        //if the other button in this pair was checked then ajdust its total, except in IE where
        //the button has already toggled BEFORE the 'change' event in which case we have to assume
        //that the user had a contrary selection prior to the change event
        if (prevButton.get(0).getAttribute("checked") == "true" || curButton.checked == true) {
            prevButton.get(0).setAttribute("checked", "false");

            //remove the previous rating made by the user if any by checking change in userRating
            if (parseInt(userPrevRate) == prevRating) {

                //update decrementing total
                if (parseInt(decrementingTotal) - 1 > -1) {
                    decrementingTotal = parseInt(decrementingTotal) - 1;
                    if (decrementingTotalLabel == likeTotalLabel) {
                        decrementingTotalLabel.setAttribute("data-rave-widget-likes", decrementingTotal);
                        decrementingTotalLabel.innerHTML = decrementingTotal;
                    }
                    else {
                        decrementingTotalLabel.setAttribute("data-rave-widget-dislikes", decrementingTotal);
                        decrementingTotalLabel.innerHTML = decrementingTotal;
                    }
                }
            }

        }

        //flag this element as the currently checked one
        curButton.setAttribute("checked", "true");

        //set the user rating of the hidden field
        if (widgetRating.isLike) {
            hiddenButton.value = "10";
        }
        else {
            hiddenButton.value = "0";
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