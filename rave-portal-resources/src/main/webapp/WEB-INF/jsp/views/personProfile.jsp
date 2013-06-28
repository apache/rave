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
<%@ page errorPage="/WEB-INF/jsp/views/error.jsp" %>
<%@ include file="/WEB-INF/jsp/includes/taglibs.jsp" %>
<fmt:setBundle basename="messages"/>
<jsp:useBean id="userProfile" type="org.apache.rave.model.User" scope="request"/>
<sec:authentication property="principal.username" var="principleUsername" scope="request"/>
<sec:authentication property="principal.displayName" var="displayName" scope="request"/>

<!-- get the display name of user -->
<fmt:message key="page.personProfile.title" var="pageTitle">
    <fmt:param><c:out value="${userProfile.displayName}"/></fmt:param>
</fmt:message>
<tiles:putAttribute name="pageTitleKey" value="${pageTitle}"/>
<tiles:importAttribute name="pageTitleKey" scope="request"/>

<!-- get the title of personal information -->
<fmt:message key="page.personProfile.personal.info" var="personalInfo"/>

<!-- get the title of basic information -->
<fmt:message key="page.personProfile.basic.info" var="basicInfo"/>

<!-- get the title of contact information -->
<fmt:message key="page.personProfile.contact.info" var="contactInfo"/>
<fmt:message key="page.home.welcome" var="pagetitle">
    <fmt:param>
        <c:choose>
            <c:when test="${not empty displayName}"><c:out value="${displayName}"/></c:when>
            <c:otherwise><c:out value="${principleUsername}"/></c:otherwise>
        </c:choose>
    </fmt:param>
</fmt:message>
<rave:navbar pageTitle="${pagetitle}"/>

