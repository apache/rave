<%@ include file="/WEB-INF/jsp/includes/taglibs.jsp" %>

<!DOCTYPE html>
<html ng-app="rave">
<head>
    <base href="/portal/app/angular/portal/">
    <link href="//netdna.bootstrapcdn.com/twitter-bootstrap/2.2.2/css/bootstrap-combined.min.css" rel="stylesheet">
    <link rel="stylesheet" href="/portal/static/css/rave.css"/>
</head>
<body>
<div class="wrapper" ng-include="'/portal/static/html/portal.html'" ng-cloak>

</div>

<script src="//cdnjs.cloudflare.com/ajax/libs/json2/20110223/json2.js"></script>
<script src="//cdnjs.cloudflare.com/ajax/libs/underscore.js/1.4.4/underscore-min.js"></script>
<script src="http://code.jquery.com/jquery-1.9.1.min.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.0.5/angular.js"></script>
<script src="http://code.angularjs.org/1.1.4/angular-resource.js"></script>
<script src="//cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/2.0.3/bootstrap.min.js"></script>


<script src="/gadgets/js/container:pubsub-2:open-views.js?c=1&container=default&debug=1"></script>

<script src="/portal/static/script/core/rave_core.js"></script>
<script src="/portal/static/script/core/rave_ajax.js"></script>
<script src="/portal/static/script/core/rave_api.js"></script>
<script src="/portal/static/script/core/rave_widget.js"></script>
<script src="/portal/static/script/core/rave_opensocial.js"></script>
<script src="/portal/static/script/core/rave_wookie.js"></script>


<script src="/portal/static/script/angular-portal/controllers.js"></script>
<script src="/portal/static/script/angular-portal/directives.js"></script>
<script src="/portal/static/script/angular-portal/services.js"></script>
<script src="/portal/static/script/angular-portal/routing.js"></script>
<script src="/portal/static/script/angular-portal/app.js"></script>


</body>
</html>