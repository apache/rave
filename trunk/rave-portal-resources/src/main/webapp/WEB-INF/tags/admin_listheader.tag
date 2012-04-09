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
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<fmt:setBundle basename="messages"/>

<c:choose>
    <c:when test="${empty searchTerm and (empty searchResult or searchResult.totalResults eq 0)}">
        <fmt:message key="admin.list.noresult" var="listheader"/>
    </c:when>
    <c:when test="${empty searchTerm}">
        <fmt:message key="admin.list.result.x.to.y" var="listheader">
            <fmt:param value="${offset + 1}"/>
            <fmt:param value="${offset + fn:length(searchResult.resultSet)}"/>
            <fmt:param value="${searchResult.totalResults}"/>
        </fmt:message>
    </c:when>
    <c:when test="${not empty searchTerm and searchResult.totalResults eq 0}">
        <fmt:message key="admin.list.search.noresult" var="listheader">
            <fmt:param><c:out value="${searchTerm}"/></fmt:param>
        </fmt:message>
    </c:when>
    <c:otherwise>
        <fmt:message key="admin.list.search.result.x.to.y" var="listheader">
            <fmt:param value="${offset + 1}"/>
            <fmt:param value="${offset + fn:length(searchResult.resultSet)}"/>
            <fmt:param value="${searchResult.totalResults}"/>
            <fmt:param><c:out value="${searchTerm}"/></fmt:param>
        </fmt:message>
    </c:otherwise>
</c:choose>
<h2>${listheader}</h2>