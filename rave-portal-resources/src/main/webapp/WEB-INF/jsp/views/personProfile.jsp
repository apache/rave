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
<%@ page errorPage="/WEB-INF/jsp/views/error.jsp" %>
<%@ include file="/WEB-INF/jsp/includes/taglibs.jsp" %>
<fmt:setBundle basename="messages"/>
<jsp:useBean id="userProfile" type="org.apache.rave.portal.model.User" scope="request"/>

<!-- get the display name of user -->
<fmt:message key="page.personProfile.title" var="pageTitle">
   	<fmt:param><c:out value="${userProfile.displayName}" /></fmt:param>
</fmt:message>
<tiles:putAttribute name="pageTitleKey" value="${pageTitle}"/>
<tiles:importAttribute name="pageTitleKey" scope="request"/>

<!-- get the title of personal information -->
<fmt:message key="page.personProfile.personal.info" var="personalInfo"/>

<!-- get the title of basic information -->
<fmt:message key="page.personProfile.basic.info" var="basicInfo"/>

<!-- get the title of contact information -->
<fmt:message key="page.personProfile.contact.info" var="contactInfo"/>
<div class="navbar navbar-fixed-top">
    <div class="navbar-inner">
        <div class="container">
            <a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </a>
            <span class="brand">
                <fmt:message key="page.home.welcome">
                    <fmt:param>
                        <c:choose>
                            <c:when test="${not empty page.owner.displayName}"><c:out value="${page.owner.displayName}"/></c:when>
                            <c:otherwise><c:out value="${page.owner.username}"/></c:otherwise>
                        </c:choose>
                    </fmt:param>
                </fmt:message>
            </span>
            <div class="nav-collapse">
                <ul class="nav pull-right">
                    <li>
                        <c:choose>
                            <c:when test="${empty referringPageId}">
                                <spring:url value="/index.html" var="gobackurl"/>
                            </c:when>
                            <c:otherwise>
                                <spring:url value="/app/page/view/${referringPageId}" var="gobackurl"/>
                            </c:otherwise>
                        </c:choose>
                        <a href="<c:out value="${gobackurl}"/>"><fmt:message key="page.general.back"/></a>
                    </li>
                    <sec:authorize url="/app/admin/">
                        <li>
                            <a href="<spring:url value="/app/admin/"/>">
                            <fmt:message key="page.general.toadmininterface"/>
                            </a>
                        </li>
                    </sec:authorize>
                    <li>
                        <a href="<spring:url value="/j_spring_security_logout" htmlEscape="true" />">
                        <fmt:message key="page.general.logout"/></a>
                    </li>
                </ul>
            </div>
        </div>
    </div>
</div>

<div id="pageContent" class="container-fluid navbar-spacer">
    <div class="row-fluid">
        <div class="span9">
            <div id="personProfileContent" class="row-fluid">
                <form:form id="editAccountForm" commandName="userProfile" action="person?referringPageId=${referringPageId}" method="POST">
                    <!-- Display personal information of user-->
                    <h2><fmt:message key="page.profile.personal.info" /></h2>
                    <fieldset>
                        <div class="span12">
                            <p>
                                <label for="givenNameField"><fmt:message key="page.profile.first.name"/></label>
                                <label id="givenName" class="profile-info-visible"><c:out value="${userProfile.givenName}"/></label>
                                <form:input id="givenNameField" path="givenName" class="profile-info-hidden" value="${userProfile.givenName}" />
                            </p>
                        </div>
                        <div class="span12">
                            <p>
                                <label for="familyNameField"><fmt:message key="page.profile.last.name"/></label>
                                <label id="familyName" class="profile-info-visible"><c:out value="${userProfile.familyName}"/></label>
                                <form:input id="familyNameField" path="familyName" class="profile-info-hidden" value="${userProfile.familyName}"/>
                            </p>
                        </div>
                        <div class="span12">
                            <p>
                                <label for="displayNameField" class="profile-info-hidden"><fmt:message key="page.profile.display.name"/></label>
                                <form:input id="displayNameField" path="displayName" class="profile-info-hidden" value="${userProfile.displayName}"/>
                            </p>
                        </div>
                    </fieldset>

                    <!-- Display basic information of user -->
                    <h2><fmt:message key="page.profile.basic.info" /></h2>
                    <fieldset>
                        <div class="span12">
                            <p>
                                <label for="aboutMeField"><fmt:message key="page.profile.about.me"/></label>
                                <label id="aboutMe" class="profile-info-visible"><c:out value=" ${userProfile.aboutMe}"/></label>
                                <form:textarea id="aboutMeField" path="aboutMe" class="profile-info-hidden" value="${userProfile.aboutMe}" />
                            </p>
                        </div>
                        <div class="span12">
                            <p>
                                <label for="statusField"><fmt:message key="page.profile.status"/></label>
                                <label id="status" class="profile-info-visible"><c:out value=" ${userProfile.status}"/></label>
                                <form:input id="statusField" path="status" class="profile-info-hidden" value="${userProfile.status}" />
                            </p>
                        </div>
                    </fieldset>

                    <!-- Display contact information of user -->
                    <h2><fmt:message key="page.profile.contact.info" /></h2>
                    <fieldset>
                        <div class="span12">
                            <p>
                                <label for="emailField"><fmt:message key="page.profile.email"/></label>
                                <label id="email" class="profile-info-visible"><c:out value=" ${userProfile.email}"/></label>
                                <form:input id="emailField" path="email" class="profile-info-hidden" value="${userProfile.email}" />
                            </p>
                        </div>
                    </fieldset>
                    <%-- only display the edit profile button if the current logged in user matches the profile being viewed --%>
                    <c:set var="currentUsername"><sec:authentication property="principal.username" htmlEscape="false" /></c:set>
                    <c:if test="${currentUsername == userProfile.username}">
                        <fieldset>
                            <div class="span12">
                                <p>
                                    <input type="hidden" id="profileInfo" value="profile-info" />
                                    <button type="button" id="profileEdit" class="btn btn-primary profile-info-visible"><fmt:message key="page.profile.edit"/></button>
                                    <fmt:message key="page.profile.save" var="save"/>
                                    <input type="submit" class="btn btn-primary profile-info-hidden" value="${save}"/>
                                    <button type="button" class="btn profile-info-hidden" id="cancelEdit"><fmt:message key="page.profile.cancel"/></button>
                                </p>
                             </div>
                        </fieldset>
                    </c:if>
                </form:form>
            </div>

            <%--render the sub pages --%>
            <div>
                <%-- insert the region layout template --%>
                <tiles:insertTemplate template="${layout}"/>
                <div class="clear-float">&nbsp;</div>
            </div>
        </div>
        <div class="span3">
            <div>
                <%-- render the person profile parent page region/widgets --%>
                <c:forEach var="region" items="${page.regions}" varStatus="status">
                    <rave:region region="${region}" regionIdx="${status.count}" />
                </c:forEach>
            </div>
        </div>
    <div class="clear-float">&nbsp;</div>
    </div>
</div>
<div class="clear-float">&nbsp;</div>

<portal:register-init-script location="${'AFTER_RAVE'}">
    <script>
        $(function () {
            rave.initProviders();
            rave.initWidgets();
            rave.initUI();
            rave.layout.init();
            rave.personprofile.init();
        });
    </script>
</portal:register-init-script>