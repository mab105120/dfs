(function() {

    group_att_check_controller.$inject = [
        '$scope',
        '$state',
        'appcon',
        'toaster',
        'authService',
        '$stateParams'
    ];

    require('angularjs-dropdown-multiselect');

    function group_att_check_controller($scope, $state, appcon, toaster, authService, $stateParams) {

        if(!authService.isAuthenticated()) {
            alert('You are not logged in. You need to log in to view this page.');
            authService.login();
        }

        $scope.attempts = 0;

        $scope.logout = function() {
            authService.endSession();
        }

        $scope.showFailMessage = $stateParams.showFailMessage === 'true';

        $scope.daysSinceCompletionOptions = [
            '1 day ago',
            '2 days ago',
            '3 days ago',
            'I am not sure'
        ]

        $scope.inviterOptions = [
            'A teacher',
            'A teacher\'s supervisor',
            'A teacher\'s mentor',
            'I am not sure'
        ];

        $scope.purposeOptions = [
            'Tenure promotion',
            'Developmental training',
            'Salary raise',
            'I am not sure'
        ];

        $scope.confirmationOptions = [
            'Yes',
            'No'
        ];

        $scope.reasons = [
            {
                id: 1,
                label: 'My managerial experience is relevant.'
            },
            {
                id: 2,
                label: 'My profile was chosen randomly.'
            },
            {
                id: 3,
                label: 'I performed well during my training rounds.',
            },
            {
                id: 4,
                label: 'I have experience with appraising performance.',
            },
            {
                id: 5,
                label: 'I am not sure.',
            },
            {
                id: 6,
                label: 'My education qualifies me for this task.',
            },
            {
                id: 7,
                label: 'My profile reflects professional expertise.'
            },
            {
                id: 8,
                label: 'My profile reflects sound judgment.'
            }
        ];

        $scope.selectedReasons = [];

        $scope.multiSelectSettings = {
            showCheckAll: false,
            showUncheckAll: false,
            smartButtonMaxItems: 3,
        }

        $scope.explanation = "";

        $scope.resetBorderColor = function(q) {
            if (q === 2)
                $('#questionTwoInput').css('border', '');
            else if (q === 3)
                $('#questionThreeInput').css('border', '');
        }

        $scope.submit = function() {
            $scope.attempts = $scope.attempts + 1;
            if ($scope.selectedReasons.length === 0) {
                alert('Please make sure to answer all questions.');
                return;
            }
            $scope.$parent.startSpinner();
            var invitationReasons = "";
            if ($scope.attempts < 4) {
                if (($stateParams.mode === 'IFS' && $scope.inviter !== 'A teacher\'s supervisor') ||
                    ($stateParams.mode === 'DFS' && $scope.inviter !== 'A teacher')) {
                    alert('Your answer to question TWO is FALSE. You can not proceed unless you provide the correct answer. Please read the SUMMARY section of the invitation email carefully, you can find the answer there.\n\nAttempts: ' + $scope.attempts + '/3');
                    $scope.$parent.stopSpinner();
                    $('#questionTwoInput').css('border', '1px solid red');
                    return;
                }
                if ($scope.purpose !== 'Tenure promotion') {
                    alert('Your answer to question THREE is FALSE. You can not proceed unless you provide the correct answer. Please read the SUMMARY section of the invitation email carefully, you can find the answer there.\n\nAttempts: ' + $scope.attempts + '/3');
                    $scope.$parent.stopSpinner();
                    $('#questionThreeInput').css('border', '1px solid red');
                    return;
                }
            }
            for(var i =0; i < $scope.selectedReasons.length; i++) {
                if(i === 0)
                    invitationReasons = $scope.selectedReasons[i].label;
                else
                    invitationReasons += "," + $scope.selectedReasons[i].label;
            }
            payload = {
                daysSinceProfileComplete: $scope.daysSinceComplete,
                inviter: $scope.inviter,
                purpose: $scope.purpose,
                confirmation: $scope.confirmation,
                invitationReasons: invitationReasons,
                reasonExplanation: $scope.explanation
            };
            appcon.recordGroupAttentionCheck(payload).then(
                function success(res) {
                    var passed = res.data === "true";
                    if(passed) {
                        $state.go('evaluation', {id: 1});
                        toaster.pop('success', 'Saved', 'Your answers were successfully saved!');
                    } else {
                        $scope.showFailMessage = true;
                    }
                    $scope.$parent.stopSpinner();
                },handleFailure
            )
        }

        function handleFailure(response) {
            var error = response.data === null ? 'Server unreachable' : response.data.message;
            $scope.$parent.stopSpinner();
            toaster.pop('error', 'Error', 'Oops! we were not able to save your response: ' + error);
        }

    }

    module.exports = group_att_check_controller;

})();