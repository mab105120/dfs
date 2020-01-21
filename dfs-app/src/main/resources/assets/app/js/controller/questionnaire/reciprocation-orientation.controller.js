(function(){

    reciprocation_orientation_controller.$inject = [
        '$scope', 'authService', 'appcon', 'toaster', '$window', '$state'
    ];

    function reciprocation_orientation_controller($scope, authService, appcon, toaster, $window, $state) {
        init();

        function init() {
            $scope.choices = require('./seven-likert-scale.js');
            $scope.items = require('./ro-items.js');

            $scope.$parent.startSpinner();

            if(!authService.isAuthenticated()) {
                alert('You are not logged in. You need to log in to view this page.');
                authService.login();
            }

            appcon.stepIsCompleted('QUEST_RO')
            .then(function success(response) {
                if(response.data === true) {
                    appcon.getUserRecipOrientation()
                    .then(function success(response) {
                        $scope.oldRes = response.data;
                        angular.forEach(response.data, function(item) {
                            angular.forEach($scope.items, function(element) {
                                if(element.code === item.itemCode) element.answer = item.response;
                            });
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

        function getUserResponsesForApiCall() {
            var userResponses = [];

            function formatItems(item) {
                userResponses.push({
                    itemCode: item.code,
                    response: item.answer
                });
            }
            angular.forEach($scope.items, formatItems);
            return userResponses;
        }

        $scope.submit = function() {
            if(!authService.isAuthenticated()) {
                toaster.pop('error', 'Error', 'You need to be logged in to perform this operation!');
                return;
            }
            var userROResponse = getUserResponsesForApiCall();
            if(responseChanged($scope.oldRes, userROResponse)) {

                $scope.$parent.startSpinner();
                appcon.postUserRecipOrientation(userROResponse)
                .then(function success(response) {
                    toaster.pop('success', 'Saved!', 'Your response has been saved');
                    $scope.$parent.stopSpinner();
                    $window.scrollTo(0, 0);
                    $state.go('scoring-matrix');
                }, function failure(response) {
                    var error = response.data === null ? 'Server unreachable!' : response.data.message;
                    toaster.pop('error', 'Error', "Sorry we weren't able to save your response. Reason: " + error);
                    $scope.$parent.stopSpinner();
                });

            } else {
                $window.scrollTo(0, 0);
                $state.go('scoring-matrix');
            }
        }

        function responseChanged(oldRes, newRes) {
            if(oldRes === undefined)
                return true;
            var oldResMap = new Map();
            angular.forEach(oldRes, function(item) {
                oldResMap.set(item.itemCode, item);
            });
            for(var i = 0; i < $scope.items.length; i++) {
                var item = $scope.items[i];
                if(oldResMap.get(item.code).response !== item.answer)
                    return true;
            }
            return false;
        }

    }

    module.exports = reciprocation_orientation_controller;

})();