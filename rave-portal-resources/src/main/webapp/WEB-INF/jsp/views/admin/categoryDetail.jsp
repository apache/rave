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
<rave:header pageTitle="${pagetitle}"/>
<rave:admin_tabsheader/>
<div class="pageContent">
    <article class="admincontent">
        <ul class="horizontal-list goback">
            <li><a href="<spring:url value="/app/admin/categories"/>"><fmt:message key="admin.categoryDetail.goback"/></a>
            </li>
        </ul>
        <h2><c:out value="${category.text}"/></h2>

        <div class="rightcolumn">
                <section class="formbox">
                    <h3><fmt:message key="admin.delete"/><c:out value=" ${category.text}"/></h3>
                    <form:form id="deleteCategory" action="delete" commandName="category" method="POST">
                        <fieldset>
                            <input type="hidden" name="token" value="<c:out value="${tokencheck}"/>"/>

                            <div>
                                <ul class="checkboxlist">
                                    <li>
                                        <input type="checkbox" name="confirmdelete" id="confirmdelete" value="true"/>
                                        <label for="confirmdelete"><fmt:message
                                                key="admin.categoryDetail.action.delete.confirm"/></label>
                                        <c:if test="${missingConfirm}">
                                            <p class="error"><fmt:message
                                                    key="admin.userdetail.action.delete.confirm.required"/></p>
                                        </c:if>
                                    </li>
                                </ul>
                            </div>
                        </fieldset>
                        <fieldset>
                            <p>
                                <fmt:message key="admin.category.delete" var="deleteCategoryMsg"/>
				<input type="submit" value="${deleteCategoryMsg}"/>
                            </p>
                        </fieldset>
                    </form:form>
                </section>


        </div>

        <div class="leftcolumn">
            <section class="formbox">
                <h3><fmt:message key="admin.categoryDetail.editData"/></h3>
                <form:form id="updateCategory" action="update" commandName="category" method="POST">
                    <form:errors cssClass="error" element="p"/>
                    <fieldset>
                        <input type="hidden" name="token" value="<c:out value="${tokencheck}"/>"/>
                        <p>
                            <label for="text"><fmt:message
                                    key="admin.categoryDetail.label.text"/></label>

                            <form:input id="text" path="text" required="required" value="${category.text}" autofocus="autofocus"/>
                            <form:errors path="text" cssClass="error"/>
                        </p>
                    </fieldset>
                    <fieldset>
			<fmt:message key="admin.category.update" var="updateCategoryMsg" />
                        <input type="submit" value="${updateCategoryMsg}"/>
                    </fieldset>
                </form:form>
            </section>

        </div>

        <div class="clear-float">

        </div>


    </article>
</div>
