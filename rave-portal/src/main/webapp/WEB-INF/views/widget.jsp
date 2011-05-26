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
  
--%><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%--
--%><%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %><%--
--%><%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %><%--
--%><%@ taglib tagdir="/WEB-INF/tags" prefix="rave"%><%--
--%><jsp:useBean id="widget" scope="request" class="org.apache.rave.portal.model.Widget" /><%--
--%><rave:rave_generic_page>
<div id="banner">
    <span class="backToPage">
        <a href="<spring:url value="/index.html" />">Back to Rave</a>
    </span>
    <span>
        <a href="<spring:url value="/app/store?referringPageId=${referringPageId}" />">Back to Store</a>
    </span>
</div>

<div id="navigation">

</div>
<div id="content">
    <table id="storeWidgetDetail">
        <tr>
            <td class="widgetDetailLeft">
                <c:if test="${not empty widget.thumbnailUrl}">
                    <img class="storeWidgetThumbnail"
                         src="${widget.thumbnailUrl}"
                         title="${widget.title}}"
                         alt="thumbnail"
                         width="120" height="60"/>
                </c:if>
                <div class="widgetDetailMeta">
                    <div class="widgetVersion"></div>
                    <div class="widgetLastUpdated"></div>
                </div>
            </td>
            <td class="widgetDetailRight" style="vertical-align: top; padding-left: 10px;">

                <div class="storeWidgetDetail">
                    <div class="secondaryPageItemTitle"
                         style="display: inline-block; vertical-align: top; padding-top: 5px;">${widget.title}
                    </div>
                    <div id="widgetAdded_${widget.id}" class="storeButton">
                        <button class="storeItemButton"
                                id="addWidget_${widget.id}"
                                onclick="rave.api.rpc.addWidgetToPage({widgetId: ${widget.id}, pageId: ${referringPageId}});">
                            Add to Page
                        </button>
                    </div>
                </div>


                <div class="storeWidgetAuthor">
                    By:
                        ${widget.author}
                </div>
                <div class="storeWidgetDesc" >
                    ${widget.description}
                </div>
            </td>
            <td>
                <div class="widgetScreenshotTitle">Widget Preview</div>
                <div class="widgetScreenshot">
                    <c:if test="${not empty widget.screenshotUrl}">
                    <img src="${widget.screenshotUrl}"
                         alt="screenshot"
                         title="${widget.title} Screenshot"/>
                    </c:if>
                </div>
            </td>
        </tr>
    </table>

</div>
<script type="text/javascript">
    var rave = rave || {
        getContext : function() {
            return "<spring:url value="/app/" />";
        }
    }
</script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.6.1/jquery.min.js" type="text/javascript"></script>
<script src="<spring:url value="/script/rave_api.js"/>" type="text/javascript"></script>

</rave:rave_generic_page>