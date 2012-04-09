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
<fmt:setBundle basename="messages"/>
<div id="tabsHeader">
    <nav>
        <%--@elvariable id="tabs" type="org.apache.rave.portal.web.model.NavigationMenu"--%>
        <c:if test="${not empty tabs}">
            <ul id="tabs" class="rave-ui-tabs">
                <c:forEach items="${tabs.navigationItems}" var="navItem">
                    <c:choose>
                        <c:when test="${navItem.selected}">
                            <li class="rave-ui-tab rave-ui-tab-selected">
                                <div class="page-title">
                                    <a href="<spring:url value="${navItem.url}"/>"><fmt:message
                                            key="${navItem.name}"/></a>
                                </div>
                            </li>
                        </c:when>
                        <c:otherwise>
                            <li class="rave-ui-tab">
                                <div class="page-title">
                                    <a href="<spring:url value="${navItem.url}"/>"><fmt:message
                                            key="${navItem.name}"/></a>
                                </div>
                            </li>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
            </ul>
        </c:if>
    </nav>
</div>