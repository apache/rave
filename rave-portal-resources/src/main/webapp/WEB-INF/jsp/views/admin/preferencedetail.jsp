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
<rave:navbar pageTitle="${pagetitle}"/>
<div class="container-fluid adminUI" id="preference_detail">
	<div class="row-fluid">
	    <div class="span2">
	    	<div class="tabs-respond">
		        <rave:admin_tabsheader/>
	    	</div>
	    </div>
	    <div class="span10">
	        <article>
	           <a href="<spring:url value="/app/admin/preferences?referringPageId=${referringPageId}"/>"><fmt:message key="admin.preferencedetail.goback"/></a>
	
	            <h2><fmt:message key="admin.preferences.shorttitle"/></h2>
	
	            <section class="span10">
	                <spring:url value="/app/admin/preferencedetail/update" var="formAction"/>
	                <form:form action="${formAction}" method="POST" modelAttribute="preferenceForm" class="form-horizontal">
	                    <form:errors cssClass="error" element="p"/>
                        <input type="hidden" name="token" value="<c:out value="${tokencheck}"/>"/>
                        <input type="hidden" name="referringPageId" value="<c:out value="${referringPageId}"/>"/>
                        <p><fmt:message key="form.some.fields.required"/></p>

                        <div class="control-group">
                            <form:label path="titleSuffix.value" class="control-label">
                            	<fmt:message key="admin.preferencedetail.titleSuffix"/>
                            </form:label>
                            <div class="controls">
	                            <form:input path="titleSuffix.value"/>
                            </div>
	                        <form:errors path="titleSuffix.value" cssClass="error"/>
                        </div>

	                    <div class="control-group">
	                    	<spring:bind path="pageSize.value">
	                        	<label for="pageSize" class="control-label">
	                        		<fmt:message key="admin.preferencedetail.pageSize"/> *
	                        	</label>
	                            <div class="controls">
	                                <input id="pageSize" name="pageSize.value" type="number" step="1" value="<c:out value="${status.value}"/>"/>
	                            </div>
	                        </spring:bind>
	                        <form:errors path="pageSize.value" cssClass="error"/>
	                    </div>
                        <div class="control-group">
                            <spring:bind path="defaultWidgetHeight.value">
                                <label for="defaultWidgetHeight" class="control-label">
                                    <fmt:message key="admin.preferencedetail.defaultWidgetHeight"/> *
                                </label>
                                <div class="controls">
                                    <input id="defaultWidgetHeight" name="defaultWidgetHeight.value" type="number" step="1" value="<c:out value="${status.value}"/>"/>
                                </div>
                            </spring:bind>
                            <form:errors path="defaultWidgetHeight.value" cssClass="error"/>
                        </div>
	                    <div class="control-group">
	                        <spring:bind path="javaScriptDebugMode.value">
	                            <form:label path="javaScriptDebugMode.value" class="control-label">
	                            	<fmt:message key="admin.preferencedetail.javaScriptDebugMode"/> *
	                            </form:label>
	                            <div class="controls">
	                                <form:select id="javaScriptDebugMode" path="javaScriptDebugMode.value">
	                                    <form:option value="0"><fmt:message key="admin.preferencedetail.javaScriptDebugMode.false"/></form:option>
	                                    <form:option value="1"><fmt:message key="admin.preferencedetail.javaScriptDebugMode.true"/></form:option>
	                                </form:select>
	                            </div>
	                            <form:errors path="javaScriptDebugMode.value" cssClass="error"/>
	                        </spring:bind>
	                    </div>
                        <div class="control-group">
                            <spring:bind path="showStackTrace.value">
                                <form:label path="showStackTrace.value" class="control-label">
                                    <fmt:message key="admin.preferencedetail.showStackTrace"/> *
                                </form:label>
                                <div class="controls">
                                    <form:select id="showStackTrace" path="showStackTrace.value">
                                        <form:option value="0"><fmt:message key="admin.preferencedetail.showStackTrace.false"/></form:option>
                                        <form:option value="1"><fmt:message key="admin.preferencedetail.showStackTrace.true"/></form:option>
                                    </form:select>
                                </div>
                                <form:errors path="showStackTrace.value" cssClass="error"/>
                            </spring:bind>
                        </div>
	                   <div class="control-group">
	                        <spring:bind path="initialWidgetStatus.value">
	                            <form:label path="initialWidgetStatus.value" class="control-label">
	                            	<fmt:message key="admin.preferencedetail.initialWidgetStatus"/> *
	                            </form:label>
	                            <div class="controls">
	                                <form:select id="initialWidgetStatus" path="initialWidgetStatus.value">
	                                    <form:option value="PREVIEW"><fmt:message key="admin.preferencedetail.initialWidgetStatus.preview"/></form:option>
	                                    <form:option value="PUBLISHED"><fmt:message key="admin.preferencedetail.initialWidgetStatus.published"/></form:option>
	                                </form:select>
	                            </div>
	                            <form:errors path="initialWidgetStatus.value" cssClass="error"/>
	                        </spring:bind>
	                    </div>	                    
	                   <div class="control-group">
                            <form:label path="externalMarketplaceUrl.value" class="control-label">
                            	<fmt:message key="admin.preferencedetail.externalMarketplaceUrl"/>
                            </form:label>
                            <div class="controls">
	                            <form:input path="externalMarketplaceUrl.value"/>
                            </div>
	                        <form:errors path="externalMarketplaceUrl.value" cssClass="error"/>
                        </div>
	                    <fieldset>
	                        <fmt:message key="admin.preferencedetail.updateButton" var="updateButtonText"/>
	                        <button class="btn btn-primary" type="submit" value="${updateButtonText}">${updateButtonText}</button>
	                    </fieldset>
	                </form:form>
	            </section>
	
	        </article>
	    </div>
	</div>
</div>
