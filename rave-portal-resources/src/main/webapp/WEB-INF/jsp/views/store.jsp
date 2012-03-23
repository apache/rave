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

<header>
    <nav class="topnav">
        <ul class="horizontal-list">
            <li>
                <a href="<spring:url value="/app/store/widget/add?referringPageId=${referringPageId}"/>"><fmt:message
                        key="page.addwidget.title"/></a>
            </li>
            <li>
                <c:choose>
                    <c:when test="${empty referringPageId}">
                        <spring:url value="/index.html" var="gobackurl"/>
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
    <h1>${pagetitle}</h1>
</header>

<div id="content" class="pageContent">

<section class="storeSearch">
    <form action="<c:url value="/app/store/search"/>" method="GET">
        <fieldset>
            <input type="hidden" name="referringPageId" value="${referringPageId}">
            <h2>
                <label for="searchTerm"><fmt:message key="page.store.search"/></label>
            </h2>
            <p>
                <input type="search" id="searchTerm" name="searchTerm" value="<c:out value="${searchTerm}"/>"/>
                <fmt:message key="page.store.search.button" var="searchButtonText"/>
                <input type="submit" value="${searchButtonText}"/>
                <c:if test="${not empty searchTerm}">
                    <a href="<spring:url value="/app/store?referringPageId=${referringPageId}"/>">
                        <fmt:message key="admin.clearsearch"/></a>
                </c:if>
            </p>
            <c:if test="${not empty tags}">
                <div>
                    <fmt:message key="page.store.list.widgets.tag"/>
                    <select name="tagList" id="tagList" style="min-width:100px" >
                        <option value="  "></option>
                        <c:forEach var="tag" items="${tags}">
                            <c:choose>
                                <c:when test="${selectedTag==tag.keyword}">
                                    <option selected>
                                </c:when>
                                <c:otherwise>
                                    <option>
                                </c:otherwise>
                            </c:choose>
                            <c:out value="${tag.keyword}"/>
                            </option>
                        </c:forEach>
                    </select>
                </div>
            </c:if>

            <c:if test="${not empty categories}">
                <div>
                    <fmt:message key="page.store.list.widgets.category"/>
                    <select name="categoryList" id="categoryList" style="min-width:100px" >
                        <option value="0"></option>
                        <c:forEach var="category" items="${categories}">
                            <c:choose>
                                <c:when test="${selectedCategory==category.entityId}">
                                    <option value="${category.entityId}" selected>
                                </c:when>
                                <c:otherwise>
                                    <option value="${category.entityId}">
                                </c:otherwise>
                            </c:choose>
                            <c:out value="${category.text}"/>
                            </option>
                        </c:forEach>
                    </select>
                </div>
            </c:if>
        </fieldset>
    </form>

    <div>
        <a href="<spring:url value="/app/store/mine?referringPageId=${referringPageId}"/>"><fmt:message
                key="page.store.list.widgets.mine"/></a>
    </div>
    <div>
        <a href="<spring:url value="/app/store?referringPageId=${referringPageId}"/>"><fmt:message
                key="page.store.list.widgets.all"/></a>
    </div>
</section>

