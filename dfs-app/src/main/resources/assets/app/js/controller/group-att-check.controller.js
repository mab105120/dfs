(function() {

    group_att_check_controller.$inject = [
        '$scope',
        '$state',
        'appcon',
        'toaster',
        'authService'
    ];

    function group_att_check_controller($scope, $state, appcon, toaster, authService) {

        if(!authService.isAuthenticated()) {
            alert('You are not logged in. You need to log in to view this page.');
            authService.login();
        }

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


        $scope.submit = function() {
            $scope.$parent.startSpinner();
            payload = {
                daysSinceProfileComplete: $scope.daysSinceComplete,
                inviter: $scope.inviter,
                purpose: $scope.purpose,
                confirmation: $scope.confirmation
            };
            appcon.recordGroupAttentionCheck(payload).then(
                function success() {
                    $state.go('evaluation', {id: 1});
                    toaster.pop('success', 'Saved', 'Your answers were successfully saved!');
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