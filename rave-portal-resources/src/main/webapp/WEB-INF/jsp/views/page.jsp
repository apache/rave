<%@ taglib prefix="portal" uri="http://www.apache.org/rave/tags" %>
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
<jsp:useBean id="pageUser" type="org.apache.rave.model.PageUser" scope="request"/>
<jsp:useBean id="pageLayouts" type="java.util.List" scope="request"/>

<%--@elvariable id="page" type="org.apache.rave.model.Page"--%>
<sec:authentication property="principal.id" var="principalId" scope="request"/>
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

<input id="currentPageId" type="hidden" value="${page.id}"/>
<c:set var="hasOnlyOnePage" scope="request">
    <c:choose>
        <c:when test="${fn:length(pages) == 1}">true</c:when>
        <c:otherwise>false</c:otherwise>
    </c:choose>
</c:set>
<c:set var="canMoveWidgetsToEditablePage" scope="request" value="false"/>

<div id="pageContent" class="container-fluid">
    <nav>
        <ul class="nav nav-tabs">
            <c:forEach var="userPage" items="${pages}">
                <%-- determine if the current page in the list matches the page the user is viewing --%>
                <c:set var="isCurrentPage">
                    <c:choose>
                        <c:when test="${page.id == userPage.id}">true</c:when>
                        <c:otherwise>false</c:otherwise>
                    </c:choose>
                </c:set>
                <c:set var="isSharedToMe">
                    <c:choose>
                        <c:when test="${userPage.ownerId == principalId}">false</c:when>
                        <c:otherwise>true</c:otherwise>
                    </c:choose>
                </c:set>
                <c:set var="isSharedByMe">
                    <c:choose>
                        <c:when test="${fn:length(userPage.members) > 1 and isSharedToMe == false}">true</c:when>
                        <c:otherwise>false</c:otherwise>
                    </c:choose>
                </c:set>
                <portal:person id="${userPage.ownerId}" var="userPageOwner"/>
                <fmt:message key="sharing.page.tab.icon.tip.from" var="iconShareToolTipFrom">
                    <fmt:param value="${userPageOwner.username}"/>
                </fmt:message>
                <fmt:message key="sharing.page.tab.icon.tip.to" var="iconShareToolTipTo"/>
                <c:choose>
                    <c:when test="${isCurrentPage}">
                        <li id="tab-${userPage.id}" class="active dropdown">
                            <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                                <c:if test="${isSharedToMe}">
                                    <b id="pageMenuSharedIcon" class="ui-icon ui-icon-person"
                                       title="<c:out value="${iconShareToolTipFrom}"/>"></b>
                                </c:if>
                                <c:if test="${isSharedByMe}">
                                    <b id="pageMenuSharedIcon" class="ui-icon ui-icon-folder-open"
                                       title="<c:out value="${iconShareToolTipTo}"/>"></b>
                                </c:if>
                                <c:out value="${userPage.name}"/>
                                <b class="caret"></b>
                            </a>
                            <ul class="dropdown-menu">
                                <li id="pageMenuEdit" class="<c:if test="${isSharedToMe}">menu-item-disabled</c:if>"><a
                                        href="#"><fmt:message key="page.general.editpage"/></a></li>
                                <li id="pageMenuDelete"
                                    class="<c:if test='${hasOnlyOnePage or isSharedToMe}'>menu-item-disabled</c:if>"><a
                                        href="#"><fmt:message key="page.general.deletepage"/></a></li>
                                <li id="pageMenuMove" class="<c:if test='${hasOnlyOnePage}'>menu-item-disabled</c:if>">
                                    <a href="#"><fmt:message key="page.general.movepage"/></a></li>
                                <li id="pageMenuExport" class="hidden"><a href="#"><fmt:message
                                        key="page.general.exportpage"/></a></li>
                                <li id="pageMenuShare" class="<c:if test="${isSharedToMe}">menu-item-disabled</c:if>"><a
                                        href="#sharePageDialog" data-toggle="modal"><fmt:message
                                        key="page.general.sharepage"/></a></li>
                                <li id="pageMenuRevokeShare"
                                    class="<c:if test="${isSharedToMe == false}">menu-item-disabled</c:if>"><a href="#"><fmt:message
                                        key="page.general.removeshare"/></a></li>
                            </ul>
                        </li>
                    </c:when>
                    <c:otherwise>
                        <li id="tab-${userPage.id}">
                            <c:choose>
                                <c:when test="${isSharedToMe}">
                                    <a href="<spring:url value="/app/page/view/${userPage.id}"/>"
                                       class="rave-ui-tab-shared-to-me">
                                        <b id="pageMenuSharedIcon" class="ui-icon ui-icon-person"
                                           title="<c:out value="${iconShareToolTipFrom}"/>"></b>
                                        <c:out value="${userPage.name}"/>
                                    </a>
                                </c:when>
                                <c:when test="${isSharedByMe}">
                                    <a href="<spring:url value="/app/page/view/${userPage.id}" />"
                                       class="rave-ui-tab-shared-by-me">
                                        <b id="pageMenuSharedIcon" class="ui-icon ui-icon-folder-open"
                                           title="<c:out value="${iconShareToolTipTo}"/>"></b>
                                        <c:out value="${userPage.name}"/>
                                    </a>
                                </c:when>
                                <c:otherwise>
                                    <a href="<spring:url value="/app/page/view/${userPage.id}" />"><c:out
                                            value="${userPage.name}"/></a>
                                </c:otherwise>
                            </c:choose>
                        </li>
                    </c:otherwise>
                </c:choose>
                <c:forEach var="members" items="${userPage.members}">
                    <c:if test="${members.userId == principalId and members.editor and userPage.id != page.id}">
                        <c:set var="canMoveWidgetsToEditablePage" scope="request" value="true"/>
                    </c:if>
                </c:forEach>
            </c:forEach>
            <li id="addPageButton"><a href="#"><i class="icon-plus"></i></a></li>
        </ul>
    </nav>
