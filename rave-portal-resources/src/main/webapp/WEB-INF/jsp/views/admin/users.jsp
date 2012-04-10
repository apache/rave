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
<%--@elvariable id="searchResult" type="org.apache.rave.portal.model.util.SearchResult<org.apache.rave.portal.model.User>"--%>

<fmt:message key="${pageTitleKey}" var="pagetitle"/>
<rave:header pageTitle="${pagetitle}"/>
<div class="container-fluid">
    <div class="span3">
        <rave:admin_tabsheader/>
    </div>
    <article class="span12">
        <c:if test="${actionresult eq 'delete' or actionresult eq 'update'}">
            <div class="alert alert-info">
                <p>
                    <fmt:message key="admin.userdetail.action.${actionresult}.success"/>
                </p>
            </div>
        </c:if>
        <c:if test="${not empty message}">
            <div class="alert alert-info"><p>${message}</p></div>
        </c:if>



        <rave:admin_listheader/>
        <rave:admin_paging/>

        <c:if test="${searchResult.totalResults > 0}">
            <table class="table table-striped table-bordered table-condensed">
                <thead>
                <tr>
                    <th><fmt:message key="admin.userdata.username"/></th>
                    <th><fmt:message key="admin.userdata.email"/></th>
                    <th><fmt:message key="admin.userdata.enabled"/></th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="user" items="${searchResult.resultSet}">
                    <spring:url value="/app/admin/userdetail/${user.entityId}" var="detaillink"/>
                    <tr data-detaillink="${detaillink}">
                        <td><a href="${detaillink}"><c:out value="${user.username}"/></a></td>
                        <td><c:out value="${user.email}"/></td>
                        <td>${user.enabled}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </c:if>

        <rave:admin_paging/>

    </article>
    <div class="span6">
            <a class="btn btn-success" href="<spring:url value="/app/admin/adduser"/>"><fmt:message key="admin.users.add"/></a>

                <form class="form-horizontal" action="<spring:url value="/app/admin/users/search"/>" method="get">
                    <fieldset>
                        <label for="searchTerm"><fmt:message key="admin.users.search"/></label>
                        <input class="search-query" type="search" id="searchTerm" name="searchTerm" value="<c:out value="${searchTerm}"/>"/>
                        <fmt:message key="page.store.search.button" var="searchButtonText"/>
                        <button class="btn btn-primary" type="submit" value="${searchButtonText}">${searchButtonText}</button>
                    </fieldset>
                </form>
            <c:if test="${not empty searchTerm}">
                <a class="btn btn-success" href="<spring:url value="/app/admin/users"/>"><fmt:message key="admin.clearsearch"/></a>
            </c:if>

    </div>
</div>
<portal:register-init-script location="${'AFTER_RAVE'}">
    <script>
        $(function() {
            rave.admin.initAdminUi();
        });
    </script>
</portal:register-init-script>
