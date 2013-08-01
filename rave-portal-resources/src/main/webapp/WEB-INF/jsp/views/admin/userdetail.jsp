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
<c:set var="canChangeUserStatus" value="${user.username ne loggedInUser}"/>
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
	            <a href="<spring:url value="/app/admin/users?referringPageId=${referringPageId}"/>"><fmt:message key="admin.userdetail.goback"/></a></li>
	            <h2><c:out value="${user.username}"/></h2>
	           
	            <c:if test="${canChangeUserStatus}">
       			<div class="well">
                    <section>
                        <form:form cssClass="form-horizontal" id="deleteUserProfile" action="delete" commandName="user" method="POST">
                            <fieldset>
                            	<legend><fmt:message key="admin.delete"/> <c:out value=" ${user.username}"/></legend>
                                <input type="hidden" name="token" value="<c:out value="${tokencheck}"/>"/>
                                <input type="hidden" name="referringPageId" value="<c:out value="${referringPageId}"/>"/>
                                <br/>
                                <div>
                                   <label class="checkbox">
                                   		<input type="checkbox" name="confirmdelete" id="confirmdelete" value="true"/>
                                   		<fmt:message key="admin.userdetail.action.delete.confirm"/>
                                   </label>
                                    <c:if test="${missingConfirm}">
                                    	<p class="error"><fmt:message key="admin.userdetail.action.delete.confirm.required"/></p>
                                    </c:if>
                                </div>
                            </fieldset>
                            <fieldset>
                                <p>
                                    <input type="submit" class="btn btn-danger" value="Delete the user"/>
                                </p>
                            </fieldset>
                        </form:form>
                    </section>
               </div>
	            </c:if>
	
	            <div>
	                <section>
	
	                    <form:form cssClass="form-horizontal" id="updateUserProfile" action="update" commandName="user" method="POST">
	                        <form:errors cssClass="error" element="p"/>
	                        <fieldset>
	                            <legend><fmt:message key="admin.userdetail.editdata"/></legend>
	                            <br/>
	                            <input type="hidden" name="token" value="<c:out value="${tokencheck}"/>"/>
                                <input type="hidden" name="referringPageId" value="<c:out value="${referringPageId}"/>"/>
	                            <form:hidden path="username" />
	                            <div class="control-group">
	                                <label class="control-label" for="email"><fmt:message key="page.general.email"/></label>
	                                <div class="controls"><spring:bind path="email">
	                                    <input type="email" name="email" id="email" value="<c:out value="${status.value}"/>"
	                                           class="long"/>
	                                </spring:bind></div>
	                                <form:errors path="email" cssClass="error"/>
	                            </div>
	
	                            <div class="control-group">
	                                <label class="control-label" for="openIdField"><fmt:message key="page.userprofile.openid.url"/></label>
	                                <div class="controls">
	                                    <spring:bind path="openId">
	                                        <input type="url" id="openIdField" name="openId" value="<c:out value="${status.value}"/>" class="long"/>
	                                    </spring:bind>
	                                </div>
	                                <form:errors path="openId" cssClass="error"/>
	                            </div>
	
	                            <div class="control-group">
	                                <span class="control-label"><fmt:message key="admin.userdata.accountstatus"/></span>
	                                <ul class="checkboxlist">
	                                    <li>
	                                        <fmt:message key="admin.userdata.enabled" var="labelEnabled"/>
	                                        <form:checkbox path="enabled" label="${labelEnabled}"
	                                                       disabled="${canChangeUserStatus ne true}"/>
	                                    </li>
	                                    <li>
	                                        <fmt:message key="admin.userdata.expired" var="labelExpired"/>
	                                        <form:checkbox path="expired" label="${labelExpired}"
	                                                       disabled="${canChangeUserStatus ne true}"/>
	                                    </li>
	                                    <li>
	                                        <fmt:message key="admin.userdata.locked" var="labelLocked"/>
	                                        <form:checkbox path="locked" label="${labelLocked}"
	                                                       disabled="${canChangeUserStatus ne true}"/>
	                                    </li>
	                                </ul>
	                            </div>
	                        </fieldset>
	                        <fieldset>
	                            <span class="control-label"><fmt:message key="admin.userdata.authorities"/></span>
                                    <%--@elvariable id="authorities" type="org.apache.rave.rest.model.SearchResult<org.apache.rave.model.Authority>"--%>
	                            <ul class="checkboxlist">
	                                <form:checkboxes path="authorities" items="${authorities.resultSet}" itemLabel="authority" itemValue="authority" element="li"/>
	                            </ul>
	                        </fieldset>
	                        <fieldset>
	                            <fmt:message key="page.userprofile.button" var="updateButtonText"/>
	                            <button class="btn btn-primary" type="submit" value="${updateButtonText}">${updateButtonText}</button>
	                        </fieldset>
	                    </form:form>
	                </section>
	            </div>
	            <div class="clear-float"></div>
	        </article>
	    </div>
	</div>
</div>