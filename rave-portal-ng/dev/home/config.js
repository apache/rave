(function(rave) {
  rave.config([
    '$stateProvider', '$urlRouterProvider',
    function($stateProvider, $urlRouterProvider) {
      // Make this our default route
      $urlRouterProvider.otherwise('/home');

      // Set up our states
      $stateProvider.state('home', {
          url: '/home',
          templateUrl: 'home/home.html'
        });
      }
  ]);
})(rave);
