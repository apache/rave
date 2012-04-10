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
<jsp:useBean id="pages" type="java.util.List<org.apache.rave.portal.model.Page>" scope="request"/>
<jsp:useBean id="pageLayouts" type="java.util.List<org.apache.rave.portal.model.PageLayout>" scope="request"/>
<%--@elvariable id="page" type="org.apache.rave.portal.model.Page"--%>
<header class="navbar navbar-fixed-top">
    <nav>
        <a class="brand" href="#">
            <fmt:message key="page.home.welcome"><fmt:param>
                <c:choose>
                    <c:when test="${not empty page.owner.displayName}"><c:out value="${page.owner.displayName}"/></c:when>
                    <c:otherwise><c:out value="${page.owner.username}"/></c:otherwise>
                </c:choose>
            </fmt:param>
            </fmt:message>

        </a>
        <ul class="nav pull-right">
            <li>
                <c:set var="profileUrl">/app/person/<sec:authentication property="principal.username"/>?referringPageId=${page.entityId}
                </c:set>
                <a href="<spring:url value="${profileUrl}" />">
                    <fmt:message key="page.profile.title">
                        <fmt:param><c:out value="${page.owner.displayName}"/></fmt:param>
                    </fmt:message>
                </a>
            </li>
            <li>
                <a href="<spring:url value="/app/store?referringPageId=${page.entityId}" />">
                    <fmt:message key="page.store.title"/>
                </a>
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
</header>


<input id="currentPageId" type="hidden" value="${page.entityId}"/>
<c:set var="hasOnlyOnePage" scope="request">
    <c:choose>
        <c:when test="${fn:length(pages) == 1}">true</c:when>
        <c:otherwise>false</c:otherwise>
    </c:choose>
</c:set>


