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

<%--
Template for rendering a Region on a page
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="/WEB-INF/jsp/includes/taglibs.jsp" %>
<%@ attribute name="region" type="org.apache.rave.model.Region" required="true" description="The region object" %>
<%@ attribute name="regionIdx" required="true" description="The regionIdx" %>

<fmt:setBundle basename="messages"/>

<%--@elvariable id="region" type="org.apache.rave.model.Region"--%>
<div class="region<c:if test="${region.locked || pageUser.editor == false}"> region-locked</c:if> <c:out value="${region.page.pageLayout.code}"/>_${regionIdx} regionNonDragging" id="region-${region.id}-id">
    <c:forEach var="regionWidget" items="${region.regionWidgets}">
        <rave:region_widget regionWidget="${regionWidget}" />
    </c:forEach>
</div>
