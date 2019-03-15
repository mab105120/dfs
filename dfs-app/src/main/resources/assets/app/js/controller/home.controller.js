(function() {

    'use strict';

    home_controller.$inject = [
            '$scope',
            '$state',
            'authService',
            'appcon',
            'toaster',
            'profileService'
        ];


    function home_controller($scope, $state, authService, appcon, toaster, profileService) {
        $scope.$parent.startSpinner();
        init();
        function init() {

            if(!authService.isAuthenticated()) {
                alert('You are not logged in. You need to log in to view this page.');
                authService.login();
            }

            $scope.showAlert = false;

            profileService.getProfile().then(
                function(response) {
                    $scope.duration = response.data.duration;
                    $scope.isExpert = response.data.mode === 'EXPERT';
                    appcon.getProgress()
                    .then(function success(response) {
                        if(response.data.completed.length !== 0)
                            $scope.showAlert = true;
                            $scope.$parent.stopSpinner();
                    }, handleFailure);
                },
                handleFailure
            );
        }
        $scope.start = function() {
            $state.go('procedure');
        };

        function handleFailure(response) {
            var error = response.data === null ? 'Server unreachable' : response.data.message;
            toaster.pop('error', 'Error', 'Oops! we are having a bit of trouble! Details: ' + error);
            $scope.$parent.stopSpinner();
        }

    };

    module.exports = home_controller;
})();