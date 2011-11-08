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
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="rave"%>
<fmt:setBundle basename="messages"/>

<fmt:message key="page.store.title" var="pagetitle"/>
<rave:rave_generic_page pageTitle="${pagetitle}">
<header>
    <nav class="topnav">
        <ul class="horizontal-list">
            <li>
                <a href="<spring:url value="/app/store/widget/add?referringPageId=${referringPageId}"/>"><fmt:message key="page.addwidget.title"/></a>
            </li>
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
            <sec:authorize access="hasRole('ROLE_ADMIN')">
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
    <h1>${pagetitle}</h1>
</header>

<div id="content">

    <div class="storeSearch">
        <form action="<c:url value="/app/store/search"/>" method="GET">
            <fieldset>
                <input type="hidden" name="referringPageId" value="${referringPageId}">
                <p>
                  <label for="searchTerm"><fmt:message key="page.store.search"/></label>
                  <input type="search" id="searchTerm" name="searchTerm" value="<c:out value="${searchTerm}"/>"/>
                  <fmt:message key="page.store.search.button" var="searchButtonText"/>
                  <input type="submit" value="${searchButtonText}"/>
                </p>
            </fieldset>
        </form>
        <div class="clear-float">&nbsp;</div>
        <c:choose>
            <c:when test="${empty searchTerm and (empty widgets or widgets.totalResults eq 0)}">
                <%-- Empty db --%>
                <fmt:message key="page.store.list.noresult" var="listheader"/>
            </c:when>
            <c:when test="${empty searchTerm}">
                <fmt:message key="page.store.list.result.x.to.y" var="listheader">
                    <fmt:param value="${offset + 1}"/>
                    <fmt:param value="${offset + fn:length(widgets.resultSet)}"/>
                    <fmt:param value="${widgets.totalResults}"/>
                </fmt:message>
            </c:when>
            <c:when test="${not empty searchTerm and widgets.totalResults eq 0}">
                <fmt:message key="page.store.list.search.noresult" var="listheader">
                    <fmt:param><c:out value="${searchTerm}"/></fmt:param>
                </fmt:message>
            </c:when>
            <c:otherwise>
                <fmt:message key="page.store.list.search.result.x.to.y" var="listheader">
                    <fmt:param value="${offset + 1}"/>
                    <fmt:param value="${offset + fn:length(widgets.resultSet)}"/>
                    <fmt:param value="${widgets.totalResults}"/>
                    <fmt:param><c:out value="${searchTerm}"/></fmt:param>
                </fmt:message>
            </c:otherwise>
        </c:choose>
        <h2>${listheader}</h2>
    </div>

    <%--@elvariable id="widgets" type="org.apache.rave.portal.model.util.SearchResult"--%>
    <c:if test="${widgets.totalResults gt 0}">
        <ul class="storeItems">
        <%--@elvariable id="widget" type="org.apache.rave.portal.model.Widget"--%>
            <c:forEach var="widget" items="${widgets.resultSet}">
                <li class="storeItem">
                    <div class="storeItemLeft">
                        <c:if test="${not empty widget.thumbnailUrl}">
                            <img class="storeWidgetThumbnail" src="${widget.thumbnailUrl}"
                                 title="<c:out value="${widget.title}"/>" alt=""
                                 width="120" height="60"/>
                        </c:if>
                        <div class="widgetType"><c:out value="${widget.type}"/></div>

                        <div class="widgetRating">
                            <fmt:message key="page.widget.rate"/>
                            <div id="rating-${widget.entityId}" class="ratingButtons">
                                <input type="radio" id="like-${widget.entityId}" class="widgetLikeButton" name="rating-${widget.entityId}"${widgetsStatistics[widget.entityId].userRating==10?" checked='true'":""}> <label for="like-${widget.entityId}">${widgetsStatistics[widget.entityId]!=null?widgetsStatistics[widget.entityId].totalLike:"0"}</label>
                                <input type="radio" id="dislike-${widget.entityId}" class="widgetDislikeButton" name="rating-${widget.entityId}"${widgetsStatistics[widget.entityId].userRating==0?" checked='true'":""}> <label for="dislike-${widget.entityId}">${widgetsStatistics[widget.entityId]!=null?widgetsStatistics[widget.entityId].totalDislike:"0"}</label>
                            </div>
                        </div>
                    </div>
                    <div class="storeItemCenter">
                        <div id="widgetAdded_${widget.entityId}" class="storeButton">
                            <button class="storeItemButton"
                                    id="addWidget_${widget.entityId}"
                                    onclick="rave.api.rpc.addWidgetToPage({widgetId: ${widget.entityId}, pageId: ${referringPageId}});">
                                <fmt:message key="page.widget.addToPage"/>
                            </button>
                        </div>
                        <a class="secondaryPageItemTitle"
                           href="<spring:url value="/app/store/widget/${widget.entityId}" />?referringPageId=${referringPageId}">
                            <c:out value="${widget.title}"/>
                        </a>

                        <c:if test="${not empty widget.author}">
                            <div class="storeWidgetAuthor"><fmt:message key="widget.author"/>: <c:out value="${widget.author}"/></div>
                        </c:if>
                        <c:if test="${not empty widget.description}">
                            <div class="storeWidgetDesc"><c:out value="${widget.description}"/></div>
                        </c:if>
                    </div>
                    <div class="clear-float" >&nbsp;</div>
                </li>
            </c:forEach>
        </ul>

        <c:if test="${widgets.numberOfPages gt 1}">
            <div class="storeBox">
                <ul class="paging">
                    <c:forEach var="i" begin="0" end="${widgets.numberOfPages - 1}">
                        <c:url var="pageUrl" value="">
                            <c:param name="referringPageId" value="${referringPageId}"/>
                            <c:param name="searchTerm" value="${searchTerm}"/>
                            <c:param name="offset" value="${i * widgets.pageSize}"/>
                        </c:url>
                        <li><a href="<c:out value="${pageUrl}"/>">${i + 1}</a></li>
                    </c:forEach>
                </ul>
            </div>
        </c:if>
    </c:if>
</div>

<script src="//ajax.aspnetcdn.com/ajax/jQuery/jquery-1.6.1.min.js"></script>
<script src="//ajax.aspnetcdn.com/ajax/jquery.ui/1.8.13/jquery-ui.min.js"></script>
<script src="<spring:url value="/script/rave.js"/>"></script>
<script src="<spring:url value="/script/rave_api.js"/>"></script>
<script src="<spring:url value="/script/rave_store.js"/>"></script>
<script>
    $(function () {
        rave.store.init();
    });
</script>
</rave:rave_generic_page>
