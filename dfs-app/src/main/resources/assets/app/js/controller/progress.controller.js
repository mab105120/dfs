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
            $scope.isExpert = $scope.profile.mode === 'EXPERT';
            init();
        }, handleFailure);

        function init() {

            if(!authService.isAuthenticated()) {
                alert('You are not logged in. You need to log in to view this page.');
                authService.login();
            }

            $scope.qual_redirect_url = localStorage.getItem('qual_redirect_url');

            $scope.showSubmit = false;

            $scope.rows = [];

            if($scope.profile.mode !== 'EXPERT') {
                $scope.rows.push(
                    {
                        id: 'QUEST_DEMO',
                        display: 'General questionnaire'
                    },
                    {
                        id: 'QUEST_EXP',
                        display: 'Professional experience questionnaire'
                    }
                );
            }

            if ($scope.profile.includeConfidenceScale)
                $scope.rows.push({
                     id: 'QUEST_CON',
                     display: 'Judgment confidence questionnaire'
             });

            function addPracticeEvaluationsToRows() {
                var totalPracticeEvaluations = $scope.profile.practice;
                if(totalPracticeEvaluations == 0) return;
                for(var i = 1; i <= totalPracticeEvaluations; i++) {
                    $scope.rows.push({
                        id: 'EVALUATION_P' + i,
                        display: 'Teacher Evaluation (Practice) ' + i + ' / ' + totalPracticeEvaluations
                    })
                }
            }

            function addEvaluationsToRows() {
                var totalEvaluations = $scope.profile.evaluations;
                for(i = 1; i <= totalEvaluations; i++) {
                    $scope.rows.push(
                        {
                            id: 'EVALUATION_' + i,
                            display: 'Teacher Evaluation ' + i + ' / ' + totalEvaluations
                        }
                    );
                }
            }

            addPracticeEvaluationsToRows();
            addEvaluationsToRows();

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
                $scope.showSubmit = allCompleted;

                if(allCompleted) {
                    var attention = localStorage.getItem('attentionCheck');
                    if(attention !== undefined && attention === 'false')
                        $scope.qual_redirect_url += '&term=attention';
                }
                $scope.$parent.stopSpinner();
            }, handleFailure);
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
            }
        }
    }

    module.exports = progress_controller;
})();