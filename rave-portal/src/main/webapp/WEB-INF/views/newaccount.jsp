<sele%--
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
  
<rave:rave_generic_page pageTitle="New Account Application - Rave">
<div id="content">
    <h1>Rave Account Application</h1>
    <form:form id="newAccountForm" commandName="newUser" action="newaccount" method="POST">
        <fieldset>
            <p>All fields are required</p>
				<p><form:errors cssClass="error"/>
            <p>
                <label for="userNameField">Username:</label>
                <form:input id="userNameField" type="text" path="username" required="required" autofocus="autofocus"/>
                <form:errors path="username" cssClass="error" />
            </p>
            <p>
                <label for="passwordField">Password:</label>
                <form:input id="passwordField" type="password" path="password" required="required"/>
            	<form:errors path="password" cssClass="error" />
            </p>
            <p>
                <label for="passwordConfirmField">Confirm Password:</label>
                <form:input id="passwordConfirmField" type="password" path="confirmPassword" required="required"/>
            	<form:errors path="confirmPassword" cssClass="error" />
            </p>
            <p>
            	<label for="pageLayoutField">Select Page Layout:</label>
            	<form:select path="pageLayout">
            		<form:option value="columns_1" id="columns_1_id">One Column</form:option>
            		<form:option value="columns_2" id="columns_2_id" selected="selected" xmlns="default namespace">Two Columns</form:option>
            		<form:option value="columns_2wn" id="columns_2wn_id">Two Columns (wide/narrow)</form:option>
            		<form:option value="columns_3" id="columns_3_id">Three Columns</form:option>
            		<form:option value="columns_3nwn" id="columns_3nwn_id">Three Columns (narrow/wide/narrow)</form:option>
            		<form:option value="columns_4" id="columns_4_id">Four Columns</form:option>
            		<form:option value="columns_3nwn_1_bottom" id="columns_3nwn_1_bottom">Four Columns (narrow/wide/narrow/bottom)</form:option>
            	</form:select>
            </p>
        </fieldset>
        <fieldset>
             <input type="submit" value="Create Account" />
        </fieldset>
    </form:form>
</div>
<script src="//ajax.aspnetcdn.com/ajax/jQuery/jquery-1.6.1.min.js"></script>
<script src="//ajax.aspnetcdn.com/ajax/jquery.validate/1.8.1/jquery.validate.min.js"></script>
<script src="<spring:url value="/script/rave.js"/>"></script>
<script src="<spring:url value="/script/rave_forms.js"/>"></script>

<script>$(document).ready(rave.forms.validateNewAccountForm());</script>
</rave:rave_generic_page>
  