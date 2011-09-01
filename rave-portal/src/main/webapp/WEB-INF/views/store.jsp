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
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="rave"%>
<fmt:setBundle basename="messages"/>

<fmt:message key="page.store.title" var="pagetitle"/>
<rave:rave_generic_page pageTitle="${pagetitle}">
<div id="header">
    <div class="header-a">
        <span class="backToPage">
            <a href="<spring:url value="/index.html" />"><fmt:message key="page.general.back"/></a>
        </span>
    </div>
    <div class="widget-a">
        <span>
            <a href="<spring:url value="/app/store/widget/add"/>"><fmt:message key="page.addwidget.title"/></a>
        </span>
    </div>
    <h1>${pagetitle}</h1>
</div>

<div id="content">
    
    <div class="storeSearch">
        <form action="<c:url value="/app/store/search"/>" method="GET">
            <fieldset>
                <input type="hidden" name="referringPageId" value="${referringPageId}">
                <p>
                  <label for="searchTerm"><fmt:message key="page.store.search"/></label>
                  <input type="search" id="searchTerm" name="searchTerm" value="<c:out value="${searchTerm}"/>"/>
                  <fmt:message key="page.store.search.button" var="searchButtonText"/>
                  <input type="submit" value="${searchButtonText}">
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
            <c:forEach var="widget" items="${widgets.resultSet}">
                <li class="storeItem">
                    <div class="storeItemLeft">
                        <c:if test="${not empty widget.thumbnailUrl}">
                            <img class="storeWidgetThumbnail" src="${widget.thumbnailUrl}"
                                 title="<c:out value="${widget.title}"/>" alt=""
                                 width="120" height="60"/>
                        </c:if>
                        <div class="widgetType"><c:out value="${widget.type}"/></div>
                    </div>
                    <div class="storeItemCenter">
                        <div id="widgetAdded_${widget.id}" class="storeButton">
                            <button class="storeItemButton"
                                    id="addWidget_${widget.id}"
                                    onclick="rave.api.rpc.addWidgetToPage({widgetId: ${widget.id}, pageId: ${referringPageId}});">
                                <fmt:message key="page.widget.addToPage"/>
                            </button>
                        </div>
                        <a class="secondaryPageItemTitle"
                           href="<spring:url value="/app/store/widget/${widget.id}" />?referringPageId=${referringPageId}">
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
<script>
    var rave = rave || {
        getContext : function() {
            return "<spring:url value="/app/" />";
        }
    }
</script>
<script src="//ajax.aspnetcdn.com/ajax/jQuery/jquery-1.6.1.min.js"></script>
<script src="<spring:url value="/script/rave_api.js"/>"></script>
</rave:rave_generic_page>
