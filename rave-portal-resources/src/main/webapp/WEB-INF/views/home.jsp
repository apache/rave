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


--%><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%--
--%><%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %><%--
--%><%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %><%--
--%><%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %><%--
--%><%@ taglib prefix="portal" uri="http://www.apache.org/rave/tags" %><%--
--%><%@ taglib tagdir="/WEB-INF/tags" prefix="rave"%><%--
--%><jsp:useBean id="pages" type="java.util.List<org.apache.rave.portal.model.Page>" scope="request"/><%--
--%><fmt:setBundle basename="messages"/>
<%--@elvariable id="page" type="org.apache.rave.portal.model.Page"--%>
<rave:rave_generic_page pageTitle="${page.name}">
    <div id="header">
        <div class="header-a">
            <a href="<spring:url value="/j_spring_security_logout" htmlEscape="true" />">
              <fmt:message key="page.general.logout"/></a>
        </div>
        <div class="widget-a">
            <a href="<spring:url value="/app/store?referringPageId=${page.entityId}" />">
              <fmt:message key="page.store.title"/>
            </a>
        </div>
      <h1>
          <fmt:message key="page.home.welcome"><fmt:param><c:out value="${page.owner.username}"/></fmt:param></fmt:message>
      </h1>
    </div>
    <input id="currentPageId" type="hidden" value="${page.entityId}" />
    <div id="tabsHeader">
        <%-- render the page tabs --%>
        <div id="tabs" class="rave-ui-tabs">
            <c:forEach var="userPage" items="${pages}">
                 <%-- determine if the current page in the list matches the page the user is viewing --%>
                 <c:set var="isCurrentPage">
                     <c:choose>
                         <c:when test="${page.entityId == userPage.entityId}">true</c:when>
                         <c:otherwise>false</c:otherwise>
                     </c:choose>
                 </c:set>
                 <c:set var="hasOnlyOnePage">
                      <c:choose>
                         <c:when test="${fn:length(pages) == 1}">true</c:when>
                         <c:otherwise>false</c:otherwise>
                     </c:choose>
                 </c:set>
                 <div id="tab-${userPage.entityId}" class="rave-ui-tab<c:if test="${isCurrentPage}"> rave-ui-tab-selected</c:if>">
                    <div id="pageTitle-${userPage.entityId}" class="page-title" onclick="rave.viewPage(${userPage.entityId});"><c:out value="${userPage.name}"/></div>
                    <c:if test="${isCurrentPage}">
                        <div id="pageMenuWrapper">
                            <span id="pageMenuButton" class="ui-icon ui-icon-circle-triangle-s" title="Page Actions Menu"></span>
                            <div id="pageMenu" class="page-menu">
                                <div id="pageMenuEdit" class="page-menu-item"><fmt:message key="page.general.editpage"/></div>
                                <div id="pageMenuDelete" class="page-menu-item<c:if test='${hasOnlyOnePage}'> page-menu-item-disabled</c:if>">
                                  <fmt:message key="page.general.deletepage"/></div>
                                <div id="pageMenuMove" class="page-menu-item<c:if test='${hasOnlyOnePage}'> page-menu-item-disabled</c:if>">
                                  <fmt:message key="page.general.movepage"/></div>
                            </div>
                        </div>
                    </c:if>
                </div>
            </c:forEach>
            <%-- display the add page button at the end of the tabs --%>
            <fmt:message key="page.general.addnewpage" var="addNewPageTitle"/>
            <button id="add_page" title="${addNewPageTitle}" style="display: none;"></button>
        </div>
    </div>
    <%--render the main page content (regions/widgets) --%>
    <div id="pageContent">
        <c:forEach var="region" items="${page.regions}">
            <div class="region" id="region-${region.entityId}-id">
                <c:forEach var="regionWidget" items="${region.regionWidgets}">
                   <div class="widget-wrapper" id="widget-${regionWidget.entityId}-wrapper">
                        <div class="widget-title-bar">
                            <span id="widget-${regionWidget.entityId}-collapse" class="widget-toolbar-toggle-collapse" title="Collapse/Restore Widget"></span>
                            <div id="widget-${regionWidget.entityId}-title" class="widget-title">
                            <c:choose>
                                <c:when test="${not empty regionWidget.widget.titleUrl}">
                                    <a href="${regionWidget.widget.titleUrl}" rel="external"><c:out value="${regionWidget.widget.title}"/></a>
                                </c:when>
                                <c:otherwise>
                                    <c:out value="${regionWidget.widget.title}"/>
                                </c:otherwise>
                            </c:choose>
                            </div>
                            <!-- These are toolbar buttons -->
                            <div id="widget-${regionWidget.entityId}-toolbar" style="float:right;">
                                <button id="widget-${regionWidget.entityId}-prefs"
                                        class="widget-toolbar-btn widget-toolbar-btn-prefs">
                                </button>
                                <button id="widget-${regionWidget.entityId}-max"
                                        class="widget-toolbar-btn">
                                </button>
                                <button id="widget-${regionWidget.entityId}-remove"
                                        class="widget-toolbar-btn">
                                </button>
                            </div>
                        </div>
                        <div class="widget-prefs" id="widget-${regionWidget.entityId}-prefs-content"></div>
                        <div class="widget" id="widget-${regionWidget.entityId}-body">
                                <portal:render-widget regionWidget="${regionWidget}" />
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:forEach>
        <div class="clear-float">&nbsp;</div>
    </div>
    <fmt:message key="page.general.addnewpage" var="addNewPageTitle"/>
    <div id="dialog" title="${addNewPageTitle}" class="dialog">
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
    <div id="movePageDialog" title="${movePageTitle}" class="dialog">
        <div><fmt:message key="page.general.movethispage"/></div>
        <form id="movePageForm">
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
        </form>
    </div>
    <script>
        //Define the global widgets variable
        //This array will be populated by RegionWidgetRender providers.
        var widgets = [];
    </script>
    <portal:render-script location="${'BEFORE_LIB'}" />
    <script src="//cdnjs.cloudflare.com/ajax/libs/json2/20110223/json2.js"></script>
    <script src="//ajax.aspnetcdn.com/ajax/jQuery/jquery-1.6.1.min.js"></script>
    <script src="//ajax.aspnetcdn.com/ajax/jquery.ui/1.8.13/jquery-ui.min.js"></script>
    <script src="//ajax.aspnetcdn.com/ajax/jquery.validate/1.8.1/jquery.validate.min.js"></script>
    <portal:render-script location="${'AFTER_LIB'}" />
    <portal:render-script location="${'BEFORE_RAVE'}" />
    <script src="<spring:url value="/script/rave.js"/>"></script>
    <script src="<spring:url value="/script/rave_api.js"/>"></script>
    <script src="<spring:url value="/script/rave_opensocial.js"/>"></script>
    <script src="<spring:url value="/script/rave_wookie.js"/>"></script>
    <script src="<spring:url value="/script/rave_layout.js"/>"></script>
    <portal:render-script location="${'AFTER_RAVE'}" />
    <script>
        $(function() {
            rave.setContext("<spring:url value="/app/" />");
            rave.initProviders();
            rave.initWidgets(widgets);
            rave.initUI();
            rave.layout.init();
        });
    </script>
</rave:rave_generic_page>
