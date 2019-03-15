(function(){

    tenure_controller.$inject = [
        '$scope',
        '$state',
        'authService',
        '$window'
    ]

    function tenure_controller($scope, $state, authService, $window) {

        if(!authService.isAuthenticated()) {
            alert('You are not logged in. You need to log in to view this page.');
            authService.login();
        }

        $scope.submit = function() {
            $window.scrollTo(0, 0);
            $state.go('questionnaire');
        }
    }

    module.exports = tenure_controller;
})();