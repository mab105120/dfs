(function() {

    progress_controller.$inject = [
        '$scope',
        '$state',
        'appcon',
        'authService',
        'profileService',
        'toaster'
    ];

    function progress_controller($scope, $state, appcon, authService, profileService, toaster) {
        $scope.$parent.startSpinner();
        profileService.getProfile().then(function(response) {
            $scope.profile = response.data;
            appcon.getInvitationStatus().then(function(res) {
                $scope.profile.invitationSent = res.data.invitationSent;
                $scope.profile.invitationPending = res.data.invitationPending;
                init();
            }, handleFailure);
        }, handleFailure);

        function init() {

            if(!authService.isAuthenticated()) {
                alert('You are not logged in. You need to log in to view this page.');
                authService.login();
            }

            $scope.qual_redirect_url = localStorage.getItem('qual_redirect_url');

            $scope.complete = false;
            $scope.inviteIsPending = false;
            $scope.actionsRequired = false;

            $scope.rows = [];

            function orderStatusRows(status1, status2) {
                return status1.priority - status2.priority;
            }
            var status_priority = 0;

            if($scope.profile.mode !== 'EXPERT') {
                $scope.rows.push(
                    {
                        priority: status_priority++,
                        id: 'QUEST_DEMO',
                        display: 'General questionnaire'
                    },
                    {
                        priority: status_priority++,
                        id: 'QUEST_EXP',
                        display: 'Professional experience questionnaire'
                    }
                );
            }

            function addPracticeEvaluationsToRows() {
                var totalPracticeEvaluations = $scope.profile.practice;
                if(totalPracticeEvaluations == 0) return;
                for(var i = 1; i <= totalPracticeEvaluations; i++) {
                    $scope.rows.push({
                        priority: status_priority++,
                        id: 'EVALUATION_P' + i,
                        display: 'Teacher Evaluation (Practice) ' + i + ' / ' + totalPracticeEvaluations
                    })
                }
            }

            function addEvaluationsToRows() {
                if($scope.profile.mode === 'NFS' || $scope.profile.invitationSent) {
                    var totalEvaluations = $scope.profile.evaluations;
                    for(i = 1; i <= totalEvaluations; i++) {
                        $scope.rows.push(
                            {
                                priority: status_priority++,
                                id: 'EVALUATION_' + i,
                                display: 'Teacher Evaluation ' + i + ' / ' + totalEvaluations
                            }
                        );
                    }
                }
            }

            function addConsentRequestToRows() {
                if($scope.profile.mode !== 'NFS' && !$scope.profile.invitationSent) {
                    $scope.rows.push({
                        priority: status_priority++,
                        id: 'CONSENT',
                        display: 'Provide Consent'
                    });
                }
            }

            addPracticeEvaluationsToRows();
            addEvaluationsToRows();
            addConsentRequestToRows();
            $scope.rows.sort();

            appcon.getProgress()
            .then(function success(response) {
                var completed = new Map();
                angular.forEach(response.data.completed, function(item) {
                    completed.set(item, item);
                });

                var allCompleted = true;

                angular.forEach($scope.rows, function(item) {
                    if(completed.get(item.id) !== undefined)
                        item.status = 'Complete';
                    else {
                        item.status = 'Not Started';
                        allCompleted = false;
                    }
                });
                if (!allCompleted) {
                    $scope.actionsRequired = true;
                } else {
                    if ($scope.profile.mode !== 'NFS' && $scope.profile.invitationPending) {
                        $scope.inviteIsPending = true;
                    } else {
                        $scope.complete = true;
                    }
                }

                if(allCompleted) {
                    var attention = localStorage.getItem('attentionCheck');
                    if(attention !== undefined && attention === 'false')
                        $scope.qual_redirect_url += '&term=attention';
                }
                adjustRowStatuses();
                $scope.$parent.stopSpinner();
            }, handleFailure);

            // Change the status of first uncompleted row to Next
            function adjustRowStatuses() {
                for (var i = 0; i < $scope.rows.length; i++) {
                    if ($scope.rows[i].status === 'Not Started') {
                        $scope.rows[i].status = 'Next';
                        break;
                    }
                }
            }
        }

        function handleFailure(response) {
            var error = response.data === null ? 'Server unreachable' : response.data.message;
            toaster.pop('error', 'Error', 'Oops! we are having a bit of trouble! Details: ' + error);
            $scope.$parent.stopSpinner();
        }

        $scope.edit = function(id) {
            if(id === 'QUEST_DEMO') {
                $state.go('questionnaire');
            } else if (id === 'QUEST_EXP') {
                $state.go('experience');
            } else if (id === 'QUEST_CON') {
                $state.go('confidence');
            } else if(id.startsWith('EVALUATION_P')) {
                $state.go('evaluation', {id: id.substr(id.indexOf('_') + 1)});
            } else if(id.startsWith('EVALUATION')) {
                $state.go('evaluation', {id: parseInt(id.substr(id.indexOf('_') + 1))});
            } else if(id.startsWith('CONSENT')) {
                $state.go('invite');
            }
        }
    }

    module.exports = progress_controller;
})();