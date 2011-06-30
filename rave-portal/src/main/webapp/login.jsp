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
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="rave"%>
<rave:rave_generic_page pageTitle="Login - Rave">
<div id="content">

    <h1>Login</h1>

    <h2>Username and Password</h2>
    <form id="loginForm" name="loginForm" action="j_spring_security_check" method="post">
        <fieldset>
            <p>
                <label for="usernameField">Username: </label>
                <input id="usernameField" type="text" name="j_username"/>
            </p>

            <p>
                <label for="passwordField">Password: </label>
                <input id="passwordField" type="password" name="j_password"/>
            </p>
        </fieldset>
        <fieldset>
            <input type="submit" value="Login"/>
        </fieldset>
    </form>

    <h2>OpenID Identity</h2>
    <form id="oidForm" name='oidf' action='j_spring_openid_security_check' method='POST'>
        <fieldset>
            <p>
                <label for="openid_identifier">Identity: </label>
                <input type='text' id="openid_identifier" name='openid_identifier' class="long"/>
            </p>
        </fieldset>
        <fieldset>
            <input type="submit" value="Login with OpenID"/>
        </fieldset>

    </form>
</div>
<script>
document.loginForm.j_username.focus();
</script>
</rave:rave_generic_page>
