(function() {

    invite_controller.$inject = [
        '$scope',
        'profileService',
        'authService',
        'toaster',
        'appcon'
    ];

    function invite_controller($scope, profileService, authService, toaster, appcon) {

        var dfs_message = 'Thank you for participating in this HIT. An anonymous profile has been created using the information you provided and ' +
                              'the results of your training rounds. Over the next 24hrs, teachers eligible for tenure promotion will review your profile and decide whether to invite you to provide feedback on their job performance. If selected we ' +
                              ' will send you an email using MTurk messaging system with an invite to another HIT. The HIT will contain a link to the teacher\'s dossier where you can submit the final evaluation. If invited, the HIT takes 3 minutes to complete and pays $2 per completion.';

        var ifs_message = 'Thank you for participating in this HIT. An anonymous profile has been created using the information you provided and ' +
                            'the results of your training rounds. Over the next 24hrs, supervisors of teachers eligible for tenure promotion will review your profile and decide whether to invite you to provide feedback on teachers\' job performance. If selected we' +
                            ' will send you an email using MTurk messaging system with an invite to another HIT. The HIT will contain a link to the teacher\'s dossier where you can submit the final evaluation. If invited, the HIT takes 3 minutes to complete and pays $2 per completion.';

        var dfs_thankYouMsg = "Thank you! Your profile is now available for teachers. If a teacher requests your feedback on their performance we will send you an invitation to another HIT using MTurk messaging system within the next 24hrs."
        var ifs_thankYouMsg = "Thank you! Your profile is now available for teacher supervisors. If a supervisor requests your feedback on one of their teacher performance we will send you an invitation to another HIT using MTurk messaging system within the next 24hrs."

        profileService.getProfile().then(function success(res) {
            $scope.mode = res.data.mode;
            init();
        }, handleFailure);

        function init() {
            $scope.logout = function() {
                $scope.startSpinner();
                appcon.postLogout()
                .then(function success(response) {
                    console.log('User successfully logged out!');
                    authService.endSession();
                    $scope.stopSpinner();
                }, function failure(response) {
                    $scope.stopSpinner();
                });
            }

            $scope.inviteSent = false;
            $scope.user = authService.getUserId();
            setMessage($scope.mode);

            $scope.alternativeEmail = '';
            $scope.useAlternativeEmail = false;
            $scope.checkAlternativeEmail = function () {
                $scope.useAlternativeEmail = !$scope.useAlternativeEmail;
            }

            $scope.submit = function() {
                var email;
                if($scope.useAlternativeEmail) {
                    if($scope.alternativeEmail === '') {
                        alert('Please enter email address to contact you');
                        return;
                    } else {
                        if (!$scope.inviteForm.alternativeEmail.$valid) {
                            alert('The email address you entered is invalid. Please enter a valid address.');
                            return;
                        } else {
                            email = $scope.alternativeEmail;
                        }
                    }
                } else {
                    email = null;
                }
                $scope.$parent.startSpinner();
                var req = { email, };
                appcon.requestInvite(req).then(
                    function success(res) {
                        $scope.$parent.stopSpinner();
                        $scope.inviteSent = true;
                    }, handleFailure
                )
            }
        }

        function setMessage(mode) {
            $scope.message = '';
            switch(mode) {
                case 'DFS':
                    $scope.message = dfs_message; // available because of closure
                    $scope.thankYouMsg = dfs_thankYouMsg;
                    break;
                case 'IFS':
                    $scope.message = ifs_message;
                    $scope.thankYouMsg = ifs_thankYouMsg;
                    break;
            }
        }


        function handleFailure(response) {
            var error = response.data === null ? 'Server unreachable' : response.data.message;
            toaster.pop('error', 'Error', 'Oops! we are having a bit of trouble! Details: ' + error);
            $scope.$parent.stopSpinner();
        }
    }

    module.exports = invite_controller;

})();