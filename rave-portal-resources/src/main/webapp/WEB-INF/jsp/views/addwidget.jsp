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
<header>
    <nav class="topnav">
        <ul class="horizontal-list">
          <c:if test="${not empty referringPageId}">
                <li>
                    <a href="<spring:url value="/app/store?referringPageId=${referringPageId}" />">
                        <fmt:message key="page.widget.backToStore"/>
                    </a>
                </li>
            </c:if>
            <li>
               <c:choose>
                <c:when test="${empty referringPageId}">
                    <spring:url value="/index.html" var="gobackurl" />
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
    <h1><fmt:message key="page.addwidget.title"/></h1>
</header>

<div id="content">
    <h2><fmt:message key="page.addwidget.form.header"/></h2>
    <form:errors path="widget" cssClass="error" element="p"/>
    <form:form id="newWidgetForm" action="add?referringPageId=${referringPageId}" commandName="widget" method="POST">
        <fieldset>
            <p><fmt:message key="form.some.fields.required"/></p>

            <p>
                <spring:bind path="url">
                    <label for="url"><fmt:message key="widget.url"/> *</label>
                    <input type="url" name="url" id="url"
                           placeholder="http://example.com/widget.xml" required="required"
                           class="long" value="<c:out value="${widget.url}"/>"/>
                </spring:bind>
                <form:errors path="url" cssClass="error"/>
            </p>

            <p>
                <label for="type1"><fmt:message key="widget.type"/> *</label>
                <label for="type1" class="formradio">
                    <form:radiobutton path="type" value="OpenSocial"/>
                    <fmt:message key="widget.type.OpenSocial"/>
                </label>
                <label for="type2" class="formradio" style="display:inline">
                    <form:radiobutton path="type" value="W3C"/>
                    <fmt:message key="widget.type.W3C"/>
                    <a id="w3cBrowseLink" style="position:relative;left:10px;" href="#"><fmt:message key="page.general.browse"/></a>
                </label>
                <form:errors path="type" cssClass="error"/>
            </p>

            <a href="#" class="storeItemButton"
                    id="fetchMetadataButton"
                    onclick="rave.api.rpc.getWidgetMetadata({
                                url: $('#url').get(0).value,
                                providerType: $('input:radio[name=type]:checked').val(),
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
                                    // update this field so we can pass widgets by key
                                    // (soon to be superseeded in wookie by using the guid instead)
                                    // remove when using 0.10.0 of wookie
                                    if($('input[name=type]:checked').val()=='W3C'){
                                        $('#url').val(widget.url);
                                    }
                                }
                            });">
                    <fmt:message key="page.getWidgetMetadata.button"/>
            </a>

            <div id="addWidgetForm">
                <p>
                    <form:label path="title"><fmt:message key="widget.title"/> *</form:label>
                    <form:input path="title" cssClass="long" required="required" autofocus="autofocus"/>
                    <form:errors path="title" cssClass="error"/>
                </p>

                <p>
                    <form:label path="description"><fmt:message key="widget.description"/> *</form:label>
                    <form:textarea path="description" required="required" cssClass="long"/>
                    <form:errors path="description" cssClass="error"/>
                </p>

                <p>
                    <spring:bind path="thumbnailUrl">
                        <label for="thumbnailUrl"><fmt:message key="widget.thumbnailUrl"/></label>
                        <input type="url" name="thumbnailUrl" id="thumbnailUrl"
                               placeholder="http://example.com/thumbnail.png" class="long"
                               value="<c:out value="${widget.thumbnailUrl}"/>"/>
                    </spring:bind>
                    <form:errors path="thumbnailUrl" cssClass="error"/>
                </p>

                <p>
                    <spring:bind path="screenshotUrl">
                        <label for="screenshotUrl"><fmt:message key="widget.screenshotUrl"/></label>
                        <input type="url" name="screenshotUrl" id="screenshotUrl"
                               placeholder="http://example.com/screenshot.png" class="long"
                               value="<c:out value="${widget.screenshotUrl}"/>"/>
                    </spring:bind>
                    <form:errors path="screenshotUrl" cssClass="error"/>
                </p>

                <p>
                    <spring:bind path="titleUrl">
                        <label for="titleUrl"><fmt:message key="widget.titleUrl"/></label>
                        <input type="url" name="titleUrl" id="titleUrl"
                               class="long" value="<c:out value="${widget.titleUrl}"/>"/>
                    </spring:bind>
                    <form:errors path="titleUrl" cssClass="error"/>
                </p>

                <p>
                    <form:label path="author"><fmt:message key="widget.author"/></form:label>
                    <form:input path="author" cssClass="long"/>
                    <form:errors path="author" cssClass="error"/>
                </p>
                <p>
                    <spring:bind path="authorEmail">
                        <label for="authorEmail"><fmt:message key="widget.authorEmail"/></label>
                        <input type="email" name="authorEmail" id="authorEmail" class="long"
                                value="<c:out value="${widget.authorEmail}"/>"/>
                    </spring:bind>
                    <form:errors path="titleUrl" cssClass="error"/>
                </p>
            </div>
            </fieldset>
            <div id="addWidgetFormSubmit">
                <fieldset>
                    <fmt:message key="page.addwidget.form.submit" var="submit"/>
                    <input type="submit" value="${submit}"/>
                </fieldset>
            </div>
    </form:form>
