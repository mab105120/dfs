(function() {

    'use-strict';

    profile_service.$inject = [
        'appcon'
    ]

    function profile_service(appcon, $scope) {

        var promise; // save result of first call so that it is made only once

        function getProfile() {
            if( !promise || promise.$$state.status !== 1 ) {
                promise = appcon.getParticipantProfile();
            }
            return promise;
        }

        return {
            getProfile: getProfile
        }
    }

    module.exports = profile_service;

})();