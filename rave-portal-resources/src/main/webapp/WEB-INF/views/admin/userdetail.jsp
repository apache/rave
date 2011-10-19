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
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="rave" %>
<fmt:setBundle basename="messages"/>

<fmt:message key="admin.userdetail.title" var="pagetitle"/>

<c:set var="canChangeUserStatus" value="${user.username ne loggedInUser}"/>

<rave:rave_generic_page pageTitle="${pagetitle}">
    <rave:header pageTitle="${pagetitle}"/>
    <rave:admin_tabsheader/>
    <div id="pageContent">
        <article class="admincontent">
            <ul class="horizontal-list goback">
                <li><a href="<spring:url value="/app/admin/users"/>"><fmt:message key="admin.userdetail.goback"/></a>
                </li>
            </ul>
            <h2><c:out value="${user.username}"/></h2>

            <div class="rightcolumn">

            </div>

            <div class="leftcolumn">
                <section class="formbox">
                    <h3><fmt:message key="admin.userdetail.editdata"/></h3>
                    <form:form id="updateUserProfile" action="update" commandName="user" method="POST">
                        <form:errors cssClass="error" element="p"/>
                        <fieldset>
                            <p>
                                <label for="email"><fmt:message key="page.general.email"/></label>
                                <spring:bind path="email">
                                    <input type="email" name="email" id="email" value="<c:out value="${status.value}"/>"
                                           class="long"/>
                                </spring:bind>
                            </p>

                            <p>
                                <label for="openIdField"><fmt:message key="page.userprofile.openid.url"/></label>
                                <spring:bind path="openId">
                                    <input type="url" id="openIdField" name="openId"
                                           value="<c:out value="${status.value}"/>" class="long"/>
                                </spring:bind>
                                <form:errors path="openId" cssClass="error"/>
                            </p>

                            <div>
                                <span class="label"><fmt:message key="admin.userdata.accountstatus"/></span>
                                <ul class="checkboxlist">
                                    <li>
                                        <fmt:message key="admin.userdata.enabled" var="labelEnabled"/>
                                        <form:checkbox path="enabled" label="${labelEnabled}"
                                                       disabled="${canChangeUserStatus ne true}"/>
                                    </li>
                                    <li>
                                        <fmt:message key="admin.userdata.expired" var="labelExpired"/>
                                        <form:checkbox path="expired" label="${labelExpired}"
                                                       disabled="${canChangeUserStatus ne true}"/>
                                    </li>
                                    <li>
                                        <fmt:message key="admin.userdata.locked" var="labelLocked"/>
                                        <form:checkbox path="locked" label="${labelLocked}"
                                                       disabled="${canChangeUserStatus ne true}"/>
                                    </li>
                                </ul>
                            </div>
                        </fieldset>
                        <fieldset>
                            <span class="label"><fmt:message key="admin.userdata.authorities"/></span>
                            <%--@elvariable id="authorities" type="org.apache.rave.portal.model.util.SearchResult<org.apache.rave.portal.model.Authority>"--%>
                            <ul class="checkboxlist">
                                <form:checkboxes path="authorities" items="${authorities.resultSet}"
                                                 itemLabel="authority" itemValue="authority" element="li" />
                            </ul>
                        </fieldset>
                        <fieldset>
                            <fmt:message key="page.userprofile.button" var="updateButtonText"/>
                            <input type="submit" value="${updateButtonText}"/>
                        </fieldset>
                    </form:form>
                </section>

            </div>

            <div style="clear:both;">

            </div>


        </article>
    </div>
</rave:rave_generic_page>