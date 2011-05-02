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

--%><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>                                  <%--
--%><%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>                           <%--
--%><%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>                                 <%--
--%><jsp:useBean id="pages" type="java.util.List<org.apache.rave.portal.model.Page>" scope="request"/><%--
--%><fmt:setBundle basename="portal" var="portal" />                                                  <%--
--%><fmt:message bundle="${portal}" key="portal.opensocial_engine.protocol" var="osProtocol"/>        <%--
--%><fmt:message bundle="${portal}" key="portal.opensocial_engine.root"     var="osRoot"/>            <%--
--%><fmt:message bundle="${portal}" key="portal.opensocial_engine.gadget_path" var="osGadget"/>          <%--
--%><c:set var="opensocial_engine_url" value="${osProtocol}://${osRoot}${osGadget}" />
<html>
<head>
  <title>Rave Home</title>
</head>
<body>
<div id="header">
      <a href="<spring:url value="/j_spring_security_logout" htmlEscape="true" />">Logout</a>
</div>

<h1>Hello ${pages[0].owner.username}, welcome to Rave!</h1>

<table>
  <tr>
    <c:forEach var="region" items="${pages[0].regions}">
      <td>
        <c:forEach var="regionWidget" items="${region.regionWidgets}">
          <c:set var="widget" value="${regionWidget.widget}"/>
          <div id="gadget_${widget.id}_chrome">
            ${widget.title}
          </div>
          <div id="gadget_${widget.id}_body">
            <iframe src="${opensocial_engine_url}/ifr?url=${widget.url}&view=home" width="250" height="250" frameborder="0"></iframe>
          </div>
        </c:forEach>
      </td>
    </c:forEach>
  </tr>
</table>

<script src="${opensocial_engine_url}/js/rpc:pubsub.js?c=1" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/script/rave_opensocial.js" type="text/javascript"></script>
</body>
</html>