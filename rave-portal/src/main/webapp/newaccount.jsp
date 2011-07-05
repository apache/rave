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
  <script src="http://code.jquery.com/jquery-latest.js"></script>
  <script src="http://view.jquery.com/trunk/plugins/validate/jquery.validate.js"></script>
  <script>
	 $(document).ready(function(){
	    $("#newAccountForm").validate();
	 });
  </script>
  <style>
	 .error {
	    font: normal 10px arial;
		 color: #ff0000
	 }
  </style>
	<div id="content">
		<h1>Rave Test Account Application</h1>
		<form id="newAccountForm" name="newAccountForm" action="app/newaccount">
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

</rave:rave_generic_page>