</div>

<div class="row-fluid">
    <div class=" tab-content">
        <div id="emptyPageMessageWrapper" class="emptyPageMessageWrapper hidden">
            <c:if test="${pageUser.pageStatus != 'PENDING'}">
                <div class="emptyPageMessage">
                    <c:choose>
                        <c:when test="${pageUser.editor == true}">
                            <a href="<spring:url value="/app/store?referringPageId=${page.id}" />"><fmt:message
                                    key="page.general.empty"/></a>
                        </c:when>
                        <c:otherwise>
                            <fmt:message key="page.general.non.editing.empty"/>
                        </c:otherwise>
                    </c:choose>
                </div>
            </c:if>
        </div>
        <c:choose>
            <c:when test="${pageUser.pageStatus != 'PENDING'}">
                <div class="regions">
                        <%-- insert the region layout template --%>
                    <tiles:insertTemplate template="${layout}"/>
                </div>
                <div class="clear-float">&nbsp;</div>
            </c:when>
            <c:otherwise>
                <div class="emptyPageMessage">
                    <div>
                        <div id="confirmSharePageLegend">
                            <c:choose>
                                <c:when test="${page.ownerId == principalId}">
                                    <fmt:message key="cloned.page.confirm.message"/>
                                </c:when>
                                <c:otherwise>
                                    <portal:person id="${page.ownerId}" var="owner"/>
                                    <fmt:message key="sharing.page.confirm.message">
                                        <fmt:param value="${owner.username}"/>
                                    </fmt:message>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                    <div>&nbsp;</div>
                    <div>
                        <a href="#" id="acceptShareLink"><fmt:message key="_rave_client.common.accept"/></a>
                    </div>
                    <div>
                        <a href="#" id="declineShareLink"><fmt:message key="_rave_client.common.decline"/></a>
                    </div>
                    <div class="clear-float">&nbsp;</div>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<div id="pageMenuDialogTabbed" class="modal hide" data-backdrop="static">
    <div id="page-tabs">
        <div>
            <a href="#" class="close" data-dismiss="modal">&times;</a>
            <ul>
                <li><a href="#tabs-1"><fmt:message key="page.general.addnewpage"/></a></li>
                <li><a href="#tabs-2"><fmt:message key="page.general.importnewpage"/></a></li>
            </ul>
        </div>
        <div id="tabs-1">
            <div class="modal-body">
                <form id="pageFormTabbed" class="form-horizontal">
                    <input type="hidden" name="tab_idTabbed" id="tab_idTabbed" value=""/>
                    <fieldset>
                        <div class="control-group error">
                            <label id="pageFormErrorsTabbed1" class="control-label"></label>
                        </div>
                        <div class="control-group">
                            <label class="control-label" for="tab_titleTabbed1"><fmt:message
                                    key="page.general.addpage.title"/></label>

                            <div class="controls">
                                <input id="tab_titleTabbed1" name="tab_titleTabbed1"
                                       class="input-xlarge focused required" type="text" value=""/>
                            </div>
                        </div>
                        <div class="control-group">
                            <label class="control-label" for="pageLayoutTabbed"><fmt:message
                                    key="page.general.addpage.selectlayout"/></label>

                            <div class="controls">
                                <select name="pageLayoutTabbed" id="pageLayoutTabbed">
                                    <c:forEach var="pageLayoutTabbed" items="${pageLayouts}">
                                        <option value="${pageLayoutTabbed.code}" id="${pageLayoutTabbed.code}_id">
                                            <fmt:message
                                                    key="page.general.addpage.layout.${pageLayoutTabbed.code}"/></option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                    </fieldset>
                </form>
            </div>
        </div>

        <div id="tabs-2">
            <div class="modal-body">
                <form method="post" id="pageFormImport" class="form-horizontal" enctype="multipart/form-data">
                    <fieldset>
                        <div class="control-group error">
                            <label id="pageFormErrorsTabbed2" class="control-label"></label>
                        </div>
                        <div class="control-group">
                            <label class="control-label" for="tab_titleTabbed2"><fmt:message
                                    key="page.general.addpage.title"/></label>

                            <div class="controls">
                                <input id="tab_titleTabbed2" name="pageName" class="input-xlarge focused required"
                                       type="text" value=""/>
                            </div>
                        </div>
                        <div class="control-group">
                            <label class="control-label" for="omdlFile">Browse for File</label>

                            <div class="controls">
                                <input id="omdlFile" name="omdlFile" class="input-xlarge focused required" type="file"
                                       value=""/>
                            </div>
                        </div>
                        <div class="control-group">
                            <div class="controls">
                                <iframe id="file_upload_frame" name="file_upload_frame" src=""
                                        style="width:0;height:0;border:0px solid black;"></iframe>
                            </div>
                        </div>
                    </fieldset>
                </form>
            </div>
        </div>
        <div class="modal-footer">
            <a id="pageMenuCloseButtonTab" href="#" class="btn"><fmt:message key="_rave_client.common.cancel"/></a>
            <a id="pageMenuUpdateButtonTab" href="#" class="btn btn-primary"></a>
        </div>
    </div>
