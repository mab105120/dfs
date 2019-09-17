(function() {

    welcome_controller.$inject = [
        '$scope',
        'authService',
        '$state',
        '$stateParams'
    ];

    function welcome_controller($scope, authService, $state, $stateParams) {
        $scope.showEndOfSessionMessage = false;
        $scope.showStartMessage = false;
        $scope.showAccessDeniedMessage = false;
        if ($stateParams.endSession === "true") {
            $scope.showEndOfSessionMessage = true;
        } else {
            var isAuthenticated = authService.handleAuthentication();
            if (isAuthenticated) {
                $scope.showStartMessage = true;
            } else {
                $scope.showAccessDeniedMessage = true;
            }
        }

        $scope.go = function go() {
            $state.go('home');
        }
    };

    module.exports = welcome_controller;

})();