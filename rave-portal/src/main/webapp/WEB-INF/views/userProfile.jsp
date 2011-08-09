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
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="rave"%>
  
<jsp:useBean id="userProfile" type="org.apache.rave.portal.model.User" scope="request"/>
<rave:rave_generic_page pageTitle="User Profile Update - Rave">
  <div id="content">
	 <h1>Update Rave User Profile</h1>
	 <form:form id="userProfileForm" commandName="userProfile" action="updateUserProfile" method="POST">
		<fieldset>
		  <p><form:errors cssClass="error"/>
		  <p>
			 Username: ${userProfile.username}
		  </p>
		  <p>
			 <label for="passwordField">Password:</label>
			 <form:input id="passwordField" type="password" path="password" required="required"/>
			 <form:errors path="password" cssClass="error" />
		  </p>
		  <p>
			 <label for="passwordConfirmField">Confirm Password:</label>
			 <input id="passwordConfirmField" type="password" path="confirmPassword" required="required"/>
			 <form:errors path="confirmPassword" cssClass="error" />
		  </p>
		  <p>
			 <label for="openIdField">OpenID URL:</label>
			 <form:input id="openIdField" type="text" path="openId"/>
			 <form:errors path="openId" cssClass="error" />
		  </p>
		</fieldset>
		<fieldset>
		  <input type="submit" value="UpdateProfile" />
		</fieldset>
	 </form:form>
  </div>
  <script src="//ajax.aspnetcdn.com/ajax/jQuery/jquery-1.6.1.min.js"></script>
  <script src="//ajax.aspnetcdn.com/ajax/jquery.validate/1.8.1/jquery.validate.min.js"></script>
  <script src="<spring:url value="/script/rave.js"/>"></script>
  <script src="<spring:url value="/script/rave_forms.js"/>"></script>
  
  <script>$(document).ready(rave.forms.validateUserProfileForm());</script>
</rave:rave_generic_page>
