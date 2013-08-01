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
<%--@elvariable id="searchResult" type="org.apache.rave.rest.model.SearchResult<org.apache.rave.model.User>"--%>


<fmt:message key="${pageTitleKey}" var="pagetitle"/>
<rave:navbar pageTitle="${pagetitle}"/>
<div class="container-fluid">
	<div class="row-fluid">
	    <div class="span2">
	    	<div class="tabs-respond">
		        <rave:admin_tabsheader/>
	    	</div>
	    </div>
	    <div class="span10">
	        <article>
	            <c:if test="${actionresult eq 'delete' or actionresult eq 'update'}">
	                <div class="alert alert-info">
	                        <fmt:message key="admin.userdetail.action.${actionresult}.success"/>
	                </div>
	            </c:if>
	            <c:if test="${not empty message}">
	                <div class="alert alert-info">${message}</div>
	            </c:if>
	
	
	
	            <rave:admin_listheader/>
	            
	            <div class="searchHeading paginationHeading">
	            	<rave:admin_paging/>	
                    <form id="userSearchForm" class="form-horizontal search-form" action="<spring:url value="/app/admin/users/search"/>" method="get">
                        <fieldset>
                            <div class="input-append">
	                            <input type="text" class="input-medium" id="searchTerm" name="searchTerm" value="<c:out value="${searchTerm}"/>" placeholder='<fmt:message key="admin.users.search"/>'
	                            />
                                <input type="hidden" name="referringPageId" value="<c:out value="${referringPageId}"/>"/>
                                <fmt:message key="page.store.search.button" var="searchButtonText"
	                            /><button class="btn btn-primary" type="submit" value="${searchButtonText}">${searchButtonText}</button>
                            </div>
                        </fieldset>
                    </form>
	                <c:if test="${not empty searchTerm}"><a class="btn btn-success" href="<spring:url value="/app/admin/users?referringPageId=${referringPageId}"/>"><fmt:message key="admin.clearsearch"/></a>
	                </c:if>
	            </div>
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
	                        <spring:url value="/app/admin/userdetail/${user.id}?referringPageId=${referringPageId}" var="detaillink"/>
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
	            <div>
	            <fieldset>
	            	<legend>Add New User</legend>
	            	<br/>
	            	<a class="btn btn-primary" href="<spring:url value="/app/admin/adduser?referringPageId=${referringPageId}"/>"><i class="icon-plus icon-white"></i> <fmt:message key="admin.users.add"/></a>
	            	<br/>
	            </fieldset>
	            <div>
          	    <br/>

	        </article>
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
