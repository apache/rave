<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %><%--
--%><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%--
--%><rave:rave_generic_page pageTitle="Error: ${errorMessage}">
    <div id="errorPage" >
        <div id="errorContent">
            <div id="errorTitle">Rave has suffered a brief meltdown</div>
            <div class="errorLogo"><img src="<spring:url value="images/error_generic.png" />" alt="Error has occurred"
                                        title="Error has occurred"/></div>
            <div class="errorMessage">
                Please bear with us while we fetch some ice cubes.<br/><br/>
                In the meantime, please try <a href="javascript: window.location.reload();">reloading</a>.<br/>
                <span style="font-size: 85%;">Interested in <a
                        href="javascript: portal.common.displayStackTraceDialog(true);">technical details</a>?</span>
            </div>
        </div>
        <button type="button"
                style="margin-bottom: 10px;"
                onClick="document.getElementById('errorStackDialog').style.display = 'block';">Close
        </button>
    </div>
    <div id="errorStackDialog" title="Error Details" style="display: none;">
        <button type="button"
                style="margin-bottom: 10px;"
                onClick="document.getElementById('errorStackDialog').style.display = 'none';">Close
        </button>
        <pre class="errorTrace">
            ${exception}

            Stack Trace:
            <c:forEach var="i" items="exception.stackTrace">
                ${i}
            </c:forEach>
        </pre>
    </div>
</rave:rave_generic_page>