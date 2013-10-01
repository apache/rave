<%--
  Licensed to the Apache Software Foundation (ASF) under one
  or more contributor license agreements.  See the NOTICE file
  distributed with this work for additional information
  regarding copyright ownership.  The ASF licenses this file
  to you under the Apache License, Version 2.0 (the
  "License"); you may not use this file except in compliance
  with the License.  You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing,
  software distributed under the License is distributed on an
  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
  KIND, either express or implied.  See the License for the
  specific language governing permissions and limitations
  under the License.
  --%>
<%@ page language="java" trimDirectiveWhitespaces="true" %>
<%@ include file="/WEB-INF/jsp/includes/taglibs.jsp" %>
<fmt:setBundle basename="messages"/>
<rave:navbar pageTitle="${widget.title}"/>

<div id="na_content" class="container">
<div class="row detail-widget">
<div class="span4">
    <div class="detail-widget-preview">
        <c:if test="${not empty widget.screenshotUrl}">
            <div class="detailWidgetScreenshot">
                <img src="${widget.screenshotUrl}"
                     alt="<fmt:message key="page.general.screenshot"/>"
                     title="<c:out value="${widget.title}"/> <fmt:message key="page.general.screenshot"/>"/>
            </div>
        </c:if>
        <c:if test="${not empty widget.thumbnailUrl}">
            <div class="detailWidgetThumbnail">
                <img src="<c:out value="${widget.thumbnailUrl}"/>" title="<c:out value="${widget.title}"/>"
                     alt="<fmt:message key="page.general.thumbnail"/>"/>
            </div>
        </c:if>
        <c:choose>
            <c:when test="${widget.widgetStatus eq 'PUBLISHED'}">
                <div id="widgetAdded_${widget.id}" class="detailWidgetAdd">
                    <button class="btn btn-primary btn-large storeItemButton widgetJspAddWidgetButton"
                            id="addWidget_${widget.id}"
                            data-widget-id="${widget.id}" data-page-id="${referringPageId}"
                            data-success="<fmt:message key="page.widget.addedToPage"/>">
                        <fmt:message key="page.widget.addToPage"/>
                    </button>
                </div>
            </c:when>
            <c:when test="${widget.widgetStatus eq 'PREVIEW'}">
                <div class="alert-message info">
                    <p><fmt:message key="widget.widgetStatus.PREVIEW"/></p>
                </div>
            </c:when>
        </c:choose>
    </div>
