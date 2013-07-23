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
<jsp:useBean id="pages" type="java.util.List<org.apache.rave.model.Page>" scope="request"/>
<%--@elvariable id="page" type="org.apache.rave.model.Page"--%>
<sec:authentication property="principal.username" var="principleUsername" scope="request"/>
<sec:authentication property="principal.displayName" var="displayName" scope="request"/>

<fmt:message key="page.home.welcome" var="pagetitle">
    <fmt:param>
        <c:choose>
            <c:when test="${not empty displayName}"><c:out value="${displayName}"/></c:when>
            <c:otherwise><c:out value="${principleUsername}"/></c:otherwise>
        </c:choose>
    </fmt:param>
</fmt:message>
<rave:navbar pageTitle="${pagetitle}"/>

<input id="currentPageId" type="hidden" value="${page.id}" />
<c:set var="hasOnlyOnePage" scope="request">
    <c:choose>
        <c:when test="${fn:length(pages) == 1}">true</c:when>
        <c:otherwise>false</c:otherwise>
    </c:choose>
</c:set>
<div id="tabsHeader">
    <%-- render the page links --%>
    <div id="tabs" class="rave-ui-tabs">
        <c:forEach var="userPage" items="${pages}">
            <%-- determine if the current page in the list matches the page the user is viewing --%>
            <c:set var="isCurrentPage">
                <c:choose>
                    <c:when test="${page.id == userPage.id}">true</c:when>
                    <c:otherwise>false</c:otherwise>
                </c:choose>
            </c:set>
            <div id="tab-${userPage.id}" class="rave-ui-tab rave-ui-tab-mobile<c:if test="${isCurrentPage}"> rave-ui-tab-selected rave-ui-tab-selected-mobile</c:if>">
                <div id="pageTitle-${userPage.id}" class="page-title"><a href="<spring:url value="page/view/${userPage.id}" />"><c:out value="${userPage.name}"/></a></div>
            </div>
        </c:forEach>
        <%-- display the add page button at the end of the tabs --%>
        <fmt:message key="page.general.addnewpage" var="addNewPageTitle"/>
        <button id="add_page" title="${addNewPageTitle}" style="display: none;"></button>
    </div>
</div>
<%-- the mobile view will only show one column of widgets --%>
<div id="pageContent" class="pageContent-mobile">
    <c:forEach var="region" items="${page.regions}">
        <div class="region-mobile" id="region-${region.id}-id">
            <c:forEach var="regionWidget" items="${region.regionWidgets}">
                <portal:widget var="widget" id="${regionWidget.widgetId}" />
                <div class="widget-wrapper widget-wrapper-mobile" id="widget-${regionWidget.id}-wrapper">
                    <div class="widget-title-bar widget-title-bar-mobile" data-regionWidget-id="${regionWidget.id}">
                        <span id="widget-${regionWidget.id}-collapse" class="widget-toolbar-toggle-collapse" title="<fmt:message key="widget.chrome.toggle"/>"><i class="icon-chevron-down"></i></span>
                        <div id="widget-${regionWidget.id}-title" class="widget-title">
                            <c:out value="${widget.title}"/>
                        </div>
                    </div>
                    <div class="widget-prefs" id="widget-${regionWidget.id}-prefs-content"></div>
                    <div class="widget widget-mobile" id="widget-${regionWidget.id}-body">
                        <portal:render-widget regionWidget="${regionWidget}" widget="${widget}"/>
                    </div>
                </div>
            </c:forEach>
        </div>
    </c:forEach>
</div>
<fmt:message key="page.general.addnewpage" var="addNewPageTitle"/>
<div id="dialog" title="${addNewPageTitle}" class="dialog hidden">
    <form id="pageForm">
        <div id="pageFormErrors" class="error"></div>
        <fieldset class="ui-helper-reset">
            <input type="hidden" name="tab_id" id="tab_id" value="" />
            <label for="tab_title"><fmt:message key="page.general.addpage.title"/></label>
            <input type="text" name="tab_title" id="tab_title" value="" class="required ui-widget-content ui-corner-all" />
            <label for="pageLayout"><fmt:message key="page.general.addpage.selectlayout"/></label>
            <select name="pageLayout" id="pageLayout">
                <option value="columns_1" id="columns_1_id"><fmt:message key="page.general.addpage.layout.columns_1"/></option>
                <option value="columns_2" id="columns_2_id" selected="selected"><fmt:message key="page.general.addpage.layout.columns_2"/></option>
                <option value="columns_2wn" id="columns_2wn_id"><fmt:message key="page.general.addpage.layout.columns_2wn"/></option>
                <option value="columns_3" id="columns_3_id"><fmt:message key="page.general.addpage.layout.columns_3"/></option>
                <option value="columns_3nwn" id="columns_3nwn_id"><fmt:message key="page.general.addpage.layout.columns_3nwn"/></option>
                <option value="columns_4" id="columns_4_id"><fmt:message key="page.general.addpage.layout.columns_4"/></option>
                <option value="columns_3nwn_1_bottom" id="columns_3nwn_1_bottom"><fmt:message key="page.general.addpage.layout.columns_3nwn_1_bottom"/></option>
            </select>
        </fieldset>
    </form>
</div>
<fmt:message key="page.general.movepage" var="movePageTitle"/>
<div id="movePageDialog" title="${movePageTitle}" class="dialog hidden">
    <div><fmt:message key="page.general.movethispage"/></div>
    <form id="movePageForm">
        <select id="moveAfterPageId">
            <c:if test="${pageUser.renderSequence != 1}">
                <option value="-1"><fmt:message key="page.general.movethispage.tofirst"/></option>
            </c:if>
            <c:forEach var="userPage" items="${pages}">
                <c:if test="${userPage.id != page.id}">
                    <option value="${userPage.id}">
                        <fmt:message key="page.general.movethispage.after">
                            <fmt:param><c:out value="${userPage.name}"/></fmt:param>
                        </fmt:message>
                    </option>
                </c:if>
            </c:forEach>
        </select>
    </form>
</div>
<fmt:message key="widget.menu.movetopage" var="moveWidgetToPageTitle"/>
<div id="moveWidgetDialog" title="${moveWidgetToPageTitle}" class="dialog hidden">
    <div><fmt:message key="widget.menu.movethiswidget"/></div>
    <form id="moveWidgetForm">
        <select id="moveToPageId">
            <c:forEach var="userPage" items="${pages}">
                <c:if test="${userPage.id != page.id}">
                    <option value="${userPage.id}">
                        <c:out value="${userPage.name}"/>
                    </option>
                </c:if>
            </c:forEach>
        </select>
    </form>
</div>

<portal:register-init-script location="${'AFTER_RAVE'}">
    <script>
        require(["rave", "jquery"], function(rave, $){
            rave.registerOnInitHandler(function(){
                $("#pageContent").on("click", ".widget-title-bar-mobile", function(event){
                    //TODO:This function is undefined, must re-define before un-commenting out
                    //rave.toggleMobileWidget($(this).data('regionWidget-id'));
                    console.log("rave.toggleMobileWidget function is undefined");
                });
            });

            $(function() {
                rave.getViewer().editor =<c:out value="${pageUser.editor}"/>;
            });
        })
    </script>
</portal:register-init-script>
