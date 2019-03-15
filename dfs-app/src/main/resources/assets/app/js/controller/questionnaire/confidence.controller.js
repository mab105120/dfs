(function() {

    quest_confidence_controller.$inject = [
        '$scope',
        '$state',
        'authService',
        'toaster',
        'appcon',
        '$window'
    ];

    function quest_confidence_controller($scope, $state, authService, toaster, appcon, $window) {

        function init() {

            $scope.$parent.startSpinner();

            if(!authService.isAuthenticated()) {
                alert('You are not logged in. You need to log in to view this page.');
                authService.login();
            }

            appcon.stepIsCompleted('QUEST_CON')
            .then(function success(response) {
                if(response.data === true) {
                    appcon.getUserConfidence()
                    .then(function success(response) {
                        $scope.oldRes = response.data;
                        angular.forEach(response.data, function(item) {
                            if(item.itemCode.startsWith("jsd")) {
                                angular.forEach($scope.jsdItems, function(element) {
                                    if(element.code === item.itemCode) element.answer = item.response;
                                });
                            } else if(item.itemCode.startsWith("pfi")) {
                                angular.forEach($scope.pfiItems, function(element) {
                                    if(element.code === item.itemCode) element.answer = item.response;
                                });
                            }
                        });
                        // enable submit
                        $('#submitBtn').prop('disabled', false);
                        $scope.$parent.stopSpinner();
                    }, function failure(response) {
                        var error = response.data === null ? 'Server unreachable' : response.data.message;
                        toaster.pop('error', 'Error', 'Oops! we are having a bit of trouble! Details: ' + error);
                        $scope.$parent.stopSpinner();
                    });
                }
                else
                    $scope.$parent.stopSpinner();
            }, function failure(response) {
                var error = response.data === null ? 'Server unreachable' : response.data.message;
                toaster.pop('error', 'Error', 'Oops! we are having a bit of trouble! Details: ' + error);
                $scope.$parent.stopSpinner();
            });
        }

         init();


        $scope.choices = [
            {
                choice: 'Strongly Disagree',
                value: -3
            },
            {
                choice: 'Disagree',
                value: -2
            },
            {
                choice: 'Somewhat Disagree',
                value: -1
            },
            {
                choice: 'Somewhat Agree',
                value: 1
            },
            {
                choice: 'Agree',
                value: 2
            },
            {
                choice: 'Strongly Agree',
                value: 3
            }
        ];
        $scope.jsdItems = [
            {
                item: 'Often I put off making difficult decisions',
                code: 'jsd1'
            },
            {
                item: "I often don't trust myself to make the right decision",
                code: 'jsd2'
            },
            {
                item: "I often trust the judgment of others more than my own",
                code: 'jsd3'
            },
            {
                item: "In almost all situations I am confident of my ability to make the right choices",
                code: 'jsd4'
            },
            {
                item: "Frequently, I doubt my ability to make sound judgments",
                code: 'jsd5'
            },
            {
                item: "I often worry about whether a decision I made will have bad consequences",
                code: 'jsd6'
            },
            {
                item: "When making a decision, I often feel confused because I have trouble keeping all relevant factors in mind",
                code: 'jsd7'
            },
            {
                item: "I have a great deal of confidence in my opinions",
                code: 'jsd8'
            },
            {
                item: "I often trust myself to make the right decision",
                code: 'jsd9'
            }
        ];
        $scope.pfiItems = [
            {
                item: 'I tend to struggle with most decisions',
                code: 'pfi1'
            },
            {
                item: 'Even after making an important decision I tend to continue to think about the pros and cons to make sure that I am not wrong',
                code: 'pfi2'
            },
            {
                item: 'I rarely doubt that the course of action I have selected be correct',
                code: 'pfi3'
            },
            {
                item: 'I tend to continue to evaluate recently made decisions',
                code: 'pfi4'
            },
            {
                item: 'Decisions rarely weigh heavily on my shoulders',
                code: 'pfi5'
            },
        ];

        function getUserResponsesForApiCall() {
            var userResponses = [];

            function formatItems(item) {
                userResponses.push({
                    itemCode: item.code,
                    response: item.answer
                });
            }

            angular.forEach($scope.jsdItems, formatItems);
            angular.forEach($scope.pfiItems, formatItems);

            return userResponses;
        }

        $scope.submit = function() {
            if(!authService.isAuthenticated()) {
                toaster.pop('error', 'Error', 'You need to be logged in to perform this operation!');
                return;
            }

            if(responseChanged) {
                var userConfidenceResponse = getUserResponsesForApiCall();

                $scope.$parent.startSpinner();
                appcon.postUserConfidence(userConfidenceResponse)
                .then(function success(response) {
                    toaster.pop('success', 'Saved!', 'Your response has been saved');
                    console.log('POST /api/questionnaire/confidence ' + response.status);
                    $scope.$parent.stopSpinner();
                    $window.scrollTo(0, 0);
                    $state.go('evaluation', {id: 1});
                }, function failure(response) {
                    var error = response.data === null ? 'Server unreachable!' : response.data.message;
                    toaster.pop('error', 'Error', "Sorry we weren't able to save your response. Reason: " + error);
                    $scope.$parent.stopSpinner();
                });

            } else {
                $window.scrollTo(0, 0);
                $state.go('evaluation', {id: 1});
            }
        }
    }

    function responseChanged(oldRes, newRes) {
        if(oldRes === undefined)
            return true;
        var oldResMap = new Map();
        angular.forEach(oldRes, function(item) {
            oldResMap.set(item.code, item);
        });
        angular.forEach($scope.jsdItems, function(item) {
            if(oldResMap.get(item.code).response != item.answer) return true;
        });
        angular.forEach($scope.pfiItems, function(item) {
            if(oldResMap.get(item.code).response != item.answer) return true;
        });
        return false;
    }

    module.exports = quest_confidence_controller;
})();