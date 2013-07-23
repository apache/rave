<!DOCTYPE html>
<html>
<head>
    <base href="/portal/app/angular/${context}/">
    <link href="//netdna.bootstrapcdn.com/twitter-bootstrap/2.2.2/css/bootstrap-combined.min.css" rel="stylesheet">
    <link href="http://code.jquery.com/ui/1.10.2/themes/smoothness/jquery-ui.css" rel="stylesheet">
    <link rel="stylesheet" href="/portal/static/css/rave.css"/>

    <script data-main="/portal/static/script/${context}/main.js"
            src="//cdnjs.cloudflare.com/ajax/libs/require.js/2.1.5/require.min.js"></script>
</head>
<body>
<div class="wrapper" ng-include="'/portal/static/html/${context}/index.html'" ng-cloak>

</div>

</body>
</html>