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
<fmt:message key="page.addwidget.title" var="pagetitle"/>
<rave:navbar pageTitle="${pagetitle}"/>

<div class="container-fluid navbar-spacer">
    <div class="row-fluid">
	    <ul class="nav nav-tabs">
	          <li class="active"><a href="<spring:url value="/app/store/widget/add?referringPageId=${referringPageId}" />">OpenSocial</a></li>
	          <li><a href="<spring:url value="/app/store/widget/add/w3c?referringPageId=${referringPageId}" />">W3C</a></li>
	          
	          <c:if test="${not empty marketplace and not empty marketplace.value}">
	          <li><a href="<spring:url value="/app/marketplace?referringPageId=${referringPageId}" />">Marketplace</a></li>
	          </c:if>
	    </ul> 
    </div>
</div>
<div class="row-fluid tab-content">
    <div class="tab-padding">
    
    <form:errors path="widget" cssClass="error" element="p"/>
    <form:form cssClass="form-horizontal" id="newWidgetForm" action="add?referringPageId=${referringPageId}" commandName="widget" method="POST">
        <fieldset>
            <div class="control-group label label-important"><fmt:message key="form.some.fields.required"/></div>

            <div class="control-group">
                <spring:bind path="url">
                    <label class="control-label" for="url"><fmt:message key="widget.url"/> *</label>
                    <div class="controls"><input class="input-xlarge" type="url" name="url" id="url"
                                                 placeholder="http://example.com/widget.xml" required="required"
                                                 value="<c:out value="${widget.url}"/>"/></div>
                </spring:bind>
                <form:errors path="url" cssClass="error"/>
            </div>
            
            <form:hidden path="type" value="OpenSocial"/>
			<div class="control-group">
	            <a href="#" class="btn btn-primary"
	               id="fetchMetadataButton">
	                <fmt:message key="page.getWidgetMetadata.button"/>
	            </a>
			</div>
            <div class="clearfix" id="addWidgetForm">

                <div class="control-group">
                    <form:label cssClass="control-label" path="title"> <fmt:message key="widget.title"/> *</form:label>
                    <div class="controls">
                        <form:input path="title" cssClass="input-xlarge" required="required" autofocus="autofocus"/></div>
                    <form:errors path="title" cssClass="error"/>
                </div>

                <div class="control-group">
                    <form:label cssClass="control-label" path="description"><fmt:message key="widget.description"/> *</form:label>
                    <div class="controls"><form:textarea path="description" required="required" cssClass="input-xlarge"/></div>
                    <form:errors path="description" cssClass="error"/>
                </div>

                <div class="control-group">
                    <spring:bind path="thumbnailUrl">
                        <label class="control-label" for="thumbnailUrl"><fmt:message key="widget.thumbnailUrl"/></label>
                        <div class="controls"><input type="url" name="thumbnailUrl" id="thumbnailUrl"
                                                     placeholder="http://example.com/thumbnail.png" class="input-xlarge"
                                                     value="<c:out value="${widget.thumbnailUrl}"/>"/></div>
                    </spring:bind>
                    <form:errors path="thumbnailUrl" cssClass="error"/>
                </div>

                <div class="control-group">
                    <spring:bind path="screenshotUrl">
                        <label class="control-label" for="screenshotUrl"><fmt:message key="widget.screenshotUrl"/></label>
                        <div class="controls">
                            <input type="url" name="screenshotUrl" id="screenshotUrl" placeholder="http://example.com/screenshot.png" class="input-xlarge"
                                   value="<c:out value="${widget.screenshotUrl}"/>"/></div>
                    </spring:bind>
                    <form:errors path="screenshotUrl" cssClass="error"/>
                </div>

                <div class="control-group">
                    <spring:bind path="titleUrl">
                        <label class="control-label" for="titleUrl"><fmt:message key="widget.titleUrl"/></label>
                        <div class="controls"><input type="url" name="titleUrl" id="titleUrl"
                                                     class="input-xlarge" value="<c:out value="${widget.titleUrl}"/>"/></div>
                    </spring:bind>
                    <form:errors path="titleUrl" cssClass="error"/>
                </div>

                <div class="control-group">
                    <form:label cssClass="control-label" path="author"><fmt:message key="widget.author"/></form:label>
                    <div class="controls"><form:input path="author" cssClass="input-xlarge"/>
                        <form:errors path="author" cssClass="error"/>
                    </div>

                </div>
                <div class="control-group">
                    <spring:bind path="authorEmail">
                        <label class="control-label" for="authorEmail"><fmt:message key="widget.authorEmail"/></label>
                        <div class="controls"><input type="email" name="authorEmail" id="authorEmail" class="input-xlarge"
                                                     value="<c:out value="${widget.authorEmail}"/>"/></div>
                    </spring:bind>
                    <form:errors path="titleUrl" cssClass="error"/>
                </div>
            </div>
        </fieldset>
        <div id="addWidgetFormSubmit">
            <fieldset>
                <fmt:message key="page.addwidget.form.submit" var="submit"/>
                <button class="btn btn-primary" type="submit" value="${submit}">${submit}</button>
            </fieldset>
        </div>
    </form:form>
</div>

<portal:register-init-script location="${'AFTER_RAVE'}">
<script>
    require(['ui', 'rave', "jquery"], function(ui, rave, $){
        rave.registerOnInitHandler(function(){
            $('#fetchMetadataButton').click(function(){
                rave.api.rpc.getWidgetMetadata({
                    url: $('#url').get(0).value,
                    providerType: 'OpenSocial',
                    successCallback: function(result) {
                        var widget = result.result;
                        $('#title').val(widget.title);
                        $('#description').val(widget.description);
                        $('#thumbnailUrl').val(widget.thumbnailUrl);
                        $('#screenshotUrl').val(widget.screenshotUrl);
                        $('#titleUrl').val(widget.titleUrl);
                        $('#author').val(widget.author);
                        $('#authorEmail').val(widget.authorEmail);
                        $('#addWidgetForm').show();
                        $('#addWidgetFormSubmit').show();
                    },
                    errorCallback: function(){
                        alert(ui.getClientMessage("api.widget_metadata.parse_error"));
                    },
                    alertInvalidParams: function(){
                        alert(ui.getClientMessage("api.widget_metadata.invalid_params"));
                    }
                })
            })
        })

       });
</script>
</portal:register-init-script>

