(function() {

    'use-strict';

    authService.$inject = [
        '$state',
        'angularAuth0',
        '$http',
        'appcon'
    ]

    function authService($state, angularAuth0, $http, appcon) {

        function handleAuthentication() {
            var mid = getParameterByName('workerId');
            if (mid === null) {
                return false;
           } else {
                localStorage.setItem('workerId', mid);
                return true;
            }
        }

        function getParameterByName(name, url) {
            if (!url) url = window.location.href;
            name = name.replace(/[\[\]]/g, '\\$&');
            var regex = new RegExp('[?&]' + name + '(=([^&#]*)|&|#|$)'),
                results = regex.exec(url);
            if (!results) return null;
            if (!results[2]) return '';
            return decodeURIComponent(results[2].replace(/\+/g, ' '));
        }

        function getUserId() {
            return localStorage.getItem('workerId');
        }

        function isAuthenticated() {
            return getUserId() !== null;
        }

        function endSession() {
            localStorage.removeItem('workerId');
            $state.go('welcome', { endSession: true });
        }

        return {
            handleAuthentication: handleAuthentication,
            isAuthenticated: isAuthenticated,
            endSession: endSession,
            getUserId: getUserId
        }
    };

    module.exports = authService;
})();