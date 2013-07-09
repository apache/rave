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
	            <a href="<spring:url value="/app/admin/categories?referringPageId=${referringPageId}"/>"><fmt:message key="admin.categoryDetail.goback"/></a>
	                
	            <h2><c:out value="${category.text}"/></h2>
	            <div class="well">
                    <section>
		                <form:form id="deleteCategory" action="delete" commandName="category" method="POST">
		                	<fieldset>
                            	<legend><fmt:message key="admin.delete"/><c:out value=" ${category.text}"/></legend>
                            	<input type="hidden" name="token" value="<c:out value="${tokencheck}"/>"/>
                                <input type="hidden" name="referringPageId" value="<c:out value="${referringPageId}"/>"/>
                            	<br/>
		                        <div>
		                            <label class="checkbox">
		                            	<input type="checkbox" name="confirmdelete" id="confirmdelete" value="true"/>
		                                <fmt:message key="admin.categoryDetail.action.delete.confirm"/>
		                            </label>
                                    <c:if test="${missingConfirm}">
                                        <p class="error"><fmt:message key="admin.userdetail.action.delete.confirm.required"/></p>
                                    </c:if>
		                        </div>
		                    </fieldset>
		                    <fieldset>
		                            <fmt:message key="admin.category.delete" var="deleteCategoryMsg"/>
		                            <button class="btn btn-danger" type="submit" value="${deleteCategoryMsg}">${deleteCategoryMsg}</button>
		                    </fieldset>
		                </form:form>
		            </section>
	            </div>        
	        </article>
	        <section>
	            <form:form id="updateCategory" action="update" commandName="category" method="POST" class="form-inline">
	                <fieldset>
		            	<legend><fmt:message key="admin.categoryDetail.editData"/></legend>
		            	<br/>
		                <form:errors cssClass="error" element="p"/>
	                    <input type="hidden" name="token" value="<c:out value="${tokencheck}"/>"/>
                        <input type="hidden" name="referringPageId" value="<c:out value="${referringPageId}"/>"/>
	                    
	                    <div class="control-group">
	                        <label for="text" class="control-label">
	                        	<fmt:message key="admin.categoryDetail.label.text"/>
	                        </label>
	                        <div class="controls">
		                        <form:input id="text" path="text" required="required" value="${category.text}" autofocus="autofocus" />&nbsp;
	                    
		                    	<fmt:message key="admin.category.update" var="updateCategoryMsg"/>
		                    	<button class="btn btn-primary" type="submit" value="${updateCategoryMsg}">
		                    		${updateCategoryMsg}
		                    	</button>
	                    	</div>
                        </div>
                        <form:errors path="text" cssClass="error"/>
	                </fieldset>
	            </form:form>
	        </section>
	    </div>
	</div>
</div>