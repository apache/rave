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

<rave:navbar pageTitle="${pagetitle}"/>
<div class="container-fluid admin-ui">
	<div class="row-fluid">
	    <div class="span2">
	    	<div class="tabs-respond">
		        <rave:admin_tabsheader/>
	    	</div>
	    </div>
	    <div class="span10">
	        <article>
	            <h2><fmt:message key="admin.category.shortTitle"/></h2>
	            <%--@elvariable id="actionresult" type="java.lang.String"--%>
	            <c:if test="${actionresult eq 'delete' or actionresult eq 'update' or actionresult eq 'create'}">
	                <div class="alert alert-info">
	                    <fmt:message key="admin.categoryDetail.action.${actionresult}.success"/>
	                </div>
	            </c:if>
	
	            <table id="categoryList" class="table table-striped table-bordered table-condensed">
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
	                    <spring:url value="/app/admin/category/edit?id=${category.id}&referringPageId=${referringPageId}" var="detaillink"/>
                        <portal:person id="${category.createdUserId}" var="createdUser" />
                        <portal:person id="${category.lastModifiedUserId}" var="modifiedUser" />

	                    <tr data-detaillink="${detaillink}">
	                        <td>
	                            <a href="${detaillink}"><c:out value="${category.text}"/></a>
	                        </td>
	                        <td>
	                            <c:out value="${createdUser.username}"/>
	                        </td>
	                        <td>
	                            <c:out value="${category.createdDate}"/>
	                        </td>
	                        <td>
	                            <c:out value="${modifiedUser.username}"/>
	                        </td>
	                        <td>
	                            <c:out value="${category.lastModifiedDate}"/>
	                        </td>
	                    </tr>
	                </c:forEach>
	                </tbody>
	            </table>
	
	
	        </article>
	        <div>
	            <form:form id="createCategory" cssClass="form-inline" commandName="category" action="category/create" method="POST">
	                <form:errors cssClass="error" element="p"/>
	                <fieldset>
	                    <legend><fmt:message key="admin.category.create"/></legend>
	                    <input type="hidden" name="token" value="<c:out value="${tokencheck}"/>"/>
                        <input type="hidden" name="referringPageId" value="<c:out value="${referringPageId}"/>"/>
	                    <br/>
	                    <div class="control-group">
	                        <label class="control-label" for="text">
	                        	<fmt:message key="admin.categoryDetail.label.text"/>
	                        </label>
	                        <div class="controls">
	                            <form:input id="text" path="text" required="required" autofocus="autofocus"/>&nbsp;
	                            <fmt:message key="admin.category.create" var="createCategoryMsg"/>
	                            <button class="btn btn-primary" type="submit" value="${createCategoryMsg}">${createCategoryMsg}</button>
	                    </div>
	                    </div>
	                    <form:errors path="text" cssClass="error"/></div>
	                </fieldset>
	            </form:form>
	        </div>
	    </div>
	</div>
</div>
<portal:register-init-script location="${'AFTER_RAVE'}">
    <script>
        require(["portal/rave_admin", "jquery"], function(raveAdmin, $){
            $(function() {
                raveAdmin.init();
            });
        })
    </script>
</portal:register-init-script>
