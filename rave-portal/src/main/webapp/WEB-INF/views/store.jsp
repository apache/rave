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
<%@ taglib tagdir="/WEB-INF/tags" prefix="rave"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<rave:rave_generic_page pageTitle="Widget Store - Rave">
<div id="header">
    <div class="header-a">
        <span class="backToPage">
            <a href="<spring:url value="/index.html" />">Back to Rave</a>
        </span>
    </div>
    <h1>Widget Store</h1>
</div>

<div id="content">
    
    <div class="storeSearch">
        <form action="<c:url value="/app/store/search"/>" method="GET">
            <fieldset>
                <input type="hidden" name="referringPageId" value="${referringPageId}">
                <p>
                    <label for="searchTerm">Search in widget store</label>
                    <input type="search" id="searchTerm" name="searchTerm" value="<c:out value="${searchTerm}"/>"/>
                    <input type="submit" value="Search">
                </p>
            </fieldset>
        </form>
        <div class="clear-float">&nbsp;</div>
        <c:choose>
            <c:when test="${empty searchTerm and (empty widgets or widgets.totalResults eq 0)}">
                <%-- Empty db --%>
                <h2>No widgets found</h2>
            </c:when>
            <c:when test="${empty searchTerm}">
                <%-- TODO: introduce paging here --%>
                <h2>Showing ${offset + 1} - ${offset + fn:length(widgets.resultSet)} of ${widgets.totalResults} widgets</h2>
            </c:when>
            <c:when test="${not empty searchTerm and widgets.totalResults eq 0}">
                <h2>No widgets found for '<c:out value="${searchTerm}"/>'</h2>
            </c:when>
            <c:otherwise>
                <h2>Showing ${offset + 1} - ${offset + fn:length(widgets.resultSet)} of ${widgets.totalResults} widgets that match '<c:out value="${searchTerm}"/>'</h2>
            </c:otherwise>
        </c:choose>
    </div>

    <%--@elvariable id="widgets" type="org.apache.rave.portal.model.util.SearchResult"--%>
    <c:if test="${widgets.totalResults gt 0}">
        <ul class="storeItems">
            <c:forEach var="widget" items="${widgets.resultSet}">
                <li class="storeItem">
                    <div class="storeItemLeft">
                        <c:if test="${not empty widget.thumbnailUrl}">
                            <img class="storeWidgetThumbnail" src="${widget.thumbnailUrl}"
                                 title="<c:out value="${widget.title}"/>" alt="Thumbnail for widget <c:out value="${widget.title}"/>"
                                 width="120" height="60"/>
                        </c:if>
                        <div class="widgetType"><c:out value="${widget.type}"/></div>
                    </div>
                    <div class="storeItemCenter">
                        <div id="widgetAdded_${widget.id}" class="storeButton">
                            <button class="storeItemButton"
                                    id="addWidget_${widget.id}"
                                    onclick="rave.api.rpc.addWidgetToPage({widgetId: ${widget.id}, pageId: ${referringPageId}});">
                                Add to Page
                            </button>
                        </div>
                        <a class="secondaryPageItemTitle"
                           href="<spring:url value="/app/store/widget/${widget.id}" />?referringPageId=${referringPageId}">
                            <c:out value="${widget.title}"/>
                        </a>

                        <c:if test="${not empty widget.author}">
                            <div class="storeWidgetAuthor">By: <c:out value="${widget.author}"/></div>
                        </c:if>
                        <c:if test="${not empty widget.description}">
                            <div class="storeWidgetDesc"><c:out value="${widget.description}"/></div>
                        </c:if>
                    </div>
                    <div class="clear-float" >&nbsp;</div>
                </li>
            </c:forEach>
        </ul>
        
        <c:if test="${fn:length(widgets.resultSet) lt widgets.totalResults and widgets.pageSize gt 0}">
            <div class="storeBox">
            <ul class="paging">
                <c:set var="nrOfPages" value="${widgets.totalResults / widgets.pageSize}"/>
                <c:forEach var="i" begin="0" end="${nrOfPages}">
                    <c:url var="pageUrl" value="/app/store/search">
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