</div>
<div class="span7 detail-widget-main">
    <div class="row-fluid">
        <h2>
            <c:set var="widgetHasTitleUrl" value="${not empty widget.titleUrl}"/>
            <c:if test="${widgetHasTitleUrl}"><a href="<c:out value="${widget.titleUrl}"/>" rel="external">
            </c:if>
            <span id="widget-${widget.id}-title"><c:out value="${widget.title}"/></span>
            <c:if test="${widgetHasTitleUrl}"></a></c:if>
        </h2>
        <c:if test="${widget.disableRendering}">
            <div class="storeWidgetDisabled">
                        <span class="widget-disabled-icon-store ui-icon ui-icon-alert"
                              title="<fmt:message key="widget.chrome.disabled"/>"></span>
                <c:out value="${widget.disableRenderingMessage}" escapeXml="true"/>
            </div>
        </c:if>
        <c:if test="${not empty widget.author}">
            <p class="storeWidgetAuthor">
                <fmt:message key="widget.author"/>
                <c:out value=" "/><%-- intentional empty String in the c:out --%>
                <c:choose>
                    <c:when test="${not empty widget.authorEmail}">
                        <a href="mailto:<c:out value="${widget.authorEmail}"/>"><c:out
                                value="${widget.author}"/></a>
                    </c:when>
                    <c:otherwise><c:out value="${widget.author}"/></c:otherwise>
                </c:choose>
            </p>
        </c:if>

        <c:if test="${not empty widget.description}">
            <p class="storeWidgetDesc"><c:out value="${widget.description}"/></p>
        </c:if>
    </div>
    <div class="row-fluid">
        <div class="widgetRating" id="widgetRatings">
            <h3><fmt:message key="page.widget.rate"/></h3>
            <form class="hidden">
                <input type="hidden" id="rate-${widget.id}"
                       value="${widgetsStatistics[widget.id]!=null?widgetsStatistics[widget.id].userRating:"-1"}">
            </form>
            <div class="ratingCounts">
                		<span class="widgetLikeCount">
                            <c:set var="widgetLikes">
                                ${widgetsStatistics[widget.id]!=null?widgetsStatistics[widget.id].totalLike:"0"}
                            </c:set>
                            <span id="totalLikes-${widget.id}" data-rave-widget-likes="${widgetLikes}">
                                <span class="like-text">${widgetLikes}</span>
                            </span>
		                    <i class="icon-thumbs-up" title="${widgetLikes}&nbsp;<fmt:message key="page.widget.rate.likes"/>"></i>
                		</span>
                    	 <span class="widgetDislikeCount">
                            <c:set var="widgetDislikes">
                                ${widgetsStatistics[widget.id]!=null?widgetsStatistics[widget.id].totalDislike:"0"}
                            </c:set>

                            <span id="totalDislikes-${widget.id}" data-rave-widget-dislikes="${widgetDislikes}">
                                <span class="dislike-text">${widgetDislikes}</span>
                            </span>
		                    <i class="icon-thumbs-down" title="${widgetDislikes}&nbsp;<fmt:message key="page.widget.rate.dislikes"/>"></i>
                        </span>
            </div>
            <div id="rating-${widget.id}"  data-toggle="buttons-radio">
                <button id="like-${widget.id}" class="widgetLikeButton btn btn-mini ${widgetsStatistics[widget.id].userRating==10? 'active btn-success':''}"
                ${widgetsStatistics[widget.id].userRating==10 ? " checked='true'":""} name="rating-${widget.id}">
                    <fmt:message key="page.widget.rate.likebtn"/>
                </button>

                <button id="dislike-${widget.id}" class="widgetDislikeButton btn btn-mini ${widgetsStatistics[widget.id].userRating==0? 'active btn-danger':''}"
                ${widgetsStatistics[widget.id].userRating==0 ? " checked='true'":""} name="rating-${widget.id}">
                    <fmt:message key="page.widget.rate.dislikebtn"/>
                </button>
                <!-- Displaying the likes and dislikes rating along with total votes -->
            </div>
        </div>
    </div>
    <div class="row-fluid">
        <div class="detail-widget-users">
            <c:set var="widgetUserCountGreaterThanZero"
                   value="${widgetStatistics != null && widgetStatistics.totalUserCount > 0}"/>
            <c:if test="${widgetUserCountGreaterThanZero}"><a href="javascript:void(0);" id="displayUsersOfWidgetLink"
                                                              data-widget-id="${widget.id}"></c:if>
            <fmt:formatNumber groupingUsed="true" value="${widgetStatistics.totalUserCount}"/>&nbsp;<fmt:message
                    key="page.widget.usercount"/>
            <c:if test="${widgetUserCountGreaterThanZero}"></a></c:if>
        </div>
    </div>

    <div class="row-fluid">
        <%--//Tag section--%>
        <div class="widgetTags">
            <c:if test="${not empty widget.tags}">
                <h3><fmt:message key="page.widget.tags.title"/></h3>
                <div class="detail-widget-tags">
                    <c:forEach var="widgettag" items="${widget.tags}">
                        <c:forEach var="tag" items="${tags}">
                            <c:set var="tagMatched">
                                ${tag.id==widgettag.tagId?true:false}
                            </c:set>
                            <c:if test="${tagMatched}">
                                <span class="label"><c:out value="${tag.keyword}"/></span>
                            </c:if>
                        </c:forEach>
                    </c:forEach>
                </div>
            </c:if>
            <div id="tagInput" class="form-inline hide">
                <label for="tags"><fmt:message key="page.widget.tags.add"/> </label>
                <input type="text" id="tags" data-provide="typeahead" />
                <button id="tag-new-${widget.id}" class="btn tagNewButton" title="Add Tag">
                    <i class="icon-tag"></i>
                </button>
            </div>
            <a href="#tagInput" data-toggle="basic-slide" data-toggle-text="Hide tag form">Add tags <i class="icon-arrow-right"></i></a>
        </div>
    </div>
    <c:if test="${not empty widget.categories}">
        <div class="row-fluid">
            <div class="widgetCategories">
                <h3><fmt:message key="widget.categories"/></h3>
                <div id="categoriesRow">
                    <div>
                        <c:forEach var="category" items="${widget.categories}">
                            <span class="storeWidgetDesc"><c:out value="${category.text}"/></span>
                        </c:forEach>
                    </div>
                </div>
            </div>
        </div>
    </c:if>
    <div class="row-fluid">
        <div class="widgetComments" id="widgetComments">
            <h3><fmt:message key="page.widget.comments"/></h3>
            <div class="new-comment form-inline">
                <div class="row-fluid">
                    <div class="span12">
                        <textarea id="newComment-${widget.id}"></textarea>&nbsp;
                        <button id="comment-new-${widget.id}" class="btn commentNewButton" title="Add Comment"><i class="icon-comment"></i></button>
                    </div>
                </div>
            </div>
            <c:if test="${not empty widget.comments}">
                <ul class="comments">
                    <c:forEach var="comment" items="${widget.comments}">
                        <portal:person id="${comment.userId}" var="commenter" />
                        <li class="comment">
                            <fmt:formatDate value="${comment.createdDate}" type="both" var="commentDate"/>
                            <p class="comment-heading">
                                        <span class="commenter">
                                            <c:choose>
                                                <c:when test="${not empty commenter.username}">
                                                    <c:out value="${commenter.username}"/>
                                                </c:when>
                                                <c:otherwise><c:out value="${comment.userId}"/></c:otherwise>
                                            </c:choose>
                                        </span>
                                        <span class="comment-date">
                                            <c:out value=" - ${commentDate} "/>
                                        </span>
                                <c:if test="${userProfile.id eq comment.userId}">
                                    <button id="comment-delete-${comment.id}" class="btn btn-danger btn-mini commentDeleteButton"
                                            value="Delete" title="Delete comment" data-widgetid="<c:out value="${widget.id}"/>">
                                        <i class="icon-remove icon-white"></i>
                                    </button>
                                    <button id="comment-edit-${comment.id}" class="btn btn-mini commentEditButton"
                                            value="Edit" title="Edit comment"
                                            data-widgetid="<c:out value="${widget.id}"/>"
                                            data-toggle="modal" data-target="#editComment-dialog">
                                        <i class="icon-pencil"></i>
                                    </button>
                                </c:if>
                            </p>
                            <p class="commentText"><c:out value="${comment.text}"/></p>

                        </li>
                    </c:forEach>
                </ul>
            </c:if>
        </div>
    </div>
