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

<div class="container-fluid navbar-spacer" id="widgetStore">
<div class="row-fluid">
<section class="span8 pagination-header">
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
    <ul id="storeItems" class="storeItems">
            <%--@elvariable id="widget" type="org.apache.rave.model.Widget"--%>
        <c:forEach var="widget" items="${widgets.resultSet}">
            <%--@elvariable id="widgetsStatistics" type="org.apache.rave.portal.model.util.WidgetStatistics"--%>
            <c:set var="widgetStatistics" value="${widgetsStatistics[widget.id]}"/>
            <c:choose>
                <c:when test='${widget.featured == "true"}'>
                    <li class="storeItem storeItemFeatured">
                </c:when>
                <c:otherwise>
                    <li class="storeItem">
                </c:otherwise>
            </c:choose>
            <div class="widget-title-bar">
                <c:out value="${widget.title}"/>
            </div>
            <div class="storeItemLeft">
                <c:if test="${not empty widget.thumbnailUrl}">
                    <img class="storeWidgetThumbnail" src="${widget.thumbnailUrl}"
                         title="<c:out value="${widget.title}"/>" alt=""
                         width="120" height="60"/>
                </c:if>

                <div id="widgetAdded_${widget.id}" class="storeButton">
                    <button class="btn btn-small btn-primary widgetAddButton" id="addWidget_${widget.id}"
                            data-widget-id="${widget.id}" data-referring-page-id="${referringPageId}"
                            data-success="<fmt:message key="page.widget.addedToPage"/>">
                        <fmt:message key="page.widget.addToPage"/>
                    </button>
                </div>

            </div>

            <div class="storeItemCenter">
                <h4><a id="widget-${widget.id}-title"
                       class="secondaryPageItemTitle"
                       href="<spring:url value="/app/store/widget/${widget.id}" />?referringPageId=${referringPageId}">
                    <c:out value="${widget.title}"/>
                </a></h4>
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

                <div class="clearfix">
                    <div class="widgetRating">
                        <strong><fmt:message key="page.widget.rate"/></strong>
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
                        <div id="rating-${widget.id}" class="ratingButtons" data-toggle="buttons-radio">
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
                <div class="clearfix">
                    <c:if test="${not empty widget.tags}">
                        <div class="widgetTags">
                            <strong><fmt:message key="page.widget.tags.title"/></strong><br/>
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
                </div>
                <c:if test="${not empty widget.categories}">
                    <div class="clearfix">
                        <div class="widgetCategories">
                            <strong><fmt:message key="widget.categories"/></strong><br/>
                            <c:forEach var="category" items="${widget.categories}">
                                <span class="storeWidgetDesc"><c:out value="${category.text}"/></span>
                            </c:forEach>
                        </div>
                    </div>
                </c:if>
                            <span class="widgetUserCount">
                                <c:set var="widgetUserCountGreaterThanZero"
                                       value="${widgetStatistics != null && widgetStatistics.totalUserCount > 0}"/>
                                <c:if test="${widgetUserCountGreaterThanZero}">
                                    <a href="javascript:void(0);" class="displayUsersLink" data-widget-id="${widget.id}">
                                        </c:if>
                                        <fmt:formatNumber groupingUsed="true"
                                                          value="${widgetStatistics!=null?widgetStatistics.totalUserCount:0}"/>&nbsp;<fmt:message key="page.widget.usercount"/>
                                        <c:if test="${widgetUserCountGreaterThanZero}"></a></c:if>
                            </span>
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
    <form class="form-inline" action="<c:url value="/app/store/search"/>" method="GET">
        <fieldset>
            <input type="hidden" name="referringPageId" value="${referringPageId}">
            <legend style="margin-bottom: 0;"><fmt:message key="page.store.search"/></legend>
            <div class="control-group" style="margin-bottom: 18px;">
                <div class="input-append">
                    <fmt:message key="page.store.search.button" var="searchButtonText"/>
                    <input type="search" id="searchTerm" name="searchTerm" value="<c:out value="${searchTerm}"/>"/><button class="btn btn-primary" type="submit" value="${searchButtonText}">${searchButtonText}</button>
                </div>
            </div>
            <legend>Filter Widget Store</legend>
            <c:if test="${not empty tags}">
                <div class="control-group">
                    <label class="control-label" for="categoryList"><fmt:message key="page.store.list.widgets.tag"/></label>
                    <div class="controls">
                        <select name="tagList" id="tagList" class="x-large">
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
                    <label class="control-label" for="categoryList"><fmt:message key="page.store.list.widgets.category"/></label>
                    <div class="controls">
                        <select name="categoryList" id="categoryList" class="x-large">
                            <option value="0"></option>
                            <c:forEach var="category" items="${categories}">
                                <c:choose>
                                    <c:when test="${selectedCategory==category.id}">
                                        <option value="${category.id}" selected>
                                    </c:when>
                                    <c:otherwise>
                                        <option value="${category.id}">
                                    </c:otherwise>
                                </c:choose>
                                <c:out value="${category.text}"/>
                                </option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
            </c:if>
        </fieldset>
    </form>
    <a href="<spring:url value="/app/store/mine?referringPageId=${referringPageId}"/>"><fmt:message key="page.store.list.widgets.mine"/></a><br/>
    <a href="<spring:url value="/app/store?referringPageId=${referringPageId}"/>"><fmt:message key="page.store.list.widgets.all"/></a>
</section>
</div>
</div>

<portal:register-init-script location="${'AFTER_RAVE'}">
    <script>
        require(["rave", "ui", "portal/rave_store", "jquery"],
                function(rave, ui, raveStore, $){
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
                        $("#storeItems").on("click", "button.widgetAddButton", function(event){
                            var element = $(this);
                            rave.api.rpc.addWidgetToPage({widgetId: element.data('widget-id'),
                                pageId: element.data('referring-page-id'), buttonId: element.attr('id'),
                                successCallback: addWidgetToPageCallback
                            });
                        });

                        $("#storeItems").on("click", "a.displayUsersLink", function(event){
                            var element = $(this);
                            ui.displayUsersOfWidget(element.data('widget-id'));
                        });

                        raveStore.init('<c:out value="${referringPageId}"/>');
                    })
        })
    </script>
</portal:register-init-script>
