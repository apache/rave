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
<%@ include file="/WEB-INF/jsp/includes/taglibs.jsp" %>
<%@ attribute name="hideButton" required="false" description="The button that should not be displayed on current page" %>

	<header>
		<div class="navbar">
			<div class="navbar-inner">
				<div class="container">
		            <a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
		                <span class="icon-bar"></span>
		                <span class="icon-bar"></span>
		                <span class="icon-bar"></span>
		            </a>
		            <span class="brand">RAVE</span>
		            <div class="nav-collapse">
		                 <ul class="nav pull-right">		                 
		                 	<c:if test="${hideButton ne 'loginButton'}">
		                        <li>
		                        	<a id="loginButton" type="submit" href="/">
		                        		&laquo; 
				                    	<fmt:message key="page.login.button" />
		                        	</a>
		                        </li>
		                    </c:if>
		                 	<c:if test="${hideButton ne 'requestNewPasswordButton'}">
		                        <li>
		                        	<form action="<c:url value="/app/newpassword"/>" method="get">
				                    	<fmt:message key="page.login.forgot.password.button" var="requestNewPasswordButtonText"/>
				                    	<button class="btn btn-link" id="requestNewPasswordButton" type="submit" value="${requestNewPasswordButtonText}">${requestNewPasswordButtonText}</button>
				                    </form>
		                        </li>
		                    </c:if>
		                 	<c:if test="${hideButton ne 'requestNewUsernameButton'}">
		                        <li>
		                        	<form action="<c:url value="/app/retrieveusername"/>" method="get">
				                        <fmt:message key="page.login.forgot.username.button" var="requestNewUsernameButton"/>
			                            <button class="btn btn-link" id="requestNewUsernameButton" type="submit" value="${requestNewUsernameButton}">${requestNewUsernameButton}</button>
			                    	</form>
		                        </li>
		                    </c:if>
		                 	<c:if test="${hideButton ne 'createNewAccountButton'}">
		                        <li>
			                        <form action="<c:url value="/app/newaccount.jsp"/>" method="get">
					                    <fmt:message key="page.login.createaccount.button" var="createAccountButtonText"/>
					                    <button class="btn btn-link" id="createNewAccountButton" type="submit" value="${createAccountButtonText}">${createAccountButtonText}</button>
					                </form>
		                        </li>
		                    </c:if>
		                 </ul>
		             </div>
		        </div>
		    </div>
		</div>
	</header>
