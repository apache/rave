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
Template for rendering all regions for layout templates that don't have a complex layout.  This template will
simply render each region one after another and rely on CSS to style and position them correctly.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/WEB-INF/jsp/includes/taglibs.jsp" %>
<%@ attribute name="page" type="org.apache.rave.model.Page" required="true" description="The Page object" %>

<%--@elvariable id="region" type="org.apache.rave.model.Region"--%>
<div class="widgetRow bottomRow">
    <c:forEach var="region" items="${page.regions}" varStatus="status">
        <rave:region region="${region}" regionIdx="${status.count}" />
    </c:forEach>
</div>