</div>

<div id="pageMenuDialog" class="modal hide" data-backdrop="static">
    <div class="modal-header">
        <a href="#" class="close" data-dismiss="modal">&times;</a>

        <h3 id="pageMenuDialogHeader"><fmt:message key="page.general.addnewpage"/></h3>
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
                        <input id="tab_title" name="tab_title" class="input-xlarge focused required" type="text"
                               value=""/>
                    </div>
                </div>
                <div class="control-group" id="pageLayoutGroup">
                    <label class="control-label" for="pageLayout"><fmt:message
                            key="page.general.addpage.selectlayout"/></label>

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
                    </div>
                </div>
            </fieldset>
        </form>
    </div>
    <div class="modal-footer">
        <a href="#" class="btn" data-dismiss="modal" data-target="#movePageDialog"><fmt:message
                key="_rave_client.common.cancel"/></a>
        <a href="#" class="btn btn-primary" id="movePageButton"><fmt:message key="page.general.movepage"/></a>
    </div>
</div>

<fmt:message key="widget.menu.movetopage" var="moveWidgetToPageTitle"/>
<div id="moveWidgetModal" class="modal hide" data-backdrop="static">
    <div class="modal-header">
        <a href="#" class="close" data-dismiss="modal">&times;</a>

        <h3><fmt:message key="widget.menu.movethiswidget"/></h3>
    </div>
    <div class="modal-body">
        <form id="moveWidgetForm" class="form-horizontal">
            <fieldset>
                <div class="control-group">
                    <div class="controls">
                        <select id="moveToPageId">
                            <c:forEach var="userPage" items="${pages}">
                                <c:forEach var="members" items="${userPage.members}">
                                    <c:if test="${members.userId == principalId and members.editor and userPage.id != page.id}">
                                        <option value="${userPage.id}">
                                            <c:out value="${userPage.name}"/>
                                        </option>
                                    </c:if>
                                </c:forEach>
                            </c:forEach>
                        </select>
                    </div>
                </div>
            </fieldset>
        </form>
    </div>
    <div class="modal-footer">
        <a href="#" class="btn" data-dismiss="modal" data-target="#moveWidgetModal"><fmt:message
                key="_rave_client.common.cancel"/></a>
        <a href="#" class="btn btn-primary" id="moveWidgetToPageButton"><fmt:message
                key="_rave_client.common.move"/></a>
    </div>
