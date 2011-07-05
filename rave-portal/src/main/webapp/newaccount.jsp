<!DOCTYPE html>
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
  <%@ taglib tagdir="/WEB-INF/tags" prefix="rave"%>
  
  <rave:rave_generic_page pageTitle="New Account Application - Rave">
	 <div id="content">
		<h1>Rave Account Application</h1>
		<form id="newAccountForm" name="newAccountForm" action="app/newaccount" method="POST">
		  <fieldset>
			 <p>
				<label for="userNameField">Username:</label> <input
				id="userNameField" type="text" name="userName" class="required" minlength="4"/>
			 </p>
			 <p>
				<label for="passwordField">Password:</label> <input
				id="passwordField" type="password" name="password" class="required" minlength="4"/>
			 </p>
			 <p>
				<label for="passwordConfirmField">Confirm Password:</label> <input
				id="passwordConfirmField" type="password" name="passwordConfirmed" class="required" minlength="4" equalTo="#passwordField"/>
			 </p>
			 <p>
				<input type="submit" value="Create Account" />
			 </p>
			 
		  </fieldset>
		</form>
	 </div>
	 <script src="//code.jquery.com/jquery-latest.js"></script>
	 <!-- 
			We may want to include this function in the release as a local src.  The JQuery
			documentation uses an obsolete link that redirects to the link below. Chrome 12, for
			one, was unable to follow the redirection, breaking the validation.  Unfortunately, it 
			is also MIT/GPL dual-licensed.
	 -->
	 <script src="//view.jquery.com/trunk/plugins/validate/jquery.validate.js"></script>
	 <script>
		$(document).ready(function(){
		$("#newAccountForm").validate();
			 });
	 </script>
	 
  </rave:rave_generic_page>
  