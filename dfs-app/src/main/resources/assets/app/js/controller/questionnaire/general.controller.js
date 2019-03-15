(function() {

    'use strict';

    questionnaire_controller.$inject = [
            '$scope',
            '$state',
            'appcon',
            'authService',
            'toaster'
        ];

    function questionnaire_controller($scope, $state, appcon, authService, toaster) {

        function init() {
            $scope.$parent.startSpinner();

            if(!authService.isAuthenticated()) {
                alert('You are not logged in. You need to log in to view this page.');
                authService.login();
            }

            appcon.stepIsCompleted('QUEST_DEMO')
            .then(function success(response) {
                console.log(response);
                if(response.data === true) {
                    appcon.getUserDemographics()
                    .then(function success(response) {
                        var userDemographics = response.data;
                        $scope.oldResponse = userDemographics;
                        // populate the fields
                        $scope.age = parseInt(userDemographics.age);
                        $scope.gender = userDemographics.gender;
                        $scope.education = userDemographics.education;
                        $scope.division = userDemographics.division;
                        $scope.educationExperience = userDemographics.educationExperience;

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

        $scope.ageGroup = [
            '[20-30]',
            '[30-40]',
            '[40-50]',
            '[50-60]',
            '60+'
        ];

        $scope.genderGroup = ['Male', 'Female'];

        $scope.eductionGroup = [
            'No degree',
            'Associate degree',
            'Bachelor degree',
            'Master degree',
            'Doctorate degree'
        ];

        $scope.educationExperienceGroup = [
            'Very good',
            'Good',
            'Neutral',
            'Bad',
            'Very bad'
        ]

        $scope.divisionGroup = [
            'Healthcare Practitioners and Technical Occupations',
            'Management Occupations',
            'Legal Occupations',
            'Business and Financial Operations Occupations',
            'Healthcare Support Occupations',
            'Architecture and Engineering Occupations',
            'Arts, Design, Entertainment, Sports, and Media Occupations',
            'Building and Grounds Cleaning and Maintenance Occupations',
            'Community and Social Service Occupations',
            'Construction and Extraction Occupations',
            'Protective Service Occupations',
            'Production Occupations',
            'Education, Training, and Library Occupations',
            'Farming, Fishing, and Forestry Occupations',
            'Personal Care and Service Occupations',
            'Office and Administrative Support Occupations',
            'Installation, Maintenance, and Repair Occupations',
            'Computer and Mathematical Occupations',
            'Life, Physical, and Social Science Occupations',
            'Food Preparation and Serving Related Occupations',
            'Sales and Related Occupations',
            'Transportation and Materials Moving Occupations',
            'Other'
        ];

        $scope.submit = function() {
            if(!authService.isAuthenticated()) {
                toaster.pop('error','Authentication Issue','You must be logged in to perform this action');
                return;
            }
            var payload = {
                age: $scope.age,
                gender: $scope.gender,
                education: $scope.education,
                division: $scope.division,
                educationExperience: $scope.educationExperience
            };

            if(responseChanged($scope.oldResponse, payload)) {
                $scope.$parent.startSpinner();
                appcon.postUserDemographic(payload)
                .then(function success(response) {
                    $scope.$parent.stopSpinner();
                    toaster.pop('success','Saved!','Your response has been saved successfully!');
                    $state.go('experience');
                }, function failure(response) {
                    var errorMessage = (response.data === null) ? 'Server unreachable' : response.data.message;
                    toaster.pop('error','Error', "Sorry we were unable to save your response. Reason (" + response.status + "): " + errorMessage);
                    $scope.$parent.stopSpinner();
                });
            } else {
                $state.go('experience');
            }

        }

        function responseChanged(oldRes, newRes) {
            if(oldRes === undefined)
                return true;
            else return oldRes.age != newRes.age ||
                        oldRes.gender !== newRes.gender ||
                        oldRes.education !== newRes.education ||
                        oldRes.division !== newRes.division ||
                        oldRes.educationExperience !== newRes.educationExperience;
        }

    }

    module.exports = questionnaire_controller;
})();