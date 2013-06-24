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
<%@ include file="/WEB-INF/jsp/includes/taglibs.jsp" %>
<portal:render-script location="${'BEFORE_LIB'}"/>
<rave:third_party_js/>
<portal:render-script location="${'AFTER_LIB'}"/>
<%-- local rave scripts --%>
<portal:render-script location="${'BEFORE_RAVE'}"/>
<%-- get the javaScriptDebugMode portal preference value --%>
<c:set var="jsDebugMode"><portal:render-js-debug-mode/></c:set>
<%-- check to see if the javaScriptDebugMode is on, if so render the individual JS files, otherwise render the minified single file --%>
<c:choose>
    <c:when test="${jsDebugMode == '1'}">
        <!--TODO: figure out js debug mode -->
    </c:when>
    <c:otherwise>

    </c:otherwise>
</c:choose>
<script src="<spring:url value="/static/script/app.js"/>"></script>
<script>
    require(["rave"], function (rave) {
        <%-- set the web application context --%>
        rave.setContext("<spring:url value="/app/" />");
        <%-- set the default widget height so js code has access to it --%>
        rave.setDefaultHeight(<c:out value="${portalSettings['defaultWidgetHeight'].value}"/>);
        <%-- set the current page viewer --%>
        <sec:authorize access="isAuthenticated()">
        <sec:authentication property="principal.username" scope="request" var="username"/>
        <sec:authentication property="principal.id" scope="request" var="id"/>
        rave.setViewer({username: "${username}", id: "${id}"});
        </sec:authorize>

        <%-- set the javascript debug mode so js code has access to it --%>
        <%--ui.setJavaScriptDebugMode(<c:out value="${jsDebugMode}"/>);--%>
    });
</script>
<portal:render-script location="${'AFTER_RAVE'}"/>
<%-- common javascript to execute on all pages --%>

