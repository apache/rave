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
<fmt:message key="${pageTitleKey}" var="pagetitle"/>
<c:set var="canChangeUserStatus" value="${user.username ne loggedInUser}"/>
<rave:navbar pageTitle="${pagetitle}"/>
<div class="container-fluid">
	<div class="row-fluid">
	    <div class="span2">
	    	<div class="tabs-respond">
		        <rave:admin_tabsheader/>
	    	</div>
	    </div>
	    <div class="span10">
            <a href="<spring:url value="/app/admin/users?referringPageId=${referringPageId}"/>"><fmt:message key="admin.userdetail.goback"/></a>

            <section class="formbox">
                <h2><fmt:message key="page.login.createaccount"/></h2>
                <%@ include file="/WEB-INF/jsp/views/includes/new_user_form.jsp" %>
            </section>
        </article>
    </div>
</div>
