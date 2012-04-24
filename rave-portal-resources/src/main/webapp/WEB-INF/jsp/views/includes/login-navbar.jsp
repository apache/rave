<%@ page language="java" trimDirectiveWhitespaces="true" %>
<%@ include file="/WEB-INF/jsp/includes/taglibs.jsp" %>
<header>

    <nav>
        <div class="navbar navbar-fixed-top">
            <div class="container">
                <a href="/" class="brand">RAVE</a>

                <ul class="nav pull-right">
                    <li>
                        <form class="form-inline" action="<c:url value="/app/newpassword"/>" method="get">
                            <fmt:message key="page.login.forgot.password.button" var="requestNewPasswordButtonText"/>
                            <button class="btn btn-info" id="requestNewPasswordButton" type="submit" value="${requestNewPasswordButtonText}">${requestNewPasswordButtonText}</button>
                        </form>
                    </li>
                    <li class="divider-vertical"></li>
                    <li>
                        <form class="form-inline " action="<c:url value="/app/retrieveusername"/>" method="get">
                            <fmt:message key="page.login.forgot.username.button" var="requestNewUsernameButton"/>
                            <button class="btn btn-info" id="requestNewUsernameButton" type="submit" value="${requestNewUsernameButton}">${requestNewUsernameButton}</button>
                        </form>
                    </li>
                    <li class="divider-vertical"></li>

                    <li>
                        <form class="form-inline" action="<c:url value="/app/newaccount.jsp"/>" method="get">
                            <fieldset>
                                <fmt:message key="page.login.createaccount.button" var="createAccountButtonText"/>
                                <button class="btn btn-info" id="createNewAccountButton" type="submit" value="${createAccountButtonText}">${createAccountButtonText}</button>
                            </fieldset>
                        </form>
                    </li>
                    <li class="divider-vertical"></li>
                </ul>
            </div>
        </div>
    </nav>
</header>