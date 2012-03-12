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
        <c:if test="${actionresult eq 'delete' or actionresult eq 'update' or actionresult eq 'create'}">
            <div class="alert-message success">
                <p>
                    <fmt:message key="admin.categoryDetail.action.${actionresult}.success"/>
                </p>
            </div>
        </c:if>

       <h2><fmt:message key="admin.category.shortTitle"/></h2>

        <div class="rightcolumn">
            <section class="formbox">
                <h3><fmt:message key="admin.category.create"/></h3>
                <form:form id="createCategoryForm" commandName="category" action="category/create" method="POST">
                    <form:errors cssClass="error" element="p"/>
                    <fieldset>
                        <input type="hidden" name="token" value="<c:out value="${tokencheck}"/>"/>

                        <p>
                            <label for="text"><fmt:message
                                    key="admin.categoryDetail.label.text"/></label>
                            <form:input id="text" path="text" required="required"  autofocus="autofocus"/>
                            <form:errors path="text" cssClass="error"/>
                        </p>
                    </fieldset>
                    <fieldset>
                        <p>
                            <fmt:message key="admin.category.create" var="createCategoryMsg"/>
                            <input type="submit" value="${createCategoryMsg}"/>
                        </p>
                    </fieldset>
                </form:form>
            </section>
        </div>

        <div class="leftcolumn">
        <table class="datatable categoryTable">
            <thead>
            <tr>
                <th class="textcell"><fmt:message key="admin.categoryData.text"/></th>
                <th class="textcell"><fmt:message key="admin.categoryData.createdBy"/></th>
                <th class="textcell"><fmt:message key="admin.categoryData.createdDate"/></th>
                <th class="textcell"><fmt:message key="admin.categoryData.modifiedBy"/></th>
                <th class="textcell"><fmt:message key="admin.categoryData.modifiedDate"/></th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${categories}" var="category">
                <spring:url value="/app/admin/category/edit?id=${category.entityId}" var="detaillink"/>

                <tr data-detaillink="${detaillink}">
                    <td class="textcell">
                        <c:out value="${category.text}"/>
                    </td>
                    <td class="textcell">
                        <c:out value="${category.createdUser.username}"/>
                    </td>
                    <td class="textcell">
                        <fmt:formatDate value="${category.createdDate}" dateStyle="short" type="both" />
                    </td>
                    <td class="textcell">
                        <c:out value="${category.lastModifiedUser.username}"/>
                    </td>
                    <td class="textcell">
                        <fmt:formatDate value="${category.lastModifiedDate}" dateStyle="short" type="both" />
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>

        </div>
    </article>
</div>
<script src="//ajax.aspnetcdn.com/ajax/jQuery/jquery-1.6.4.min.js"></script>
<script src="<spring:url value="/script/rave_admin.js"/>"></script>
<script>$(function() {
rave.admin.initAdminUi();
});</script>
