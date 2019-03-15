(function() {

    welcome_controller.$inject = [
        '$scope',
        'authService'
    ];

    function welcome_controller($scope, authService) {
        $scope.login = function() {
            authService.login();
        }
    };

    module.exports = welcome_controller;

})();