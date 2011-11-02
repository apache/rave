<!DOCTYPE html>
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
<%@ tag trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="rave" tagdir="/WEB-INF/tags" %>
<%@ attribute name="pageTitle" required="false" description="The title of the page, will appear in the title bar" %>
<fmt:setBundle basename="messages"/>
<%--
This tag will provide simple template layouts for pages that use it. See for example home.jsp.
--%>
<html>
  <head>
     <meta charset="ISO-8859-1"/>
     <title><c:out value="${pageTitle} "/> <fmt:message key="page.general.titlesuffix"/></title>
     <link rel="stylesheet" href="//ajax.aspnetcdn.com/ajax/jquery.ui/1.8.13/themes/base/jquery-ui.css"/>
     <link rel="stylesheet" href="<c:url value="/css/default.css" />" />
     <rave:custom_css/>
     <!--[if lt IE 9]>
     <script src="//html5shiv.googlecode.com/svn/trunk/html5.js"></script>
     <![endif]-->
  </head>
  <body>
  <jsp:doBody/>
  </body>
</html>