<div id="pageContent" class="container-fluid navbar-spacer">
    <div class="row-fluid">
        <div class="span9">
            <div id="personProfileContent">
                <c:url var="profileUrl" value="/app/person">
                    <c:param name="referringPageId" value="${referringPageId}"/>
                </c:url>
                <form:form id="editAccountForm" commandName="userProfile" action="${profileUrl}" method="POST"
                           class="form-horizontal">

                    <fieldset class="row-fluid" id="userProfilePrimaryData">
                        <div class="span2 profile-user-thumb">
                            <img src="<spring:url value="/static/images/profile_picture_placeholder.png"/>" alt=""/>
                        </div>

                        <div class="span10">
                            <!-- Display user info-->
                            <div class="profile-info-visible">
                                <h2><c:out value="${userProfile.displayName}"/></h2>

                                <h3><span id="givenName"><c:out value="${userProfile.givenName}"/></span>&nbsp;<span
                                        id="familyName"><c:out value="${userProfile.familyName}"/></span></h3>

                                <p class="profile-user-status">
                                    <fmt:message key="page.profile.status"/>
                                    &nbsp;<span id="status"><c:out value="${userProfile.status}"/></span>
                                </p>

                                <p class="profile-info-visible">
                                    <fmt:message key="page.profile.about.me"/>
                                    &nbsp;<span id="aboutMe"><c:out value=" ${userProfile.aboutMe}"/></span>
                                </p>
                            </div>
                            <!-- Display edit view of user info-->
                            <div class="profile-info-hidden">
                                <div class="control-group">
                                    <label for="displayNameField" class="control-label"><fmt:message
                                            key="page.profile.display.name"/></label>

                                    <div class="controls">
                                        <form:input id="displayNameField" path="displayName"
                                                    value="${userProfile.displayName}" class="span6"/>
                                    </div>
                                </div>
                                <div class="control-group">
                                    <label class="control-label pull-left"><fmt:message
                                            key="page.profile.first.name"/></label>
                                    <form:input id="givenNameField" path="givenName" class="span3 pull-left"
                                                value="${userProfile.givenName}"/>&nbsp;

                                    <label class="control-label pull-left"><fmt:message
                                            key="page.profile.last.name"/></label>
                                    <form:input id="familyNameField" path="familyName" class="span3 pull-left"
                                                value="${userProfile.familyName}"/>
                                </div>
                                <div class="control-group">
                                    <label for="statusField" class="control-label"><fmt:message
                                            key="page.profile.status"/></label>

                                    <div class="controls">
                                        <form:select path="status" class="span3" id="statusField">
                                            <fmt:message key="page.general.relation.single" var="singleText"/>
                                            <form:option value="${singleText}"
                                                         id="single_id">${singleText}</form:option>
                                            <fmt:message key="page.general.relation.committed" var="committedText"/>
                                            <form:option value="${committedText}"
                                                         id="committed_id">${committedText}</form:option>
                                            <fmt:message key="page.general.relation.married" var="marriedText"/>
                                            <form:option value="${marriedText}"
                                                         id="married_id">${marriedText}</form:option>
                                            <fmt:message key="page.general.relation.other" var="otherText"/>
                                            <form:option value="${otherText}" id="other_id">${otherText}</form:option>
                                        </form:select>
                                    </div>
                                </div>
                                <div class="control-group">
                                    <label for="aboutMeField" class="control-label"><fmt:message
                                            key="page.profile.about.me"/></label>

                                    <div class="controls">
                                        <form:textarea id="aboutMeField" path="aboutMe" value="${userProfile.aboutMe}"
                                                       class="span11"/>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </fieldset>

                    <!-- Display contact information of user -->
                    <fieldset class="row-fluid">
                        <div class=" span12">
                            <div class="well">
                                <h4><fmt:message key="page.profile.contact.info"/></h4>

                                <div class="profile-info-visible">
                                    <p id='email'><c:out value=" ${userProfile.email}"/></p>
                                </div>
                                <div class="profile-info-hidden">
                                    <div class="control-group">
                                        <label for="emailField" class="control-label"><fmt:message
                                                key="page.profile.email"/></label>

                                        <div class="controls">
                                            <form:input id="emailField" name="email" required="required" path="email"
                                                        class="span5" value="${userProfile.email}"/>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </fieldset>

                    <%-- only display the edit profile button if the current logged in user matches the profile being viewed --%>
                    <c:set var="currentUsername"><sec:authentication property="principal.username" htmlEscape="false"/></c:set>
                    <c:if test="${currentUsername == userProfile.username}">
                        <fieldset class="row-fluid">
                            <div class="span12">
                                <p>
                                    <input type="hidden" id="profileInfo" value="profile-info"/>
                                    <button type="button" id="profileEdit" class="btn btn-primary profile-info-visible">
                                        <fmt:message key="page.profile.edit"/></button>
                                    <fmt:message key="page.profile.save" var="save"/>
                                    <input type="submit" class="btn btn-primary profile-info-hidden" value="${save}"/>
                                    <button type="button" class="btn profile-info-hidden" id="cancelEdit"><fmt:message
                                            key="page.profile.cancel"/></button>
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
            <portal:person id="${page.ownerId}" var="owner"/>
            <button type="button" id="addRemoveFriend" value="${owner.username}"
                    class="btn btn-primary profile-info-visible"><fmt:message
                    key="page.personProfile.addremove.friends"/></button>
        </div>
        <div class="span3">
            <div>
                <%-- render the person profile parent page region/widgets --%>
                <c:forEach var="region" items="${page.regions}" varStatus="status">
                    <rave:region region="${region}" regionIdx="${status.count}"/>
                </c:forEach>
            </div>
        </div>
        <div class="clear-float">&nbsp;</div>
    </div>
</div>
<div id="userDialog" class="modal hide" data-backdrop="static">
    <div class="modal-header">
        <a href="#" class="close" data-dismiss="modal">&times;</a>

        <h3><fmt:message key="page.general.search.title"/></h3>
    </div>
    <div class="modal-body">
        <div id="userDialogContent">
            <div id="userContent">
                <div id="searchControls"><input id="searchTerm" name="searchTerm" type="text"/>
                    <input id="userSearchButton" value="<fmt:message key="page.store.search.button"/>" type="submit"/>
                    <input id="clearSearchButton" value="<fmt:message key="admin.clearsearch"/>" type="submit"
                           class="hide"/>
                </div>
                <div id="userSearchListHeader"></div>
                <div id="userSearchListPaging"></div>
                <div id="userSearchResults"></div>
            </div>
        </div>
    </div>
    <div class="modal-footer">
        <a href="#" class="btn" data-dismiss="modal" data-target="#userDialog">Close</a>
    </div>
</div>
<div class="clear-float">&nbsp;</div>

<input id="currentPageId" type="hidden" value="${page.id}"/>

<portal:register-init-script location="${'AFTER_RAVE'}">
    <script>
        require(["rave", "ui", "portal/rave_person_profile"],
                function (rave, ui, ravePersonProfile) {
                    rave.setDefaultView('profile');
                    rave.setOwner({
                        username: "<c:out value="${userProfile.username}"/>",
                        id: "<c:out value="${userProfile.id}"/>"
                    });

                    rave.registerOnInitHandler(function () {
                        ui.forms.validateEditAccountForm();
                        rave.renderWidgets('home');
                    })
                })
    </script>
</portal:register-init-script>
