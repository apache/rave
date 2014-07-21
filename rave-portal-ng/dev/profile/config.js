(function(rave) {
  rave.config([
    '$stateProvider', '$urlRouterProvider',
    function($stateProvider, $urlRouterProvider) {
      // Our profile states
      $stateProvider.state('profile', {
        url: '/profile',
        templateUrl: 'profile/profile.html'
      });
    }
  ]);
})(rave);
