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
<div class="container-fluid">
    <div class="span2">
        <rave:admin_tabsheader/>
    </div>
    <article class="span8">
        <h2><fmt:message key="admin.category.shortTitle"/></h2>
        <%--@elvariable id="actionresult" type="java.lang.String"--%>
        <c:if test="${actionresult eq 'delete' or actionresult eq 'update' or actionresult eq 'create'}">
            <div class="alert alert-info">
                <fmt:message key="admin.categoryDetail.action.${actionresult}.success"/>
            </div>
        </c:if>

        <table class="table table-striped table-bordered table-condensed">
            <thead>
            <tr>
                <th><fmt:message key="admin.categoryData.text"/></th>
                <th><fmt:message key="admin.categoryData.createdBy"/></th>
                <th><fmt:message key="admin.categoryData.createdDate"/></th>
                <th><fmt:message key="admin.categoryData.modifiedBy"/></th>
                <th><fmt:message key="admin.categoryData.modifiedDate"/></th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${categories}" var="category">
                <spring:url value="/app/admin/category/edit?id=${category.entityId}" var="detaillink"/>

                <tr data-detaillink="${detaillink}">
                    <td>
                        <a href="${detaillink}"><c:out value="${category.text}"/></a>
                    </td>
                    <td>
                        <c:out value="${category.createdUser.username}"/>
                    </td>
                    <td>
                        <c:out value="${category.createdDate}"/>
                    </td>
                    <td>
                        <c:out value="${category.lastModifiedUser.username}"/>
                    </td>
                    <td>
                        <c:out value="${category.lastModifiedDate}"/>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>


    </article>
    <div class="span4">
        <form:form cssClass="form-horizontal" commandName="category" action="category/create" method="POST">
            <form:errors cssClass="error" element="p"/>
            <fieldset>
                <legend><fmt:message key="admin.category.create"/></legend>
                <input type="hidden" name="token" value="<c:out value="${tokencheck}"/>"/>
                <div class="control-group">
                    <label class="control-label" for="text"><fmt:message key="admin.categoryDetail.label.text"/></label>
                    <div class="controls">
                        <form:input cssClass="input-medium" id="text" path="text" required="required" autofocus="autofocus"/>
                        <form:errors path="text" cssClass="error"/></div>
                </div>
            </fieldset>
            <fieldset>
                <fmt:message key="admin.category.create" var="createCategoryMsg"/>
                <div class="controls">
                    <button class="btn btn-primary" type="submit" value="${createCategoryMsg}">${createCategoryMsg}</button>
                </div>

            </fieldset>
        </form:form>
    </div>
</div>
<portal:register-init-script location="${'AFTER_RAVE'}">
    <script>
        $(function() {
            rave.admin.initAdminUi();
        });
    </script>
</portal:register-init-script>
