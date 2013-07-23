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
<div class="container-fluid">
	<div class="row-fluid">
	    <div class="span2">
	    	<div class="tabs-respond">
		        <rave:admin_tabsheader/>
	    	</div>
	    </div>
	    <div class="span10">
		    <article>
		        <a href="<spring:url value="/app/admin/widgets?referringPageId=${referringPageId}"/>"><fmt:message key="admin.widgetdetail.goback"/> </a>
		        <h2>
		            <c:out value="${widget.title}"/>
		        </h2>
		
		        <section class="formbox">

		            <form:form id="updateWidget" action="update" commandName="widget"
		                       method="POST" class="form-horizontal">
		                <fieldset>
		                	<legend>
		                		<fmt:message key="admin.widgetdetail.editdata"/>
		                		<div class="control-group pull-right">
		                    	<div class="controls">
		                        <a href="#" class="btn btn-warning storeItemButton" id="fetchMetadataButton">
		                            <fmt:message key="page.updateWidgetMetadata.button"/> </a>
		                        </div>
		                    </div>
		                	</legend>
		                	<form:errors cssClass="error" element="p"/>
		                    
		                    <input type="hidden" name="token"
		                           value="<c:out value="${tokencheck}"/>"/>
                            <input type="hidden" name="referringPageId" value="<c:out value="${referringPageId}"/>"/>
		                    <p>
		                        <fmt:message key="form.some.fields.required"/>
		                    </p>
		
		                    <div class="control-group">
		                        <form:label path="title" class="control-label">
		                            <fmt:message key="widget.title"/> *
		                        </form:label>
		                        <div class="controls">
			                        <form:input path="title" cssClass="long" required="required"
		                                    autofocus="autofocus"/>
		                        </div>
		                        <form:errors path="title" cssClass="error"/>
		                    </div>
		
		                    <div class="control-group">
		                        <spring:bind path="url">
		                            <label for="url" class="control-label">
		                            	<fmt:message key="widget.url"/> *
		                            </label>
		                            <div class="controls">
			                            <input type="url" name="url" id="url" placeholder="http://example.com/widget.xml" required="required" class="long" value="<c:out value="${widget.url}"/>"/>
		                            </div>
		                        </spring:bind>
		                        <form:errors path="url" cssClass="error"/>
		                    </div>
		
		                    <div class="control-group">
		                        <label for="type1" class="control-label">
		                        	<fmt:message key="widget.type"/> *
		                        </label> 
		                        <div class="controls">
		                        	<label for="type1" class="radio">
		                        		<form:radiobutton path="type" value="OpenSocial"/>&nbsp;
		                        		<fmt:message key="widget.type.OpenSocial"/> 
		                        	</label> 
		                        	<label for="type2" class="radio">
		                        		<form:radiobutton path="type" value="W3C"/>&nbsp;
		                        		<fmt:message key="widget.type.W3C"/> 
		                        	</label>
		                        </div>
		                        <form:errors path="type" cssClass="error"/>
		                    </div>
		                    <div class="control-group">
		                        <form:label path="description" class="control-label">
		                            <fmt:message key="widget.description"/> *
		                        </form:label>
		                        <div class="controls">
		                        	<form:textarea path="description" required="required" cssClass="long"/>
		                        </div>
		                        <form:errors path="description" cssClass="error"/>
		                    </div>
		
		                    <div class="control-group">
		                        <form:label path="featured" class="control-label">
		                            <fmt:message key="page.general.checkBox.featured"/>
		                        </form:label>
		                        <div class="controls">
			                        <form:checkbox path="featured" id="featured"/>
		                        </div>
		                        <form:errors path="featured" cssClass="error"/>
		                    </div>
		
		                    <div class="control-group">
		                        <form:label path="disableRendering" class="control-label">
		                            <fmt:message key="widget.disableRendering"/>
		                        </form:label>
		                        <div class="controls">
			                        <form:checkbox path="disableRendering" id="disableRendering"/>
		                        </div>
		                        <form:errors path="disableRendering" cssClass="error"/>
		                    </div>
		
		                    <div class="control-group">
		                        <form:label path="disableRenderingMessage" class="control-label">
		                            <fmt:message key="widget.disableRenderingMessage"/>
		                        </form:label>
		                        <div class="controls">
			                        <form:input path="disableRenderingMessage" cssClass="long" autofocus="autofocus"/>
		                        </div>
		                        <form:errors path="disableRenderingMessage" cssClass="error"/>
		                    </div>
		
		                    <div class="control-group">
		                        <form:label path="widgetStatus" class="control-label">
		                            <fmt:message key="widget.widgetStatus"/>
		                        </form:label>
		                        <div class="controls">
			                        <form:select path="widgetStatus" items="${widgetStatus}"/>
		                        </div>
		                    </div>
		
		                    <div class="control-group">
		                        <form:label path="categories" class="control-label">
		                            <fmt:message key="widget.categories"/>
		                        </form:label>
		                        <div class="controls">
			                        <form:select path="categories" items="${categories}" multiple="true" itemLabel="text" size="10"/>
		                        </div>
		                    </div>
		
		                    <div class="control-group">
		                        <spring:bind path="thumbnailUrl">
		                            <label for="thumbnailUrl" class="control-label">
		                            	<fmt:message key="widget.thumbnailUrl"/> 
		                            </label>
		                            <div class="controls">
			                            <input type="url" name="thumbnailUrl" id="thumbnailUrl" placeholder="http://example.com/thumbnail.png" class="long" value="<c:out value="${widget.thumbnailUrl}"/>"/>
		                        
				                         <c:if test="${not empty widget.thumbnailUrl}">
						                 <a data-toggle="modal" href="#thumbnailModal" class="btn btn-small btn-info">
						                 	<i class="icon-eye-open" title="View Image"></i> 
						                 </a>
						                    <div class="modal modal-image hide" id="thumbnailModal">
						                    	<div class="modal-header">
							                    	<button type="button" class="close" data-dismiss="modal">�</button>
							                    	<h4><fmt:message key="widget.thumbnailUrl"/></h4>
							                    </div>
							                    <div class="modal-body">
								                    <img src="<c:out value="${widget.thumbnailUrl}"/>" alt="<fmt:message key="widget.thumbnailUrl"/>" />
		    									</div>
		    									<div class="modal-footer">
		    										<a href="#" class="btn" data-dismiss="modal"><fmt:message key="page.general.close"/></a>
		    									</div>
						                    </div>
						                </c:if>
		                            </div>
				                </spring:bind>
		                        <form:errors path="thumbnailUrl" cssClass="error"/>
		                   </div>
		
		                    <div class="control-group">
		                        <spring:bind path="screenshotUrl">
		                            <label for="screenshotUrl" class="control-label">
		                            	<fmt:message key="widget.screenshotUrl"/> 
		                            </label>
		                            <div class="controls">
			                            <input type="url" name="screenshotUrl" id="screenshotUrl" placeholder="http://example.com/screenshot.png" class="long" value="<c:out value="${widget.screenshotUrl}"/>"/>
		                    
						                <c:if test="${not empty widget.screenshotUrl}">
						                <a data-toggle="modal" href="#screenshotModal" class="btn btn-small btn-info">
						                 	<i class="icon-eye-open" title="View Image"></i> 
						                 </a>
						                    <div class="modal modal-image hide" id="screenshotModal">
						                    	<div class="modal-header">
							                    	<button type="button" class="close" data-dismiss="modal">�</button>
							                    	<h4><fmt:message key="widget.screenshotUrl"/></h4>
							                    </div>
							                    <div class="modal-body">
								                    <img src="<c:out value="${widget.screenshotUrl}"/>" alt="<fmt:message key="widget.screenshotUrl"/>" />
		    									</div>
		    									<div class="modal-footer">
		    										<a href="#" class="btn" data-dismiss="modal"><fmt:message key="page.general.close"/></a>
		    									</div>
						                    </div>
						                </c:if> 
		                            </div>
		                        </spring:bind>
		                        <form:errors path="screenshotUrl" cssClass="error"/>
		                    </div>
		
		                    <div class="control-group">
		                        <spring:bind path="titleUrl">
		                            <label for="titleUrl" class="control-label">
		                            	<fmt:message key="widget.titleUrl"/>
		                            </label>
		                            <div class="controls">
			                            <input type="url" name="titleUrl" id="titleUrl" class="long" value="<c:out value="${widget.titleUrl}"/>"/>
		                            </div>
		                        </spring:bind>
		                        <form:errors path="titleUrl" cssClass="error"/>
		                    </div>
		
		                    <div class="control-group">
		                        <form:label path="author" class="control-label">
		                            <fmt:message key="widget.author"/>
		                        </form:label>
		                        <div class="controls">
			                        <form:input path="author" cssClass="long"/>
		                        </div>
		                        <form:errors path="author" cssClass="error"/>
		                    </div>
		                    
		                    <div class="control-group">
		                        <spring:bind path="authorEmail">
		                            <label for="authorEmail" class="control-label">
		                            	<fmt:message key="widget.authorEmail"/> 
		                            </label>
		                            <div class="controls">
			                            <input type="email" name="authorEmail" id="authorEmail" class="long" value="<c:out value="${widget.authorEmail}"/>"/>
		                            </div>
		                        </spring:bind>
		                        <form:errors path="titleUrl" cssClass="error"/>
		                    </div>
		                </fieldset>
		                <fieldset>
		                    <fmt:message key="admin.widgetdetail.updatebutton"
		                                 var="updateButtonText"/>
		                    <button class="btn btn-primary" type="submit" value="${updateButtonText}">${updateButtonText}</button>
		                </fieldset>
		            </form:form>
		        </section>	
		    </div>		
		    <div class="clear-float"></div>
        </article>
    </div>
</div>
<portal:register-init-script location="${'AFTER_RAVE'}">
    <script>
        require(["rave", "ui", "portal/rave_admin", "jquery"], function(rave, ui, raveAdmin, $){
            rave.registerOnInitHandler(function(){
                $('#fetchMetadataButton').click(function(){
                    rave.api.rpc.getWidgetMetadata({
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

            $(function() {
                raveAdmin.init();
            });
        })
    </script>
</portal:register-init-script>
