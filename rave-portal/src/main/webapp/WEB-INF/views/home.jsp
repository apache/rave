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
<script src="${pageContext.request.contextPath}/script/rave_opensocial.js" type="text/javascript"></script>

<script type="text/javascript">
    //TODO: Move much of this code out of this page and into the JS namespace Matt created.

    //Create the common container instance.
    var containerConfig = {};
    containerConfig[osapi.container.ServiceConfig.API_PATH] = "/rpc";
    containerConfig[osapi.container.ContainerConfig.RENDER_DEBUG] = "1";
    var container = new osapi.container.Container(containerConfig);

    //Enumerate all of our regionWidgets and collect up metadata about them.
    var widgets = [];
    var widgetUrls = [];
    <c:forEach var="region" items="${defaultPage.regions}">
        <c:forEach var="regionWidget" items="${region.regionWidgets}">
            widgets.push({regionWidgetId: ${regionWidget.id}, widgetUrl: "${regionWidget.widget.url}", userPrefs: {}});
            widgetUrls.push("${regionWidget.widget.url}");
        </c:forEach>
    </c:forEach>

    /**
     * Tell the common container to pre-load the metadata for all the widgets we're going to ask it to render.  If we
     * don't do this then when we call navigateGadget for each regionWidget the common container will fetch the metadata
     * for each one at a time.  We also pass a callback function which will take the metadata retrieved from the preload
     * so we can get all the default values for userPrefs and pass them along with our navigateGadget call.
     *
     * TODO: Prime the common container metadata cache with data we pull from our own persistent store so that we dont have
     * to have common container fetch metadata on every page render.  See osapi.container.Container.prototype.preloadFromConfig_
     * function which gets called from the osapi.container.Container constructor to get an idea of how this might be done.
     *
     * TODO: Use real userPrefs that we pull from our persistent store instead of the default values pulled from common
     * containers metadata call.
     *
     * TODO: Get real moduleId's based on the regionWidget.id into the iframe URL.  Right now common container uses an
     * internal counter for the mid parameter value with no external way to set it.
    */
    container.preloadGadgets(widgetUrls, function(response) {
        for (var i = 0; i < widgets.length; i++) {
            var widget = widgets[i];
            var widgetMetadata = response[widget.widgetUrl];

            for (var userPref in widgetMetadata.userPrefs) {
                userPref = widgetMetadata.userPrefs[userPref];
                widget.userPrefs[userPref.name] = userPref.defaultValue;
            }

            var renderParams = {};
            renderParams[osapi.container.RenderParam.VIEW] = "home";
            renderParams[osapi.container.RenderParam.WIDTH] = 250;
            renderParams[osapi.container.RenderParam.HEIGHT] = 250;
            renderParams[osapi.container.RenderParam.USER_PREFS] = widget.userPrefs;
            var widgetBodyElement = document.getElementById(["widget-", widget.regionWidgetId, "-body"].join(""));
            var gadgetSite = container.newGadgetSite(widgetBodyElement);
            container.navigateGadget(gadgetSite , widget.widgetUrl, {}, renderParams);
        }
    });
</script>

</body>
</html>