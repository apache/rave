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
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="rave" %>
<jsp:useBean id="widget" scope="request" class="org.apache.rave.portal.model.Widget"/>
<fmt:setBundle basename="messages"/>
<rave:rave_generic_page pageTitle="${widget.title}">
    <header>
        <nav class="topnav">
            <ul class="horizontal-list">
                <c:if test="${not empty referringPageId}">
                    <li>
                        <a href="<spring:url value="/app/store?referringPageId=${referringPageId}" />">
                            <fmt:message key="page.widget.backToStore"/>
                        </a>
                    </li>
                </c:if>
                <li>
                    <c:choose>
                        <c:when test="${empty referringPageId}">
                            <spring:url value="/index.html" var="gobackurl" />
                        </c:when>
                        <c:otherwise>
                            <spring:url value="/app/page/view/${referringPageId}" var="gobackurl"/>
                        </c:otherwise>
                    </c:choose>
                    <a href="<c:out value="${gobackurl}"/>"><fmt:message key="page.general.back"/></a>
                </li>
                <sec:authorize url="/app/admin/">
                    <li>
                        <a href="<spring:url value="/app/admin/"/>">
                            <fmt:message key="page.general.toadmininterface"/>
                        </a>
                    </li>
                </sec:authorize>
                <li>
                    <a href="<spring:url value="/j_spring_security_logout" htmlEscape="true" />">
                        <fmt:message key="page.general.logout"/></a>
                </li>
            </ul>
        </nav>
        <h1><c:out value="${widget.title}"/></h1>
    </header>


    <div id="content">
        <div id="widget-content">

            <div class="widgetDetailLeft">
                <c:if test="${not empty widget.thumbnailUrl}">
                    <img class="storeWidgetThumbnail"
                         src="<c:out value="${widget.thumbnailUrl}"/>"
                         title="<c:out value="${widget.title}"/>"
                         alt="<fmt:message key="page.general.thumbnail"/>"
                         width="120" height="60"/>
                </c:if>

                <c:choose>
                        <c:when test="${widget.widgetStatus eq 'PUBLISHED'}">
                            <div id="widgetAdded_${widget.entityId}" class="storeButton">
                                <button class="storeItemButton"
                                        id="addWidget_${widget.entityId}"
                                        onclick="rave.api.rpc.addWidgetToPage({widgetId: ${widget.entityId}, pageId: ${referringPageId}, redirectAfterAdd:true});">
                                    <fmt:message key="page.widget.addToPage"/>
                                </button>
                            </div>
                        </c:when>
                        <c:when test="${widget.widgetStatus eq 'PREVIEW'}">
                            <p class="warn"><fmt:message key="widget.widgetStatus.PREVIEW"/></p>
                        </c:when>
                    </c:choose>

            </div>

            <div class="widgetDetailCenter">

                <div class="storeWidgetDetail">
                    <span class="secondaryPageItemTitle">
                        <c:choose>
                            <c:when test="${not empty widget.titleUrl}">
                                <a href="<c:out value="${widget.titleUrl}"/>" rel="external"><c:out value="${widget.title}"/></a>
                            </c:when>
                            <c:otherwise>
                                <c:out value="${widget.title}"/>
                            </c:otherwise>
                        </c:choose>
                    </span>

                </div>


                <c:if test="${not empty widget.author}">
                    <p class="storeWidgetAuthor">
                        <fmt:message key="widget.author"/>
                        <c:out value=" "/><%-- intentional empty String in the c:out --%>
                        <c:choose>
                            <c:when test="${not empty widget.authorEmail}">
                                <a href="mailto:<c:out value="${widget.authorEmail}"/>"><c:out value="${widget.author}"/></a>
                            </c:when>
                            <c:otherwise><c:out value="${widget.author}"/></c:otherwise>
                        </c:choose>
                    </p>
                </c:if>

                <c:if test="${not empty widget.description}">
                    <p class="storeWidgetDesc"><c:out value="${widget.description}"/></p>
                </c:if>

                <div class="widgetRating">
                    <fmt:message key="page.widget.rate"/>
                    <div id="radio" class="ratingButtons">
                       <input type="radio" id="like-${widget.entityId}" class="widgetLikeButton widgetRatingButton" value="10" name="rating-${widget.entityId}"${widgetStatistics.userRating=='10'?" checked='true'":""}> <label for="like-${widget.entityId}">${widgetStatistics.totalLike}</label>
                       <input type="radio" id="dislike-${widget.entityId}" class="widgetDislikeButton widgetRatingButton" value="0" name="rating-${widget.entityId}"${widgetStatistics.userRating=='0'?" checked='true'":""}> <label for="dislike-${widget.entityId}">${widgetStatistics.totalDislike}</label>
                    </div>
                </div>

                <div class="widgetComments">
                    <div class="new-comment">
                        <h3>Comment</h3>
                        <textarea id="newComment-${widget.entityId}" rows="3" cols="50"> </textarea>

                        <button id="comment-new-${widget.entityId}" class="commentNewButton"> </button>
                    </div>
                    <c:if test="${not empty widget.comments}">
                        <c:forEach var="comment" items="${widget.comments}">
                                <div class="comment" data-widgetid="<c:out value="${comment.widgetId}"/>">
                                    <c:if test="${userProfile.entityId == comment.user.entityId}">
                                        <button id="comment-delete-${comment.entityId}" class="commentDeleteButton" value="Delete"> </button>
                                        <button id="comment-edit-${comment.entityId}" class="commentEditButton" value="Edit"> </button>
                                    </c:if>
                                    <fmt:formatDate value="${comment.createdDate}" type="both" var="commentDate"/>
                                    <p><c:out value="${comment.user.username} (${commentDate})"/></p>
                                    <p><c:out value="${comment.text}"/></p>

                                </div>
                        </c:forEach>
                    </c:if>
                </div>

            </div>
            
            <div class="widgetDetailRight">
                <c:if test="${not empty widget.screenshotUrl}">
                    <div class="widgetScreenshotTitle"><fmt:message key="page.widget.widgetPreview"/></div>
                    <div class="widgetScreenshot">
                        <img src="${widget.screenshotUrl}"
                             alt="<fmt:message key="page.general.screenshot"/>"
                             title="<c:out value="${widget.title}"/> <fmt:message key="page.general.screenshot"/>"/>
                    </div>
                </c:if>
            </div>
            <div class="clear-float" >&nbsp;</div>
        </div>
    </div>
    <div id="editComment-dialog" title="Edit Comment" style="display: none;">
        <textarea id="editComment" rows="3" cols="50"> </textarea>
    </div>
    <script src="//ajax.aspnetcdn.com/ajax/jQuery/jquery-1.6.4.min.js"></script>
    <script src="//ajax.aspnetcdn.com/ajax/jquery.ui/1.8.16/jquery-ui.min.js"></script>
    <!--[if lt IE 9]><script src=//css3-mediaqueries-js.googlecode.com/svn/trunk/css3-mediaqueries.js></script><![endif]-->
    <script src="<spring:url value="/script/rave.js"/>"></script>
    <script src="<spring:url value="/script/rave_api.js"/>"></script>
    <script src="<spring:url value="/script/rave_store.js"/>"></script>
    <script>
        $(function() {
            rave.setContext("<spring:url value="/app/" />");
            rave.store.init();
            rave.store.initComments();
        });
    </script>

</rave:rave_generic_page>
