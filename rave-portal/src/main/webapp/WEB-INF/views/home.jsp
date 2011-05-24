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

--%><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%--
--%><%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %><%--
--%><%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %><%--
--%><%@ taglib prefix="portal" uri="http://www.apache.org/rave/tags" %><%--
--%><%@ taglib tagdir="/WEB-INF/tags" prefix="rave"%><%--

--%><jsp:useBean id="pages" type="java.util.List<org.apache.rave.portal.model.Page>" scope="request"/><%--
--%><fmt:setBundle basename="portal" var="portal"/><%--
--%><fmt:message bundle="${portal}" key="portal.opensocial_engine.protocol" var="osProtocol"/><%--
--%><fmt:message bundle="${portal}" key="portal.opensocial_engine.root" var="osRoot"/><%--
--%><fmt:message bundle="${portal}" key="portal.opensocial_engine.gadget_path" var="osGadget"/><%--
--%><c:set var="opensocial_engine_url" value="${osProtocol}://${osRoot}${osGadget}"/><%--

--%><rave:rave_generic_page>
<div id="header">
    <a href="<spring:url value="/j_spring_security_logout" htmlEscape="true" />">Logout</a>
</div>

<c:set var="defaultPage" value="${pages[0]}"/>
<h1>Hello ${defaultPage.owner.username}, welcome to Rave!</h1>
<script type="text/javascript">
    //Define the global widgets variable
    var widgets = [];
</script>
<c:forEach var="region" items="${defaultPage.regions}">
<div class="region" id="region-${region.id}-id" >
    <c:forEach var="regionWidget" items="${region.regionWidgets}">
    <div class="widget-wrapper">
        <div class="widget-title-bar" >
            <span id="widget-${regionWidget.id}-title">${regionWidget.widget.title}</span>
        </div>
        <div class="widget" id="widget-${regionWidget.id}-body">
            <portal:render-widget regionWidget="${regionWidget}" />
        </div>
    </div>
    </c:forEach>
</div>
</c:forEach>
<div class="clear-float">&nbsp;</div>

<script src="${opensocial_engine_url}/js/container.js?c=1&container=default&debug=1" type="text/javascript"></script>
<script src="<spring:url value="/script/rave.js"/>" type="text/javascript"></script>
<script src="<spring:url value="/script/rave_opensocial.js"/>" type="text/javascript"></script>
<script src="<spring:url value="/script/rave_wookie.js"/>" type="text/javascript"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.6.1/jquery.min.js" type="text/javascript"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.8.13/jquery-ui.min.js" type="text/javascript"></script>

<script type="text/javascript">
    $(function(){
        rave.setContext("<spring:url value="/app/" />");
        rave.initProviders();
        rave.initWidgets(rave.createWidgetMap(widgets));
        rave.initDragAndDrop();
    });
</script>
</rave:rave_generic_page>