<section class="storeBox">
    <c:choose>
        <c:when test="${empty searchTerm and (empty widgets or widgets.totalResults eq 0)}">
            <%-- Empty db --%>
            <fmt:message key="page.store.list.noresult" var="listheader"/>
        </c:when>
        <c:when test="${empty searchTerm}">
            <fmt:message key="page.store.list.result.x.to.y" var="listheader">
                <fmt:param value="${widgets.offset + 1}"/>
                <fmt:param value="${widgets.offset + fn:length(widgets.resultSet)}"/>
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
                <fmt:param value="${widgets.offset + 1}"/>
                <fmt:param value="${widgets.offset + fn:length(widgets.resultSet)}"/>
                <fmt:param value="${widgets.totalResults}"/>
                <fmt:param><c:out value="${searchTerm}"/></fmt:param>
            </fmt:message>
        </c:otherwise>
    </c:choose>
    <h2>${listheader}</h2>
    <%--@elvariable id="widgets" type="org.apache.rave.portal.model.util.SearchResult"--%>
    <c:if test="${widgets.totalResults gt 0}">
        <c:if test="${widgets.numberOfPages gt 1}">

            <ul class="paging">
                <c:forEach var="i" begin="1" end="${widgets.numberOfPages}">
                    <c:url var="pageUrl" value="">
                        <c:param name="referringPageId" value="${referringPageId}"/>
                        <c:param name="searchTerm" value="${searchTerm}"/>
                        <c:param name="offset" value="${(i - 1) * widgets.pageSize}"/>
                    </c:url>

                    <li>
                        <c:choose>
                            <c:when test="${i eq widgets.currentPage}">
                                <span class="currentPage">${i}</span>
                            </c:when>
                            <c:otherwise>
                                <a href="<c:out value="${pageUrl}"/>">${i}</a>
                            </c:otherwise>
                        </c:choose>
                    </li>
                </c:forEach>
            </ul>

        </c:if>

        <ul class="storeItems">
            </br>
                <%--@elvariable id="widget" type="org.apache.rave.portal.model.Widget"--%>
            <c:forEach var="widget"
                       items="${widgets.resultSet}">
                <%--@elvariable id="widgetsStatistics" type="org.apache.rave.portal.model.util.WidgetStatistics"--%>
                <c:set var="widgetStatistics" value="${widgetsStatistics[widget.entityId]}"/>
                <c:choose>
                    <c:when test='${widget.featured == "true"}' >
                        <li class="storeItem storeItemFeatured">
                    </c:when>
                    <c:otherwise>
                        <li class="storeItem">
                    </c:otherwise>
                </c:choose>

                <div class="storeItemLeft">
                        <c:if test="${not empty widget.thumbnailUrl}">
                            <img class="storeWidgetThumbnail" src="${widget.thumbnailUrl}"
                                 title="<c:out value="${widget.title}"/>" alt=""
                                 width="120" height="60"/>
                        </c:if>

                        <div id="widgetAdded_${widget.entityId}" class="storeButton">
                            <button class="storeItemButton"
                                    id="addWidget_${widget.entityId}"
                                    onclick="rave.api.rpc.addWidgetToPage({widgetId: ${widget.entityId}, pageId: ${referringPageId}, buttonId: this.id});">
                                <fmt:message key="page.widget.addToPage"/>
                            </button>
                        </div>

                    </div>

                    <div class="storeItemCenter">

                        <a id="widget-${widget.entityId}-title"
                           class="secondaryPageItemTitle"
                           href="<spring:url value="/app/store/widget/${widget.entityId}" />?referringPageId=${referringPageId}">
                            <c:out value="${widget.title}"/>
                        </a>
                        <c:if test="${widget.disableRendering}">
                            <div class="storeWidgetDisabled">
                                <span class="widget-disabled-icon-store ui-icon ui-icon-alert"
                                      title="<fmt:message key="widget.chrome.disabled"/>"></span>
                                <c:out value="${widget.disableRenderingMessage}" escapeXml="true"/>
                            </div>
                        </c:if>
                        <c:if test="${not empty widget.author}">
                            <div class="storeWidgetAuthor"><fmt:message key="widget.author"/>: <c:out
                                    value="${widget.author}"/></div>
                        </c:if>
                        <c:if test="${not empty widget.description}">
                            <div class="storeWidgetDesc"><c:out
                                    value="${fn:substring(widget.description, 0, 200)}..."/></div>
                        </c:if>
                        <div class="widgetRating">
                            <fmt:message key="page.widget.rate"/>

                            <div id="rating-${widget.entityId}" class="ratingButtons">
                                <form>
                                    <input type="hidden" id="rate-${widget.entityId}"
                                           value="${widgetsStatistics[widget.entityId]!=null?widgetsStatistics[widget.entityId].userRating:"-1"}">
                                </form>
                                <input type="radio" id="like-${widget.entityId}" class="widgetLikeButton"
                                       name="rating-${widget.entityId}"${widgetsStatistics[widget.entityId].userRating==10?" checked='true'":""}>
                                <label for="like-${widget.entityId}"></label>
                                <input type="radio" id="dislike-${widget.entityId}" class="widgetDislikeButton"
                                       name="rating-${widget.entityId}"${widgetsStatistics[widget.entityId].userRating==0?" checked='true'":""}>
                                <label for="dislike-${widget.entityId}"> </label>

                                <!-- Displaying the likes and dislikes rating along with total votes -->

                            </div>
                        </div>
                        <c:if test="${not empty widget.tags}">
                            <table class="widgetTags">
                                <tr>
                                    <td>
                                        <fmt:message key="page.widget.tags.title"/>
                                    </td>
                                    <c:forEach var="tag" items="${widget.tags}">
                                        <td class="storeWidgetDesc"><c:out value="${tag.tag.keyword}"/></td>
                                    </c:forEach>
                                </tr>
                            </table>
                        </c:if>
                        <c:if test="${not empty widget.categories}">
                            <table class="widgetCategories">
                                <tr>
                                    <td>
                                        <fmt:message key="widget.categories"/>
                                    </td>
                                    <c:forEach var="category" items="${widget.categories}">
                                        <td class="storeWidgetDesc"><c:out value="${category.text}"/></td>
                                    </c:forEach>
                                </tr>
                            </table>
                        </c:if>
                        <ul class="horizontal-list">
                            <!-- display total likes -->
                            <li>
                                <c:set var="widgetLikes">
                                    ${widgetsStatistics[widget.entityId]!=null?widgetsStatistics[widget.entityId].totalLike:"0"}
                                </c:set>
                                <fmt:message key="page.widget.rate.likes"/><label id="totalLikes-${widget.entityId}"
                                                                                  data-rave-widget-likes="${widgetLikes}">${widgetLikes}</label>
                            </li>
                            <!-- display total dislikes  -->
                            <li>
                                <c:set var="widgetDislikes">
                                    ${widgetsStatistics[widget.entityId]!=null?widgetsStatistics[widget.entityId].totalDislike:"0"}
                                </c:set>
                                <fmt:message key="page.widget.rate.dislikes"/><label
                                    id="totalDislikes-${widget.entityId}"
                                    data-rave-widget-dislikes="${widgetDislikes}">${widgetDislikes}</label>
                            </li>
                            <li class="widgetUserCount">
                                <c:set var="widgetUserCountGreaterThanZero"
                                       value="${widgetStatistics != null && widgetStatistics.totalUserCount > 0}"/>
                                <c:if test="${widgetUserCountGreaterThanZero}"><a href="javascript:void(0);"
                                                                                  onclick="rave.displayUsersOfWidget(${widget.entityId});"></c:if>
                                <fmt:formatNumber groupingUsed="true"
                                                  value="${widgetStatistics!=null?widgetStatistics.totalUserCount:0}"/>&nbsp;<fmt:message
                                        key="page.widget.usercount"/>
                                <c:if test="${widgetUserCountGreaterThanZero}"></a></c:if>
                            </li>
                        </ul>
                    </div>

                    <div class="clear-float"></div>
                </li>
            </c:forEach>
        </ul>

        <c:if test="${widgets.numberOfPages gt 1}">

            <ul class="paging">
                <c:forEach var="i" begin="1" end="${widgets.numberOfPages}">
                    <c:url var="pageUrl" value="">
                        <c:param name="referringPageId" value="${referringPageId}"/>
                        <c:param name="searchTerm" value="${searchTerm}"/>
                        <c:param name="offset" value="${(i - 1) * widgets.pageSize}"/>
                    </c:url>

                    <li>
                        <c:choose>
                            <c:when test="${i eq widgets.currentPage}">
                                <span class="currentPage">${i}</span>
                            </c:when>
                            <c:otherwise>
                                <a href="<c:out value="${pageUrl}"/>">${i}</a>
                            </c:otherwise>
                        </c:choose>
                    </li>
                </c:forEach>
            </ul>

        </c:if>
    </c:if>
</section>

</div>

<portal:register-init-script location="${'AFTER_RAVE'}">
    <script>
        $(function () {
            rave.store.init('<c:out value="${referringPageId}"/>');
        });
    </script>
</portal:register-init-script>