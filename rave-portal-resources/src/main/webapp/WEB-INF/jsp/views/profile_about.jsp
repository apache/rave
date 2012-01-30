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
  
  Description:
  User's information display and edit options
  --%>

<%@ page language="java" trimDirectiveWhitespaces="true" %>
<%@ include file="/WEB-INF/jsp/includes/taglibs.jsp" %>
<fmt:setBundle basename="messages"/>
<jsp:useBean id="userProfile" type="org.apache.rave.portal.model.User" scope="request"/>

<div id="content">
	<form:form id="editAccountForm" commandName="userProfile" action="userInfo?referringPageId=${referringPageId}" method="POST">
	<!-- Display personal information of user-->
	<h2><fmt:message key="page.profile.personal.info" /></h2>
	<fieldset>
		<p>
    		<label for="givenNameField"><fmt:message key="page.profile.first.name"/></label> 
    		<label id="givenName" class="profile-info-visible"><c:out value="${userProfile.givenName}"/></label>
    		<form:input id="givenNameField" path="givenName" class="profile-info-hidden" value="${userProfile.givenName}" />
    	</p>
    	<br>
    	<p>
    		<label for="familyNameField"><fmt:message key="page.profile.last.name"/></label> 
    		<label id="familyName" class="profile-info-visible"><c:out value="${userProfile.familyName}"/></label>
    		<form:input id="familyNameField" path="familyName" class="profile-info-hidden" value="${userProfile.familyName}"/>
    	</p>
    	<p>
    		<label for="displayNameField" class="profile-info-hidden"><fmt:message key="page.profile.display.name"/></label> 
    		<form:input id="displayNameField" path="displayName" class="profile-info-hidden" value="${userProfile.displayName}"/>
    	</p>
    </fieldset>
      	
    <!-- Display basic information of user -->
    <h2><fmt:message key="page.profile.basic.info" /></h2>
	<fieldset>
		<p>
    		<label for="aboutMeField"><fmt:message key="page.profile.about.me"/></label> 
    		<label id="aboutMe" class="profile-info-visible"><c:out value=" ${userProfile.aboutMe}"/></label>
    		<form:textarea id="aboutMeField" path="aboutMe" class="profile-info-hidden" value="${userProfile.aboutMe}" />
    	</p>
    	<br>
    	<p>
    		<label for="statusField"><fmt:message key="page.profile.status"/></label> 
    		<label id="status" class="profile-info-visible"><c:out value=" ${userProfile.status}"/></label>
    		<form:input id="statusField" path="status" class="profile-info-hidden" value="${userProfile.status}" />
    	</p>
    </fieldset> 			
    
    <!-- Display contact information of user -->
    <h2><fmt:message key="page.profile.contact.info" /></h2>
	<fieldset>
		<p>
    		<label for="emailField"><fmt:message key="page.profile.email"/></label> 
    		<label id="email" class="profile-info-visible"><c:out value=" ${userProfile.email}"/></label>
    		<form:input id="emailField" path="email" class="profile-info-hidden" value="${userProfile.email}" />
    	</p>
    </fieldset>
    
    <!-- Edit profile option -->
    <h2><fmt:message key="page.profile.edit.option" /></h2>
    <fieldset>
    	<p>
    		<input type="hidden" id="profileInfo" value="profile-info" />
    		<button type="button" id="profileEdit" class="profile-info-visible"><fmt:message key="page.profile.edit"/></button>
    		<fmt:message key="page.profile.save" var="save"/>
    		<input type="submit" class="profile-info-hidden" value="${save}"/>
    		<button type="button" class="profile-info-hidden" id="cancelEdit"><fmt:message key="page.profile.cancel"/></button>
    	</p>
    </fieldset>    		
    </form:form>
</div>
