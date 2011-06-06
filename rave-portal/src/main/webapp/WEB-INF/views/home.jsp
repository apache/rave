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
<%@ taglib prefix="portal" uri="http://www.apache.org/rave/tags" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="rave"%>
<jsp:useBean id="pages" type="java.util.List<org.apache.rave.portal.model.Page>" scope="request"/>
<fmt:setBundle basename="portal" var="portal"/>
<fmt:message bundle="${portal}" key="portal.opensocial_engine.protocol" var="osProtocol"/>
<fmt:message bundle="${portal}" key="portal.opensocial_engine.root" var="osRoot"/>
<fmt:message bundle="${portal}" key="portal.opensocial_engine.gadget_path" var="osGadget"/>
<c:set var="opensocial_engine_url" value="${osProtocol}://${osRoot}${osGadget}"/>

<rave:rave_generic_page>
<c:set var="defaultPage" value="${pages[0]}"/>
<div id="header">
    <a href="<spring:url value="/j_spring_security_logout" htmlEscape="true" />">Logout</a>
    <a href="<spring:url value="/app/store?referringPageId=${defaultPage.id}" />">Widget Store</a>
</div>

<script src="${opensocial_engine_url}/js/container.js?c=1&container=default&debug=1"></script>
<script src="<spring:url value="/script/rave.js"/>"></script>
<script src="<spring:url value="/script/rave_api.js"/>"></script>
<script src="<spring:url value="/script/rave_opensocial.js"/>"></script>
<script src="<spring:url value="/script/rave_wookie.js"/>"></script>
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.6.1/jquery.min.js"></script>
<script src="//ajax.googleapis.com/ajax/libs/jqueryui/1.8.13/jquery-ui.min.js"></script>

<h1>Hello ${defaultPage.owner.username}, welcome to Rave!</h1>
<script>
    //Define the global widgets variable
	 //This array will be populated by RegionWidgetRender providers.
    var widgets = [];
</script>
<c:forEach var="region" items="${defaultPage.regions}">
<div class="region" id="region-${region.id}-id" >
    <c:forEach var="regionWidget" items="${region.regionWidgets}">
    <!-- FIXME: whether region.id should be included in the id. 
         I am not sure whether id's of widgets in different regions can be the same.
         If so, region.id must be included. When a gadget is moved, region id must be updated. 
         Otherwise, it is not needed.
    -->
    <div class="widget-wrapper" id="widget-wrapper-${regionWidget.id}">
        <div class="widget-title-bar" >
            <span id="widget-${regionWidget.id}-title">${regionWidget.widget.title}</span>
            
				<!-- These are toolbar buttons -->
				<span id="widget-${regionWidget.id}-toolbar" style="float:right;">
				  <button id="widget-${regionWidget.id}-max" 
							 class="widget-toolbar-btn"
							 onclick="rave.toolbarMaximize(this,{myRegionWidgetId:${regionWidget.id},myRegionId:${region.id},myPageId:${defaultPage.id}})">
				  </button>
				  <button id="widget-${regionWidget.id}-remove" 
							 class="widget-toolbar-btn"
							 onclick="rave.toolbarDelete(this,{myRegionWidgetId:${regionWidget.id},myRegionId:${region.id},myPageId:${defaultPage.id}})">
				  </button>
				  <script>
					 //This decorates the toolbar buttons.  As currently written,
					 //it needs to be in the forEach loop.
					 $("#widget-${regionWidget.id}-max").button({
					 text: false,
					 icons: {
					 primary: "ui-icon-arrow-4-diag"
					 }
					 });

					 $("#widget-${regionWidget.id}-remove").button({
					 text: false,
					 icons: {
					 primary: "ui-icon-close"
					 }
					 });
					 rave.mapGadgetToRegion("${regionWidget.id}", "${region.id}");
				  </script>

				</span>
		  </div>
        <div class="widget" id="widget-${regionWidget.id}-body">
			 <!-- 
					Among other things, the render-widget tag will populate the widgets[] array.  
					See the markup text in OpenSocialWidgetRenderer.java, for example.
			 -->
            <portal:render-widget regionWidget="${regionWidget}" />
        </div>
    </div>
    </c:forEach>
</div>
</c:forEach>
<div class="clear-float">&nbsp;</div>

<script>
    $(function(){
        rave.setContext("<spring:url value="/app/" />");
        rave.initProviders();
        rave.initWidgets(rave.createWidgetMap(widgets));
        rave.initDragAndDrop();
    });
</script>
</rave:rave_generic_page>