<div class="container-fluid navbar-spacer">
    <nav>
        <ul class="nav nav-tabs">
            <c:forEach var="userPage" items="${pages}">
                <%-- determine if the current page in the list matches the page the user is viewing --%>
                <c:set var="isCurrentPage">
                    <c:choose>
                        <c:when test="${page.entityId == userPage.entityId}">true</c:when>
                        <c:otherwise>false</c:otherwise>
                    </c:choose>
                </c:set>
                <c:choose>
                    <c:when test="${isCurrentPage}">
                        <li id="tab-${userPage.entityId}" class="active dropdown">
                            <a href="#" class="dropdown-toggle" data-toggle="dropdown"><c:out value="${userPage.name}"/><b class="caret"></b></a>
                            <ul class="dropdown-menu">
                                <li id="pageMenuEdit"><a href="#"><fmt:message key="page.general.editpage"/></a></li>
                                <li id="pageMenuDelete" class="<c:if test='${hasOnlyOnePage}'>menu-item-disabled</c:if>"><a href="#"><fmt:message key="page.general.deletepage"/></a></li>
                                <li id="pageMenuMove" class="<c:if test='${hasOnlyOnePage}'>menu-item-disabled</c:if>"><a href="#"><fmt:message key="page.general.movepage"/></a></li>
                            </ul>
                        </li>
                    </c:when>
                    <c:otherwise>
                        <li id="tab-${userPage.entityId}" onclick="rave.viewPage(${userPage.entityId});"><a href="#"><c:out value="${userPage.name}"/></a></li>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
            <a id="addPageButton" href="#" class="btn"><i class="icon-plus"></i></a>
        </ul>
    </nav>


    <div class="row-fluid">
        <div id="emptyPageMessageWrapper" class="emptyPageMessageWrapper hidden">
            <div class="emptyPageMessage">
                <a href="<spring:url value="/app/store?referringPageId=${page.entityId}" />"><fmt:message key="page.general.empty"/></a>
            </div>
        </div>
        <div class="regions">
            <%-- insert the region layout template --%>
            <tiles:insertTemplate template="${layout}"/>
        </div>
        <div class="clear-float">&nbsp;</div>
    </div>


    <div id="pageMenuDialog" class="modal hide" data-backdrop="static">
        <div class="modal-header">
            <a href="#" class="close" data-dismiss="modal">&times;</a>
            <h3 id="pageMenuDialogHeader"></h3>
        </div>
        <div class="modal-body">
            <form id="pageForm" class="form-horizontal">
                <input type="hidden" name="tab_id" id="tab_id" value=""/>
                <fieldset>
                    <div class="control-group error">
                        <label id="pageFormErrors" class="control-label"></label>
                    </div>
                    <div class="control-group">
                        <label class="control-label" for="tab_title"><fmt:message key="page.general.addpage.title"/></label>
                        <div class="controls">
                            <input id="tab_title" name="tab_title" class="input-xlarge focused required" type="text" value="" />
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label" for="pageLayout"><fmt:message key="page.general.addpage.selectlayout"/></label>
                        <div class="controls">
                            <select name="pageLayout" id="pageLayout">
                                <c:forEach var="pageLayout" items="${pageLayouts}">
                                    <option value="${pageLayout.code}" id="${pageLayout.code}_id">
                                        <fmt:message key="page.general.addpage.layout.${pageLayout.code}"/></option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                </fieldset>
            </form>
        </div>
        <div class="modal-footer">
            <a id="pageMenuCloseButton" href="#" class="btn"><fmt:message key="_rave_client.common.cancel"/></a>
            <a id="pageMenuUpdateButton" href="#" class="btn btn-primary"></a>
        </div>
    </div>

    <div id="movePageDialog" class="modal hide" data-backdrop="static">
        <div class="modal-header">
            <a href="#" class="close" data-dismiss="modal">&times;</a>
            <h3><fmt:message key="page.general.movethispage"/></h3>
        </div>
        <div class="modal-body">
            <form id="movePageForm" class="form-horizontal">
                <fieldset>
                    <div class="control-group">
                        <div class="controls">
                            <select id="moveAfterPageId">
                                <c:if test="${page.renderSequence != 1}">
                                    <option value="-1"><fmt:message key="page.general.movethispage.tofirst"/></option>
                                </c:if>
                                <c:forEach var="userPage" items="${pages}">
                                    <c:if test="${userPage.entityId != page.entityId}">
                                        <option value="${userPage.entityId}">
                                            <fmt:message key="page.general.movethispage.after">
                                                <fmt:param><c:out value="${userPage.name}"/></fmt:param>
                                            </fmt:message>
                                        </option>
                                    </c:if>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                </fieldset>
            </form>
        </div>
        <div class="modal-footer">
            <a href="#" class="btn" onclick="$('#movePageDialog').modal('hide');"><fmt:message key="_rave_client.common.cancel"/></a>
            <a href="#" class="btn btn-primary" onclick="rave.layout.movePage();"><fmt:message key="page.general.movepage"/></a>
        </div>
    </div>

    <fmt:message key="widget.menu.movetopage" var="moveWidgetToPageTitle"/>
    <div id="moveWidgetDialog" title="${moveWidgetToPageTitle}" class="dialog hidden">
        <div><fmt:message key="widget.menu.movethiswidget"/></div>
        <form id="moveWidgetForm">
            <select id="moveToPageId">
                <c:forEach var="userPage" items="${pages}">
                    <c:if test="${userPage.entityId != page.entityId}">
                        <option value="${userPage.entityId}">
                            <c:out value="${userPage.name}"/>
                        </option>
                    </c:if>
                </c:forEach>
            </select>
        </fieldset>
    </form>
</div>
<portal:register-init-script location="${'BEFORE_RAVE'}">
    <script>
        //Define the global widgets map.  This map will be populated by RegionWidgetRender providers.
        var widgetsByRegionIdMap = {};
    </script>
</portal:register-init-script>

<portal:register-init-script location="${'AFTER_RAVE'}">
    <script>
        $(function() {
            rave.initProviders();
            rave.initWidgets(widgetsByRegionIdMap);
            rave.initUI();
            rave.layout.init();
        });
    </script>
</portal:register-init-script>