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
<%-- Note: This page has the body definition embedded so we can reference it directly from the security config file. --%>
<tiles:insertDefinition name="templates.base">
    <%-- Override the default pageTitleKey and then export it to the request scope for use later on this page --%>
    <tiles:putAttribute name="pageTitleKey" value="page.login.title"/>
    <tiles:importAttribute name="pageTitleKey" scope="request"/>

    <tiles:putAttribute name="body">
    
    <rave:login_navbar hideButton="loginButton" />
    
        <div class="container-fluid">
            <c:if test="${not empty message}">
                <div class="alert alert-info">${message}</div>
            </c:if>
            
            <h1><fmt:message key="${pageTitleKey}"/></h1>
            <div class="row-fluid" id="loginOptions">
                <div id="loginBlock" class="clearfix well">
                    <div class="span6">
                        <%--
                            //############################################
                            // LOGIN FORM
                            //############################################
                        --%>
                        <form class="form-horizontal" id="loginForm" name="loginForm" action="j_spring_security_check" method="post">
                            <fieldset>
                                <legend><fmt:message key="page.login.usernamepassword"/></legend>
                                <c:if test="${param['authfail'] eq 'form'}">
                                    <div class="alert alert-error"><fmt:message key="page.login.usernamepassword.fail"/></div>
                                </c:if>
                                <div class="control-group">
                                    <label class="control-label" for="usernameField"><fmt:message key="page.general.username"/></label>
                                    <div class="controls">
                                        <input class="input-large" id="usernameField" type="text" name="j_username" autofocus="autofocus" autocapitalize="off" autocorrect="off"/>
                                    </div>
                                </div>
                                <div class="control-group">
                                    <label class="control-label" for="passwordField"><fmt:message key="page.general.password"/></label>
                                    <div class="controls">
                                        <input class="input-large" id="passwordField" type="password" name="j_password"/>
                                    </div>
                                </div>
                                <div class="control-group">
                                    <label class="control-label" for="remember_me">
                                        <fmt:message key="page.login.rememberme"/>
                                    </label>
                                    <div class="controls">
                                        <input type='checkbox' name='_spring_security_remember_me' id="remember_me" value="true"/>
                                    </div>
                                </div>
                            </fieldset>
                            <fieldset>
                                <fmt:message key="page.login.usernamepassword.login" var="loginButtonText"/>
                                <div class="controls">
                                    <button class="btn btn-primary" type="submit" value="${loginButtonText}">${loginButtonText}</button>
                                </div>
                            </fieldset>
                        </form>
                    </div>
                    <div class="span4">
                        <%--
                            //############################################
                            // OPENID LOGIN
                            //############################################
                        --%>
                        <form class="form-horizontal" id="openIdForm" name='oidf' action='j_spring_openid_security_check' method='POST'>
                            <fieldset>
                                <legend><fmt:message key="page.login.openid"/></legend>
                                <c:if test="${param['authfail'] eq 'openid'}">
                                    <div class="alert alert-error"><fmt:message key="page.login.openid.fail"/></div>
                                </c:if>
                                <div class="control-group">
                                    <label class="control-label" for="openid_identifier"><fmt:message key="page.login.openid.identifier"/></label>
                                    <div class="controls">
                                        <input class="input-large" type="text" id="openid_identifier" name="openid_identifier" autocapitalize="off" autocorrect="off"/>
                                    </div>
                                </div>
                                <div class="control-group">
                                    <label class="control-label" for="remember_me">
                                        <fmt:message key="page.login.rememberme"/>
                                    </label>
                                    <div class="controls">
                                        <input type='checkbox' name='_spring_security_remember_me' id="remember_me_openid" value="true"/>
                                    </div>
                                </div>
                            </fieldset>
                            <fieldset>
                                <fmt:message key="page.login.openid.button" var="openidButtonText"/>
                                <div class="controls">
                                    <button class="btn btn-primary" type="submit" value="${openidButtonText}">${openidButtonText}</button>
                                </div>
                            </fieldset>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </tiles:putAttribute>
</tiles:insertDefinition>
