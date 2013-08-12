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
<rave:navbar pageTitle="${pagetitle}"/>
<div class="container-fluid navbar-spacer">
    <div class="row-fluid">
    <ul class="nav nav-tabs">
          <li><a href="<spring:url value="/app/store/widget/add?referringPageId=${referringPageId}" />">OpenSocial</a></li>
          <li><a href="<spring:url value="/app/store/widget/add/w3c?referringPageId=${referringPageId}" />">W3C</a></li>
          <li class="active"><a href="#marketplace">Marketplace</a></li>
    </ul> 
    </div>

    <div class="row-fluid">
        <section class="span8">
            <c:choose>
                <c:when test="${(empty widgets or widgets.totalResults eq 0)}">
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
            <%--@elvariable id="widgets" type="org.apache.rave.rest.model.SearchResult"--%>
            <c:if test="${widgets.totalResults gt 0}">
                <c:if test="${widgets.numberOfPages gt 1}">
                    <div>
                        <ul class="pagination">
                            <c:forEach var="i" begin="1" end="${widgets.numberOfPages}">
                                <c:url var="pageUrl" value="">
                                    <c:param name="referringPageId" value="${referringPageId}"/>
                                    <c:param name="searchTerm" value="${searchTerm}"/>
                                    <c:param name="offset" value="${(i - 1) * widgets.pageSize}"/>
                                </c:url>
                                <c:choose>
                                    <c:when test="${i eq widgets.currentPage}">
                                        <li class="active"><a href="#">${i}</a></li>
                                    </c:when>
                                    <c:otherwise>
                                        <li><a href="<c:out value="${pageUrl}"/>">${i}</a></li>
                                    </c:otherwise>
                                </c:choose>

                            </c:forEach>
                        </ul>
                    </div>

                </c:if>
                <ul class="storeItems" id="marketplaceWidgetList">

                    <c:forEach var="widget" items="${widgets.resultSet}">
                        <li class="storeItem">
                            <div class="storeItemLeft">
                                <c:if test="${not empty widget.thumbnailUrl}">
                                    <img class="storeWidgetThumbnail" src="${widget.thumbnailUrl}"
                                         title="<c:out value="${widget.title}"/>" alt=""
                                         width="120" height="60"/>
                                </c:if>
                                <div id="widgetAdded_${widget.id}" class="storeButton">
                                    <button class="btn btn-small btn-primary widgetAddButton" id="addWidget_${widget.id}"
                                            data-widget-url="<c:out value="${widget.url}"/>" data-widget-type="<c:out value="${widget.type}"/>">
                                        <fmt:message key="page.widget.marketplace.addToStore"/>
                                    </button>
                                </div>

                        </div>

                        <div class="storeItemCenter">
                            <a id="widget-${widget.id}-title"
                               class="secondaryPageItemTitle"
                               href="<spring:url value="/app/marketplace/widget/${widget.id}" />?referringPageId=${referringPageId}&externalId=${widget.externalId}">
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
                        </div>

                        <div class="clear-float"></div>
                        </li>
                    </c:forEach>
                </ul>

                <c:if test="${widgets.numberOfPages gt 1}">
                    <div >
                        <ul class="pagination">
                            <c:forEach var="i" begin="1" end="${widgets.numberOfPages}">
                                <c:url var="pageUrl" value="">
                                    <c:param name="referringPageId" value="${referringPageId}"/>
                                    <c:param name="searchTerm" value="${searchTerm}"/>
                                    <c:param name="offset" value="${(i - 1) * widgets.pageSize}"/>
                                </c:url>
                                <c:choose>
                                    <c:when test="${i eq widgets.currentPage}">
                                        <li class="active"><a href="#">${i}</a></li>
                                    </c:when>
                                    <c:otherwise>
                                        <li> <a href="<c:out value="${pageUrl}"/>">${i}</a></li>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                        </ul>
                    </div>
                </c:if>
            </c:if>
        </section>
        <section class="span4">
            <form class="form-inline" action="<c:url value="/app/marketplace/search"/>" method="GET">
                <fieldset>
                    <input type="hidden" name="referringPageId" value="${referringPageId}">
                    <legend style="margin-bottom: 0;"><fmt:message key="page.store.search"/></legend>
                    <div class="control-group" style="margin-bottom: 18px;">
                        <div class="input-append">
                            <fmt:message key="page.store.search.button" var="searchButtonText"/>
                            <input type="search" id="searchTerm" name="searchTerm" value="<c:out value="${searchTerm}"/>"/>
                            <button class="btn btn-primary" type="submit" value="${searchButtonText}">${searchButtonText}</button>
                        </div>
                    </div>
                    <legend></legend>
                    <c:if test="${not empty tags}">
                        <div class="control-group">
                            <label class="control-label" for="categoryList"><fmt:message key="page.store.list.widgets.tag"/></label>
                            <div class="controls">
                                <select name="tagList" id="tagList" class="span4">
                                <option value=""></option>
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
                        </div>
                    </c:if>

                    <c:if test="${not empty categories}">
                        <div class="control-group">
                            <label class="control-label" for="marketplaceCategoryList"><fmt:message key="page.store.list.widgets.category"/></label>
                            <div class="controls">
                                <select name="marketplaceCategoryList" id="marketplaceCategoryList" class="span4">
                                    <option value="0"></option>
                                    <c:forEach var="category" items="${categories}">
                                        <c:choose>
                                            <c:when test="${selectedCategory==category}">
                                                <option value="${category}" selected>
                                            </c:when>
                                            <c:otherwise>
                                                <option value="${category}">
                                            </c:otherwise>
                                        </c:choose>
                                        <c:out value="${category}"/>
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                    </c:if>
                </fieldset>
            </form>
        </section>
    </div>
</div>

<portal:register-init-script location="${'AFTER_RAVE'}">
    <script>
        require(["portal/rave_store", "rave", "jquery"], function(raveStore, rave, $){
            rave.registerOnInitHandler(function(){
                $("#marketplaceWidgetList").on("click", "button.widgetAddButton", function(event){
                    raveStore.confirmAddFromMarketplace($(this).data('widget-url'), $(this).data('widget-type'));
                });
            })

            $(function () {
                rave.init();
                raveStore.init('<c:out value="${referringPageId}"/>');
            });
        })
    </script>
</portal:register-init-script>
