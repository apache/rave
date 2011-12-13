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
<%@ include file="/WEB-INF/jsp/includes/taglibs.jsp" %>
<fmt:setBundle basename="messages"/>

<fmt:message key="${pageTitleKey}" var="pagetitle"/>
<rave:header pageTitle="${pagetitle}"/>
<rave:admin_tabsheader/>
<div class="pageContent">
    <article class="admincontent">
        <%--@elvariable id="actionresult" type="java.lang.String"--%>
        <c:if test="${actionresult eq 'delete' or actionresult eq 'update'}">
            <div class="alert-message success">
                <p>
                    <fmt:message key="admin.preferencedetail.action.${actionresult}.success"/>
                </p>
            </div>
        </c:if>

        <h2><fmt:message key="admin.preferences.shorttitle"/></h2>

        <spring:url value="/app/admin/preferencedetail/edit" var="detaillink"/>

            <%--@elvariable id="preferenceMap" type="java.util.Map<java.lang.String, org.apache.rave.portal.model.PortalPreference>"--%>
        <c:choose>
            <c:when test="${fn:length(preferenceMap) eq 0}">
                <a href="<c:out value="${detaillink}"/>"><fmt:message key="admin.preferences.edit"/></a>
            </c:when>
            <c:otherwise>
                <table class="datatable preferencestable">
                    <tbody>
                    <c:forEach items="${preferenceMap}" var="entry">
                        <c:set value="${entry.value}" var="portalPreference"/>
                        <tr data-detaillink="<c:out value="${detaillink}"/>">
                            <th scope="row" class="largetextcell">
                                <a href="<c:out value="${detaillink}"/>"><fmt:message
                                        key="admin.preferencedetail.${portalPreference.key}"/></a>
                            </th>
                            <td class="largetextcell">
                                <ul>
                                    <c:forEach items="${portalPreference.values}" var="value">
                                        <li><a href="<c:out value="${detaillink}"/>"><c:out value="${value}"/></a></li>
                                    </c:forEach>
                                </ul>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </c:otherwise>
        </c:choose>

    </article>
</div>