(function() {

    'use strict';

    function navbar_directive() {
        return {
                templateUrl: 'app/template/navbar.html',
                controller: navbar_controller,
                controllerAs: 'vm'
        };
    }

    navbar_controller.$inject = ['$scope', 'authService', 'appcon'];

    function navbar_controller($scope, authService, appcon) {
        var vm = this; // why do this ?
        vm.auth = authService;

        $scope.login = function() {
            authService.login();
        }

        $scope.logout = function() {
            $scope.startSpinner();
            appcon.postLogout()
            .then(function success(response) {
                console.log('User successfully logged out!');
                authService.logout();
                $scope.stopSpinner();
            }, function failure(response) {
                $scope.stopSpinner();
            });
        }
    };

    module.exports = navbar_directive;
})();