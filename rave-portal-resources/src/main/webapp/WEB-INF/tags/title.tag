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
<fmt:setBundle basename="messages"/>
<c:set var="profileTitleKey" value="page.profile.title" />
<c:set var="personProfileTitleKey" value="page.personProfile.title" />
<%--@elvariable id="page" type="org.apache.rave.model.Page"--%>
<%-- determine if this is a user page, and if so, display the page name in the HTML title --%>
<c:choose>
    <c:when test="${not empty page}">
        <c:out value="${page.name}" escapeXml="true" />
    </c:when>
    <c:when test="${pageTitleKey == profileTitleKey}">
        <fmt:message key="${pageTitleKey}">
            <fmt:param><c:out value="${userProfile.displayName}" /></fmt:param>
        </fmt:message>
    </c:when>
    <c:when test="${pageTitleKey == personProfileTitleKey}">
        <fmt:message key="${pageTitleKey}">
            <fmt:param><c:out value="${userProfile.displayName}" /></fmt:param>
        </fmt:message>
    </c:when>
    <c:otherwise>
        <fmt:message key="${pageTitleKey}"/>
    </c:otherwise>
</c:choose>
<c:if test="${not empty portalSettings and not empty portalSettings['titleSuffix']}">
    <c:out value="${portalSettings['titleSuffix'].value}"/>
</c:if>