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
--%><jsp:useBean id="pages" type="java.util.List<org.apache.rave.portal.model.Page>" scope="request"/><%--
--%><fmt:setBundle basename="portal" var="portal"/><%--
--%><fmt:message bundle="${portal}" key="portal.opensocial_engine.protocol" var="osProtocol"/><%--
--%><fmt:message bundle="${portal}" key="portal.opensocial_engine.root" var="osRoot"/><%--
--%><fmt:message bundle="${portal}" key="portal.opensocial_engine.gadget_path" var="osGadget"/><%--
--%><c:set var="opensocial_engine_url" value="${osProtocol}://${osRoot}${osGadget}"/>
<html>
<head>
    <title>Rave Home</title>
</head>
<body>

<div id="header">
    <a href="<spring:url value="/j_spring_security_logout" htmlEscape="true" />">Logout</a>
</div>

<c:set var="defaultPage" value="${pages[0]}"/>
<h1>Hello ${defaultPage.owner.username}, welcome to Rave!</h1>

<table>
    <tr>
        <c:forEach var="region" items="${defaultPage.regions}">
            <td>
                <c:forEach var="regionWidget" items="${region.regionWidgets}">
                    <div id="widget-${regionWidget.id}-chrome">
                            ${regionWidget.widget.title}
                    </div>
                    <div id="widget-${regionWidget.id}-body"></div>
                </c:forEach>
            </td>
        </c:forEach>
    </tr>
</table>

<script src="${opensocial_engine_url}/js/container.js?c=1&container=default&debug=1" type="text/javascript"></script>
<script src="<spring:url value="/script/rave.js"/>" type="text/javascript"></script>
<script src="<spring:url value="/script/rave_opensocial.js"/>" type="text/javascript"></script>
<script src="<spring:url value="/script/rave_wookie.js"/>" type="text/javascript"></script>

<script type="text/javascript">
    rave.opensocial.init();

    //Enumerate all of our regionWidgets and collect up metadata about them.
    var widgets = [];
    <c:forEach var="region" items="${defaultPage.regions}">
        <c:forEach var="regionWidget" items="${region.regionWidgets}">
            widgets.push({type: '${regionWidget.widget.type}', regionWidgetId: ${regionWidget.id}, widgetUrl: "${regionWidget.widget.url}", userPrefs: {}});
        </c:forEach>
    </c:forEach>
    //Get a map of widgets keyed by their type
    var widgetMap = rave.createWidgetMap(widgets);
    rave.opensocial.initGadgets(widgetMap[rave.opensocial.TYPE]);
    rave.wookie.initWidgets(widgetMap[rave.wookie.TYPE]);
</script>

</body>
</html>