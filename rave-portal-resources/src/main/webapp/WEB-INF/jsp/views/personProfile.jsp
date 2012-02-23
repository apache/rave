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
<div id="person-profile-left">
    <div id="personProfileContent">
        <!-- Display personal information of user-->
        <h2><fmt:message key="page.personProfile.personal.info" /></h2>
        <p>
            <fmt:message key="page.personProfile.first.name"/>
            <c:out value="${userProfile.givenName}"/>
        </p>
        <p>
            <fmt:message key="page.personProfile.last.name"/> 
            <c:out value="${userProfile.familyName}"/>
        </p>
        <p>
            <fmt:message key="page.personProfile.display.name"/>
            <c:out value="${userProfile.displayName}"/>
        </p>
            
        <!-- Display basic information of user -->
        <h2><fmt:message key="page.personProfile.basic.info" /></h2>
        <p>
            <fmt:message key="page.personProfile.about.me"/>
            <c:out value=" ${userProfile.aboutMe}"/>
        </p>
        <p>
            <fmt:message key="page.personProfile.status"/>
            <c:out value=" ${userProfile.status}"/>    		
        </p> 			
        
        <!-- Display contact information of user -->
        <h2><fmt:message key="page.personProfile.contact.info" /></h2>
        <p>
            <fmt:message key="page.personProfile.email"/>
            <c:out value=" ${userProfile.email}"/>    		
        </p>
    </div>
    <%--render the sub pages --%>
    <div id="pageContent" class="person-profile-page-content">
        <div class="regions">
            <%-- insert the region layout template --%>
            <tiles:insertTemplate template="${layout}"/>
        </div>
        <div class="clear-float">&nbsp;</div>
    </div>
</div>
<div id="person-profile-right">
    <div>
        <%-- render the person profile parent page region/widgets --%>
        <c:forEach var="region" items="${page.regions}" varStatus="status">
            <rave:region region="${region}" regionIdx="${status.count}" />
        </c:forEach>
    </div>
</div>
<div class="clear-float">&nbsp;</div>

<script>
    //Define the global widgets map.  This map will be populated by RegionWidgetRender providers.
    var widgetsByRegionIdMap = {};
</script>
<portal:render-script location="${'BEFORE_LIB'}" />
<script src="//cdnjs.cloudflare.com/ajax/libs/json2/20110223/json2.js"></script>
<script src="//ajax.aspnetcdn.com/ajax/jQuery/jquery-1.6.4.min.js"></script>
<script src="//ajax.aspnetcdn.com/ajax/jquery.ui/1.8.16/jquery-ui.min.js"></script>
<script src="//ajax.aspnetcdn.com/ajax/jquery.validate/1.8.1/jquery.validate.min.js"></script>
<!--[if lt IE 9]><script src=//css3-mediaqueries-js.googlecode.com/svn/trunk/css3-mediaqueries.js></script><![endif]-->
<portal:render-script location="${'AFTER_LIB'}" />
<portal:render-script location="${'BEFORE_RAVE'}" />
<script src="<spring:url value="/script/rave.js"/>"></script>
<script src="<spring:url value="/script/rave_api.js"/>"></script>
<script src="<spring:url value="/script/rave_opensocial.js"/>"></script>
<script src="<spring:url value="/script/rave_wookie.js"/>"></script>
<script src="<spring:url value="/script/rave_layout.js"/>"></script>
<script src="<spring:url value="/script/rave_person_profile.js"/>"></script>
<script src="<spring:url value="/app/messagebundle/rave_client_messages.js"/>"></script>
<portal:render-script location="${'AFTER_RAVE'}" />
<script>
    $(function () {
        rave.setContext("<spring:url value="/app/" />");
        rave.initProviders();
        rave.initWidgets(widgetsByRegionIdMap);
        rave.initUI();
        rave.layout.init();
        rave.personprofile.init();
    });
</script>