</div>
</div>
</div>
<!--@Atom Group: replace with twitter modal-->
<fmt:message key="page.widget.comment.edit" var="editCommentTitle"/>
<div id="editComment-dialog" title="<c:out value="${editCommentTitle}"/>" class="modal hide">
    <div class="modal-header">
        <a class="close" data-dismiss="modal"><i class="icon-remove"></i></a>
        <h3><c:out value="${editCommentTitle}"/></h3>
    </div>
    <div class="modal-body">
        <textarea id="editComment" rows="5"> </textarea>
    </div>
    <div class="modal-footer">
        <a href="#" class="btn" data-dismiss="modal">Cancel</a>
        <button id="updateComment" value="Update" class="btn btn-primary">Update</button>
        </a>
    </div>
</div>

<portal:register-init-script location="${'AFTER_RAVE'}">
    <script>
        require(["rave", "ui", "portal/rave_store", "jquery"], function(rave, ui, raveStore, $){
            //Helper function for callback below
            function addWidgetToPageCallback (result){
                var widgetTitle = ui.getClientMessage("widget.add_prefix");

                if (result != undefined && result.title != undefined && result.title.length > 0) {
                    widgetTitle = result.title;
                }
                ui.showInfoMessage(widgetTitle + ' ' + ui.getClientMessage("widget.add_suffix"));

                // Update Add Widget button to reflect status
                var addWidgetButton = "#addWidget_" + result.widgetId;
                var addedText = '<i class="icon icon-ok icon-white"></i> ' + $(addWidgetButton).data('success');

                $(addWidgetButton).removeClass("btn-primary").addClass("btn-success").html(addedText);
            }

            rave.registerOnInitHandler(function(){
                $('.widgetJspAddWidgetButton').click(function(){
                    var element = $(this);
                    var widgetId = element.data('widget-id');
                    var pageId = element.data('page-id');
                    rave.api.rpc.addWidgetToPage({widgetId: widgetId, pageId: pageId, redirectAfterAdd: true, successCallback: addWidgetToPageCallback})
                })

                $('#displayUsersOfWidgetLink').click(function(){
                    ui.displayUsersOfWidget($(this).data('widget-id'))
                })

                raveStore.init('<c:out value="${referringPageId}"/>');
                raveStore.initTags("<c:out value="${widget.id}"/>");
            })
        })
    </script>
</portal:register-init-script>