</div>
<script src="//ajax.aspnetcdn.com/ajax/jQuery/jquery-1.6.4.min.js"></script>
<script src="//ajax.aspnetcdn.com/ajax/jquery.ui/1.8.16/jquery-ui.min.js"></script>
<script src="<spring:url value="/script/rave.js"/>"></script>
<script src="<spring:url value="/script/rave_api.js"/>"></script>
<script src="<spring:url value="/app/messagebundle/rave_client_messages.js"/>"></script>
<script>
    $(function() {
        rave.setContext("<spring:url value="/app/" />");
        $('#addWidgetForm').hide();
        $('#addWidgetFormSubmit').hide();
        $('#w3cBrowseLink').hide();
        $('input[name=type]:first').attr('checked', true);
        $('input[name=type]').change(function(){
            if($('input[name=type]:checked').val()=='W3C'){
                $('#w3cBrowseLink').show();
            }
            else{
                $('#w3cBrowseLink').hide();
            }
        });

        $("#w3cBrowseForm").dialog({
            autoOpen: false,
            height: 300,
            width: 350,
            modal: true,
            buttons: {
                Cancel: function(){
                    $(this).dialog("close");
                }
            },
            close: function(){
                // clear contents
                $('#w3cwidgetsList').empty();
            }
        });

        $("#w3cBrowseLink").click(function() {
            rave.api.rpc.getWidgetMetadataGroup({
                url: "?all=true",
                providerType: "W3C",
                successCallback: function(result) {
                var i=0;
                var widgets = result.result;
                PostLoadW3cWidgets.setList(widgets);
                jQuery.each(widgets, function() {
                    $('#w3cwidgetsList')
                        .append(
                            $("<li/>")
                            .addClass("storeItem")
                            .append(
                                $("<div/>")
                                .addClass("storeItemLeft")
                                .append(
                                    $("<div/>")
                                    .attr("id", "w3cImageHolder"+i)
                                )
                                .append(
                                    $("<div/>")
                                    .attr("id", "widgetAdded")
                                    .addClass("storeButton")
                                    .append(
                                        $("<button/>")
                                        .addClass("storeItemButton")
                                        .attr("id", this.url)
                                        .attr("onclick", "updateRaveMetadata("+i+");")
                                        .text("Get Metadata")
                                    )
                                )
                            )
                            .append(
                                $("<div/>")
                                .addClass("storeItemCenter")
                                .append(
                                    $("<div/>")
                                    .addClass("secondaryPageItemTitle")
                                    .text(this.title)
                                )
                                .append(
                                    $("<div/>")
                                    .addClass("storeWidgetAuthor")
                                    .text(this.author)
                                )
                                .append(
                                    $("<div/>")
                                    .addClass("storeWidgetDesc")
                                    .text(this.description)
                                )
                            )
                            .append(
                                $("<div/>")
                                .addClass("clear-float")
                            )
                        )
                        // add the thumbnail image if found
                        if(this.thumbnailUrl!=null){
                            $('#w3cImageHolder'+i)
                            .append(
                                $("<img/>")
                                .addClass("storeWidgetThumbnail")
                                .attr("src", this.thumbnailUrl)
                                .attr("title", this.title)
                                .attr("alt", "")
                                .attr("width", "80")
                                .attr("height", "80")
                            )
                        }
                    i++;
                    });
                    $("#w3cBrowseForm").dialog("open");
                }
            })
        });
    });

   // use this object to hold the choices of w3c widgets after the page has loaded.
    var PostLoadW3cWidgets = new function PostLoadW3cWidgets() {
        this.list = null;
        this.setList = function (list) {
            this.list = list;
        }
        this.getList = function () {
            return this.list;
        }
        this.getListItemByIndex = function(idx){
            return this.list[idx];
        }
    }

    function updateRaveMetadata(id){
        if(id != null){
            widget = PostLoadW3cWidgets.getListItemByIndex(id);
            $('#title').val(widget.title);
            $('#description').val(widget.description);
            $('#thumbnailUrl').val(widget.thumbnailUrl);
            $('#screenshotUrl').val(widget.screenshotUrl);
            $('#titleUrl').val(widget.titleUrl);
            $('#author').val(widget.author);
            $('#authorEmail').val(widget.authorEmail);
            $('#url').val(widget.url);
            $('#addWidgetForm').show();
            $('#addWidgetFormSubmit').show();
        }
        $("#w3cBrowseForm").dialog("close");
    }
    </script>

    <div id="w3cBrowseForm" title="Browse available W3C widgets">
        <ul id="w3cwidgetsList" class="storeItems">
        </ul>
    </div>
