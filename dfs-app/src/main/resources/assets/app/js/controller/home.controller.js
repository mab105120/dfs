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
        $scope.home = true;
        function init() {

            if(!authService.isAuthenticated()) {
                alert('This application is only for MTurk workers. Access denied.');
                $scope.$parent.stopSpinner();
                $state.go('welcome', { endSession: false });
            }

            $scope.showAlert = false;

            profileService.getProfile().then(
                function(response) {
                    $scope.profile = response.data;
                    $scope.duration = response.data.duration;
                    $scope.isExpert = response.data.mode === 'EXPERT';
                    appcon.getInvitationStatus().then(function success(res) {
                        if (res.data.invitationSent) {
                            $scope.home = false;
                            $scope.invitationHome = true;
                            $scope.$parent.stopSpinner();
                        } else {
                            appcon.getProgress()
                            .then(function success(response) {
                                if(response.data.completed.length !== 0)
                                    $scope.showAlert = true;
                                    $scope.$parent.stopSpinner();
                            }, handleFailure);
                        }
                    }, handleFailure);
                },
                handleFailure
            );
        }
        $scope.start = function() {
            $state.go('procedure');
        };

        $scope.continueFnc = function() {
            $state.go('group-att-check', { showFailMessage: false, mode: $scope.profile.mode, training: $scope.profile.training });
        }

        function handleFailure(response) {
            var error = response.data === null ? 'Server unreachable' : response.data.message;
            toaster.pop('error', 'Error', 'Oops! we are having a bit of trouble! Details: ' + error);
            $scope.$parent.stopSpinner();
        }

    };

    module.exports = home_controller;
})();