(function() {

    'use-strict';

    scoringMatrixController.$inject = [
        '$scope',
        '$state',
        'authService',
        '$window'
    ];

    function scoringMatrixController($scope, $state, authService, $window) {
        $scope.slExpandLabel = 'More';
        $scope.ipExpandLabel = 'More';
        $scope.pfExpandLabel = 'More';

        $scope.slRead = false;
        $scope.ipRead = false;
        $scope.pfRead = false;

        $scope.collapseClick = function(job_function) {
            switch(job_function) {
                case 'sl':
                    $scope.slExpandLabel = $scope.slExpandLabel === 'More' ? 'Less' : 'More';
                    $scope.slRead = true;
                    break;
                case 'ip':
                    $scope.ipExpandLabel = $scope.ipExpandLabel === 'More' ? 'Less' : 'More';
                    $scope.ipRead = true;
                    break;
                case 'pf':
                    $scope.pfExpandLabel = $scope.pfExpandLabel === 'More' ? 'Less' : 'More';
                    $scope.pfRead = true;
                    break;

            }
        }
        $scope.pendingConfirmation = true;
        $scope.confirm = function() {
            console.log('before change: ' + $scope.pendingConfirmation);
            $scope.pendingConfirmation = !$scope.pendingConfirmation;
            console.log('after change: ' + $scope.pendingConfirmation);
        }

        $scope.submit = function() {
            if(!authService.isAuthenticated()) {
                toaster.pop('error', 'Error', 'You need to be logged in to perform this operation!');
                return;
            }
            if ($scope.slRead === false || $scope.ipRead === false || $scope.pfRead === false) {
                alert('You must read scoring instructions for all job dimensions. Please make sure to click the More button next to each job dimension.');
                return;
            }
            $window.scrollTo(0, 0);
            $state.go('evaluation', {id : 'P1'});
        }
    }

    module.exports = scoringMatrixController;

})();