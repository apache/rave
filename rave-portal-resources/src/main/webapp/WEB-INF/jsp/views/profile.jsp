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

<!-- get the display name of user -->
<fmt:message key="page.profile.title" var="pageTitle">
   	<fmt:param><c:out value="${userProfile.displayName}" /></fmt:param>
</fmt:message>
<tiles:putAttribute name="pageTitleKey" value="${pageTitle}"/>
<tiles:importAttribute name="pageTitleKey" scope="request"/>

<!-- get the title of personal information -->
<fmt:message key="page.profile.personal.info" var="personalInfo"/>

<!-- get the title of basic information -->
<fmt:message key="page.profile.basic.info" var="basicInfo"/>

<!-- get the title of contact information -->
<fmt:message key="page.profile.contact.info" var="contactInfo"/>

<!-- Tag pages -->
<fmt:message key="page.profile.posts.page" var="postsTagPage"/>
<fmt:message key="page.profile.about.page" var="aboutTagPage"/>
<fmt:message key="page.profile.widgets.page" var="widgetsTagPage"/>
<fmt:message key="page.profile.friends.page" var="findFriendsTagPage"/>

<header>
   	<nav class="topnav">
       	<ul class="horizontal-list">
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
       		<li>
                <a href="<spring:url value="/app/store?referringPageId=${referringPageId}" />">
                  <fmt:message key="page.store.title"/>
                </a>
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
   	</nav>
	<h1>${pageTitle}</h1>
</header>
<div id="content">				
		<table class="profile-table">
  			<tr>
    			<%-- Body Content --%>
    			<td class="profile-body-column">
					<fieldset class="profile-submenu">
						<input type="hidden" id="referringPageId" value="${referringPageId}" />
							<ul>
								<li>
            						<label id="postsTag" class="profile-tag"><fmt:message key="page.profile.posts" /></label>    
            					</li>
            					<li>
            						<label id="aboutTag" class="profile-tag"><fmt:message key="page.profile.about" /></label>    
            					</li>
            					<li>
            						<label id="widgetsTag" class="profile-tag"><fmt:message key="page.profile.widgets" /></label>    
            					</li>
            					<li>
            						<label id="findFriendsTag" class="profile-tag"><fmt:message key="page.profile.find.friends" /></label>    
            					</li>
							</ul>
					</fieldset>
	
					<input type="hidden" id="defaultTagPage" value="${defaultTagPage}" />
	
					<fieldset id="postsTagPage" class="profile-tag-page">
						<tiles:insertTemplate template="${postsTagPage}" />
					</fieldset>
	
					<fieldset id="aboutTagPage" class="profile-tag-page">
						<tiles:insertTemplate template="${aboutTagPage}" />
					</fieldset>
	
					<fieldset id="widgetsTagPage" class="profile-tag-page">
						<tiles:insertTemplate template="${widgetsTagPage}" />
					</fieldset>
	
					<fieldset id="findFriendsTagPage" class="profile-tag-page">
						<tiles:insertTemplate template="${findFriendsTagPage}" />
					</fieldset>
    			</td>
  			</tr>
		</table>
</div>

<script src="//ajax.aspnetcdn.com/ajax/jQuery/jquery-1.6.4.min.js"></script>
<script src="//ajax.aspnetcdn.com/ajax/jquery.ui/1.8.16/jquery-ui.min.js"></script>
<script src="<spring:url value="/script/rave.js"/>"></script>
<script src="<spring:url value="/script/rave_api.js"/>"></script>
<script src="<spring:url value="/script/rave_profile.js"/>"></script>
<script>
    $(function () {
        rave.profile.init();
    });
</script>
