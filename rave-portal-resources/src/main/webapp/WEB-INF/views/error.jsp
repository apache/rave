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
<%@ page isErrorPage="true" trimDirectiveWhitespaces="true" contentType="text/html" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="portal" uri="http://www.apache.org/rave/tags" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="rave"%>
<fmt:setBundle basename="messages"/>
<rave:rave_generic_page pageTitle="Error ${requestScope['javax.servlet.error.status_code']}: ${requestScope['javax.servlet.error.exception_type'].simpleName}">
    <header>
      <h1>
          <fmt:message key="page.error.title" />
      </h1>
    </header>
    <div id="content" >
        <div id="errorMessage">
            <div class="errorMessage">
                <fmt:message key="page.error.message" />
                &nbsp;<a href="javascript: window.location.reload();"><fmt:message key="page.error.reload" /></a>.<br/>
                <a onclick="document.getElementById('errorStack').style.display = 'block';"><fmt:message key="page.error.details" /></a>
            </div>
        </div>
        <div id="errorStack" title="Error Details" >
            <pre class="errorTrace">
                <c:out value="${requestScope['javax.servlet.error.message']}"/>

                <c:forEach var="i" items="${requestScope['javax.servlet.error.exception'].stackTrace}">
                    <c:out value="${i}" /><br />
                </c:forEach>
            </pre>
            <button type="button"
                    onClick="document.getElementById('errorStack').style.display = 'none';">Close
            </button>
        </div>
    </div>
</rave:rave_generic_page>
