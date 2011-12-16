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
<%@ page errorPage="/WEB-INF/jsp/views/error.jsp" %>
<%@ include file="/WEB-INF/jsp/includes/taglibs.jsp" %>
<fmt:setBundle basename="messages"/>
<%-- Expose any attributes defined in the tiles-defs.xml to the request scope for use in other tiles --%>
<tiles:importAttribute scope="request"/>
<%--@elvariable id="page" type="org.apache.rave.portal.model.Page"--%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="ISO-8859-1"/>
    <meta name="viewport" content="width=device-width" />
    <title>
        <%-- determine if this is a user page, and if so, display the page name in the HTML title --%>
        <c:choose>
            <c:when test="${not empty page}">
                <c:out value="${page.name}" escapeXml="true" />
            </c:when>
            <c:otherwise>
                <fmt:message key="${pageTitleKey}"/>
            </c:otherwise>
        </c:choose>
        <c:if test="${not empty portalSettings and not empty portalSettings['titleSuffix']}">
            <c:out value="${portalSettings['titleSuffix'].value}"/>
        </c:if>
    </title>
    <link rel="stylesheet" href="//ajax.aspnetcdn.com/ajax/jquery.ui/1.8.13/themes/base/jquery-ui.css"/>
    <link rel="stylesheet" href="<c:url value="/css/default.css" />"/>
    <rave:custom_css/>
    <!--[if lt IE 9]>
    <script src="//html5shiv.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->
</head>
<body>
<%-- Header Content --%>
<tiles:insertAttribute name="header"/>
<%-- Main Body Content --%>
<tiles:insertAttribute name="body"/>
<%-- Footer Content --%>
<tiles:insertAttribute name="footer"/>
</body>
</html>