</div>

<div id="sharePageDialog" class="modal hide" data-backdrop="static">
    <div class="modal-header">
        <a href="#" class="close" data-dismiss="modal">&times;</a>

        <h3><fmt:message key="page.general.search.title"/></h3>
    </div>
    <div class="modal-body">
        <div id="sharePageDialogContent">
            <div id="shareContent">
                <div id="searchControls"><input id="searchTerm" name="searchTerm" type="text"/>
                    <input id="shareSearchButton" value="<fmt:message key="page.store.search.button"/>" type="submit"/>
                    <input id="clearSearchButton" value="<fmt:message key="admin.clearsearch"/>" type="submit"
                           class="hide"/>
                </div>
                <div id="shareSearchListHeader"></div>
                <div id="shareSearchListPaging"></div>
                <div id="shareSearchResults"></div>
            </div>
        </div>
    </div>
    <div class="modal-footer">
        <a href="#" class="btn" data-dismiss="modal"><fmt:message key="_rave_client.common.cancel"/></a>
    </div>
</div>

<portal:register-init-script location="${'AFTER_RAVE'}">
<script>
    require(["rave", "ui", 'jquery'], function (rave, ui, $) {
        rave.registerOnInitHandler(function () {
            $('#acceptShareLink').click(function () {
                ui.models.currentPage.acceptShare();
            });


            $('#declineShareLink').click(function () {
                    ui.models.currentPage.declineShare();
                });

                $('#movePageButton').click(function () {
                    ui.models.movePage();
                })

                $('#moveWidgetToPageButton').click(function () {
                    ui.layout.moveWidgetToPage($('#moveWidgetModal').data('regionWidgetId'));
                })

                rave.renderWidgets('home');
            });

            rave.setDefaultView('home');
            rave.setPage({
                id: "${page.id}"
            });
            rave.getViewer().editor =<c:out value="${pageUser.editor}"/>;
            rave.setExportEnabled(${applicationProperties['portal.export.ui.enable']});

            ui.models.currentPage.set({
                id: "${page.id}",
                ownerId: "${page.ownerId}",
                viewerId: "<sec:authentication property="principal.id" />"
            }, {silent: true})

            <c:forEach var="members" items="${page.members}">
            <portal:person id="${members.userId}" var="member"/>
            ui.models.currentPage.addInitData('${member.id}', ${members.editor})
            </c:forEach>
        });
    </script>
</portal:register-init-script>