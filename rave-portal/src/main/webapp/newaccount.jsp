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
            <p>All fields are required</p>
            <p>
                <label for="userNameField">Username:</label>
                <input id="userNameField" type="text" name="userName" required="required" autofocus="autofocus"/>
            </p>
            <p>
                <label for="passwordField">Password:</label>
                <input id="passwordField" type="password" name="password" required="required"/>
            </p>
            <p>
                <label for="passwordConfirmField">Confirm Password:</label>
                <input id="passwordConfirmField" type="password" name="passwordConfirm" required="required"/>
            </p>
        </fieldset>
        <fieldset>
             <input type="submit" value="Create Account" />
        </fieldset>
    </form>
</div>
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.6.1/jquery.min.js"></script>
<script src="<c:url value="/script/3rdparty/jquery.validate.min.js"/>"></script>
<script src="<spring:url value="/script/rave.js"/>"></script>
<script src="<spring:url value="/script/rave_forms.js"/>"></script>

<script>$(document).ready(rave.forms.validateNewAccountForm());</script>
</rave:rave_generic_page>
  