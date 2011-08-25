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

  $Id$

--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="portal" uri="http://www.apache.org/rave/tags" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="rave"%>
<jsp:useBean id="pages" type="java.util.List<org.apache.rave.portal.model.Page>" scope="request"/>
<jsp:useBean id="openSocialEnv" scope="request" type="org.apache.rave.provider.opensocial.config.OpenSocialEnvironment"/>
<c:set var="opensocial_engine_url" value="${openSocialEnv.engineProtocol}://${openSocialEnv.engineRoot}${openSocialEnv.engineGadgetPath}"/>

<rave:rave_generic_page pageTitle="Rave - ${page.name}">    
    <div id="header">
        <div class="header-a">
            <a href="<spring:url value="/j_spring_security_logout" htmlEscape="true" />">Logout</a>
        </div>
        <div class="widget-a">
            <a href="<spring:url value="/app/store?referringPageId=${page.id}" />">Widget Store</a>
        </div>
        <h1>Hello <c:out value="${page.owner.username}"/>, welcome to Rave!</h1>
    </div>    
    <input id="currentPageId" type="hidden" value="${page.id}" />
    <div id="tabsHeader">      
        <%-- render the page tabs --%>
        <div id="tabs" class="rave-ui-tabs">
            <c:forEach var="userPage" items="${pages}">
                 <%-- determine if the current page in the list matches the page the user is viewing --%>
                 <c:set var="isCurrentPage">
                     <c:choose>
                         <c:when test="${page.id == userPage.id}">true</c:when>
                         <c:otherwise>false</c:otherwise>
                     </c:choose>
                 </c:set>      
                 <c:set var="hasOnlyOnePage">
                      <c:choose>
                         <c:when test="${fn:length(pages) == 1}">true</c:when>
                         <c:otherwise>false</c:otherwise>
                     </c:choose>
                 </c:set>                       
                 <div id="tab-${userPage.id}" class="rave-ui-tab<c:if test="${isCurrentPage}"> rave-ui-tab-selected</c:if>">
                    <div id="pageTitle-${userPage.id}" class="page-title" onclick="rave.viewPage(${userPage.id});"><c:out value="${userPage.name}"/></div>
                    <c:if test="${isCurrentPage}">                   
                        <div id="pageMenuWrapper">
                            <span id="pageMenuButton" class="ui-icon ui-icon-circle-triangle-s" title="Page Actions Menu"></span>
                            <div id="pageMenu" class="page-menu">
                                <div id="pageMenuEdit" class="page-menu-item">Edit Page</div>
                                <div id="pageMenuDelete" class="page-menu-item<c:if test='${hasOnlyOnePage}'> page-menu-item-disabled</c:if>">Delete Page</div>
                                <div id="pageMenuMove" class="page-menu-item<c:if test='${hasOnlyOnePage}'> page-menu-item-disabled</c:if>">Move Page</div>
                            </div>
                        </div>
                    </c:if>
                </div>
            </c:forEach>        
            <%-- display the add page button at the end of the tabs --%>
            <button id="add_page" title="Add a New Page" style="display: none;"></button>
        </div>   
    </div>
    <%--render the main page content (regions/widgets) --%>
    <div id="pageContent">
        <c:forEach var="region" items="${page.regions}">
            <div class="region" id="region-${region.id}-id">
                <c:forEach var="regionWidget" items="${region.regionWidgets}">
                    <div class="widget-wrapper" id="widget-${regionWidget.id}-wrapper">
                        <div class="widget-title-bar">
                            <span id="widget-${regionWidget.id}-title"><c:out value="${regionWidget.widget.title}"/></span>
                            <!-- These are toolbar buttons -->
                            <div id="widget-${regionWidget.id}-toolbar" style="float:right;">
                                <button id="widget-${regionWidget.id}-prefs"
                                        class="widget-toolbar-btn widget-toolbar-btn-prefs">
                                </button>
                                <button id="widget-${regionWidget.id}-max"
                                        class="widget-toolbar-btn">
                                </button>
                                <button id="widget-${regionWidget.id}-remove"
                                        class="widget-toolbar-btn">
                                </button>
                            </div>
                        </div>
                        <div class="widget-prefs" id="widget-${regionWidget.id}-prefs-content"></div>
                        <div class="widget" id="widget-${regionWidget.id}-body">
                                <%-- Widget will be rendered here --%>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:forEach>
        <div class="clear-float">&nbsp;</div>
    </div>
    <div id="dialog" title="Add a New Page" class="dialog">
        <form id="pageForm">
            <div id="pageFormErrors" class="error"></div>
            <fieldset class="ui-helper-reset">
                <label for="tab_title">Title</label>
                <input type="text" name="tab_title" id="tab_title" value="" class="required ui-widget-content ui-corner-all" />
                <label for="pageLayout">Select Page Layout:</label>
                <select name="pageLayout" id="pageLayout">
                    <option value="columns_1" id="columns_1_id">One Column</option>
                    <option value="columns_2" id="columns_2_id" selected="selected">Two Columns</option>
                    <option value="columns_2wn" id="columns_2wn_id">Two Columns (wide/narrow)</option>
                    <option value="columns_3" id="columns_3_id">Three Columns</option>
                    <option value="columns_3nwn" id="columns_3nwn_id">Three Columns (narrow/wide/narrow)</option>
                    <option value="columns_4" id="columns_4_id">Four Columns</option>
                    <option value="columns_3nwn_1_bottom" id="columns_3nwn_1_bottom">Four Columns (narrow/wide/narrow/bottom)</option>
                </select>
            </fieldset>
        </form>
    </div>    
    <div id="movePageDialog" title="Move Page" class="dialog">
        <div>Move this page:</div>
        <form id="movePageForm">
            <select id="moveAfterPageId">
                <c:if test="${page.renderSequence != 1}">
                    <option value="-1">To First Tab (Set as Default)</option>
                </c:if>
                <c:forEach var="userPage" items="${pages}">
                    <c:if test="${userPage.id != page.id}">
                        <option value="${userPage.id}">After <c:out value="${userPage.name}"/></option>
                    </c:if>
                </c:forEach>
            </select>
        </form>
    </div>
    <script src="//cdnjs.cloudflare.com/ajax/libs/json2/20110223/json2.js"></script>
    <script src="//ajax.aspnetcdn.com/ajax/jQuery/jquery-1.6.1.min.js"></script>
    <script src="//ajax.aspnetcdn.com/ajax/jquery.ui/1.8.13/jquery-ui.min.js"></script>
    <script src="//ajax.aspnetcdn.com/ajax/jquery.validate/1.8.1/jquery.validate.min.js"></script>
    <script src="${opensocial_engine_url}/js/container.js?c=1&amp;container=default&amp;debug=1"></script>
    <script src="<spring:url value="/script/rave.js"/>"></script>
    <script src="<spring:url value="/script/rave_api.js"/>"></script>
    <script src="<spring:url value="/script/rave_opensocial.js"/>"></script>
    <script src="<spring:url value="/script/rave_wookie.js"/>"></script>
    <script src="<spring:url value="/script/rave_layout.js"/>"></script>
    <script>
        //Define the global widgets variable
        //This array will be populated by RegionWidgetRender providers.
        var widgets = [];
        <%--
           Among other things, the render-widget tag will populate the widgets[] array.
           See the markup text in OpenSocialWidgetRenderer.java, for example.
        --%>

        <c:forEach var="region" items="${page.regions}">
            <c:forEach var="regionWidget" items="${region.regionWidgets}">
                <portal:render-widget regionWidget="${regionWidget}" />
            </c:forEach>
        </c:forEach>

        $(function() {
            rave.setContext("<spring:url value="/app/" />");
            rave.initProviders();
            rave.initWidgets(widgets);
            rave.initUI();  
            rave.layout.init();            
            rave.forms.validateUserProfileForm();
        });     
    </script>
</rave:rave_generic_page>
