(function() {

    procedure_controller.$inject = ['$scope', '$state', 'authService', '$window', 'profileService'];

    function procedure_controller($scope, $state, authService, $window, profileService) {
        $scope.$parent.startSpinner();

        if(!authService.isAuthenticated()) {
            alert('You are not logged in. You need to log in to view this page.');
            authService.login();
        }

        profileService.getProfile()
        .then(
            function success(response) {
                $scope.isExpert = response.data.mode === 'EXPERT';
                $scope.practiceRounds = response.data.practice;
                $scope.isRelative = response.data.relative === true;
                $scope.$parent.stopSpinner();
            },
            function failure() {
                var error = response.data === null ? 'Server unreachable' : response.data.message;
                toaster.pop('error', 'Error', 'Oops! we are having a bit of trouble! Details: ' + error);
                $scope.$parent.stopSpinner();
            }
        )

        $scope.next = function() {
            $window.scrollTo(0, 0);
            if($scope.isExpert)
                $state.go('evaluation', {id: 1});
            else
                $state.go('questionnaire');
        }
    }

    module.exports = procedure_controller;

})();