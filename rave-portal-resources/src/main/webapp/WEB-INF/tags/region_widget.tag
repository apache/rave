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

<%--
Template for rendering a RegionWidget including wrapper chrome, toolbars, menus, etc as well as the widget content
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="portal" uri="http://www.apache.org/rave/tags" %>
<%@ attribute name="regionWidget" type="org.apache.rave.portal.model.RegionWidget" required="true" description="The regionWidget object" %>

<c:set var="isLocked" value="${regionWidget.locked}" />
<c:set var="renderTitle" value="${regionWidget.renderTitle}" />
<fmt:setBundle basename="messages"/>
<%--@elvariable id="regionWidget" type="org.apache.rave.portal.model.RegionWidget"--%>
<div class="widget-wrapper<c:if test="${isLocked}"> widget-wrapper-locked</c:if>" id="widget-${regionWidget.entityId}-wrapper">

    <c:if test="${renderTitle}">
        <div class="widget-title-bar<c:if test="${isLocked}"> widget-title-bar-locked</c:if>">
            <c:if test="${!isLocked}">
                <div id="widget-${regionWidget.entityId}-collapse" class="widget-toolbar-toggle-collapse" title="<fmt:message key="widget.chrome.toggle"/>"></div>
            </c:if>
            <div id="widget-${regionWidget.entityId}-title" class="widget-title">
                <c:choose>
                    <c:when test="${not empty regionWidget.widget.titleUrl}">
                        <a href="<c:out value="${regionWidget.widget.titleUrl}"/>" rel="external"><c:out value="${regionWidget.widget.title}"/></a>
                    </c:when>
                    <c:otherwise>
                        <c:out value="${regionWidget.widget.title}"/>
                    </c:otherwise>
                </c:choose>
            </div>

            <%-- These are toolbar buttons --%>
            <div id="widget-${regionWidget.entityId}-toolbar" class="widget-toolbar <c:if test="${isLocked}">hidden</c:if>">
                <div id="widget-${regionWidget.entityId}-widget-menu-wrapper" class="dropdown widget-menu-wrapper">
                    <a id="widget-${regionWidget.entityId}-menu-button" class="dropdown-toggle" data-toggle="dropdown">
                        <i class="icon-cog"></i>
                    </a>
                    <ul id="widget-${regionWidget.entityId}-menu" class="dropdown-menu widget-menu">
                        <%--
                            By default the edit prefs item is disabled.
                            Each provider's widget initialization will be responsible for enabling this item
                            if the widget has preferences to be edited
                        --%>
                        <li id="widget-${regionWidget.entityId}-menu-editprefs-item" class="menu-item-disabled">
                            <a href="#">
                                <fmt:message key="widget.menu.editprefs"/>
                            </a>
                        </li>
                        <li id="widget-${regionWidget.entityId}-menu-maximize-item">
                            <a href="#">
                                <fmt:message key="widget.menu.maximize"/>
                            </a>
                        </li>
                        <li id="widget-${regionWidget.entityId}-menu-move-item" <c:if test='${hasOnlyOnePage}'>class="menu-item-disabled"</c:if>>
                            <a href="#">
                                <fmt:message key="widget.menu.movetopage"/>
                            </a>
                        </li>
                        <li id="widget-${regionWidget.entityId}-menu-delete-item">
                            <a href="#">
                                <fmt:message key="widget.menu.delete"/>
                            </a>
                        </li>
                        <li class="divider"></li>
                        <li id="widget-${regionWidget.entityId}-menu-about-item">
                            <a href="#">
                                <fmt:message key="widget.menu.about"/>
                            </a>
                        </li>
                    </ul>
                </div>

                <%-- the minimize widget button, which is hidden by default and only displays in maximized view --%>
                <span id="widget-${regionWidget.entityId}-min" class="widget-toolbar-btn widget-toolbar-btn-min">
                    <i class="icon-resize-small"></i>
                </span>
            </div>

            <%-- if widget is disabled then display notification in titlebar --%>
            <c:if test="${regionWidget.widget.disableRendering}">
                <div id="widget-${regionWidget.entityId}-disabled" class="widget-disabled-icon ui-icon ui-icon-alert" title="<fmt:message key="widget.chrome.disabled"/>"></div>
            </c:if>
        </div>
    </c:if>

    <div class="widget-prefs" id="widget-${regionWidget.entityId}-prefs-content"></div>
    <div class="widget" id="widget-${regionWidget.entityId}-body">
        <portal:render-widget regionWidget="${regionWidget}" />
    </div>
</div>