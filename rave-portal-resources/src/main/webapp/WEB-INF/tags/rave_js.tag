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
<portal:render-script location="${'BEFORE_LIB'}" />
<rave:third_party_js />
<portal:render-script location="${'AFTER_LIB'}" />
<%-- local rave scripts --%>
<portal:render-script location="${'BEFORE_RAVE'}" />
<%-- check to see if the javaScriptDebugMode is on, if so render the individual JS files, otherwise render the minified single file --%>
<c:choose>
    <c:when test="${not empty portalSettings and not empty portalSettings['javaScriptDebugMode'] and portalSettings['javaScriptDebugMode'].value == '0'}">
        <script src="<spring:url value="/script/rave_all.min.js"/>"></script>
    </c:when>
    <c:otherwise>
        <script src="<spring:url value="/script/rave.js"/>"></script>
        <script src="<spring:url value="/script/rave_api.js"/>"></script>
        <script src="<spring:url value="/script/rave_opensocial.js"/>"></script>
        <script src="<spring:url value="/script/rave_wookie.js"/>"></script>
        <script src="<spring:url value="/script/rave_layout.js"/>"></script>
        <script src="<spring:url value="/script/rave_forms.js"/>"></script>
        <script src="<spring:url value="/script/rave_person_profile.js"/>"></script>
        <script src="<spring:url value="/script/rave_store.js"/>"></script>
        <script src="<spring:url value="/script/rave_admin.js"/>"></script>
    </c:otherwise>
</c:choose>
<script src="<spring:url value="/app/messagebundle/rave_client_messages.js"/>"></script>
<%-- bootstrap --%>
<script src="<spring:url value="/css/bootstrap/js/bootstrap.js"/>"></script>
<portal:render-script location="${'AFTER_RAVE'}" />
<%-- common javascript to execute on all pages --%>
<script>
    rave.setContext("<spring:url value="/app/" />");
</script>
