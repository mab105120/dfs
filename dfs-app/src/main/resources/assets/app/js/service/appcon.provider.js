(function() {

    function appcon_provider() {
        var url;

        this.setUrl = function(val) {
            url = val;
        }

        appcon_service.$inject = ['$http'];

        function appcon_service($http) {

            function getReviews(evaluationCode, mode) {
                var config = {
                    method: 'GET',
                    url:  url + '/api/performance-review/' + evaluationCode + '/' + mode
                };
                return $http(config);
            }

            function getExpertEvaluation(payload) {
                var workerId = localStorage.getItem("workerId");
                var config = {
                    method: 'POST',
                    headers: {
                        "workerId": workerId
                    },
                    data: payload,
                    url:  url + '/api/evaluation/expert/absolute'
                };
                return $http(config);
            }

            function postUserDemographic(user) {
                var workerId = localStorage.getItem("workerId");
                return $http({
                    method: 'POST',
                    headers: {
                        'workerId': workerId
                    },
                    data: user,
                    url: url + '/api/questionnaire/user-demographic'
                });
            }

            function stepIsCompleted(step) {
                var workerId = localStorage.getItem("workerId");
                return $http({
                    method: 'GET',
                    headers: {
                        'workerId': workerId
                    },
                    url: url + '/api/status/step-is-completed/' + step
                });
            }

            function getUserDemographics() {
                var workerId = localStorage.getItem("workerId");
                return $http({
                    method: 'GET',
                    headers: {
                        "workerId": workerId
                    },
                    url: url + '/api/questionnaire/user-demographic'
                })
            }

            function postUserExperience(user) {
                var workerId = localStorage.getItem('workerId');
                return $http({
                    method: 'POST',
                    headers: {
                        'workerId': workerId
                    },
                    data: user,
                    url: url + '/api/questionnaire/user-experience'
                });
            }

            function getUserExperience() {
                var workerId = localStorage.getItem('workerId');
                return $http({
                    method: 'GET',
                    headers: {
                        'workerId': workerId
                    },
                    url: url + '/api/questionnaire/user-experience'
                })
            }

            function getUserRecipOrientation() {
                var workerId = localStorage.getItem('workerId');
                return $http({
                    method: 'GET',
                    headers: {
                        'workerId': workerId
                    },
                    url: url + '/api/questionnaire/user-ro'
                });
            }

            function postUserRecipOrientation(payload) {
                var workerId = localStorage.getItem('workerId');
                return $http({
                    method: 'POST',
                    headers: {
                        'workerId': workerId
                    },
                    data: payload,
                    url: url + '/api/questionnaire/user-ro'
                });
            }

            function postUserEvaluation(userEvaluation, mode) {
                var workerId = localStorage.getItem('workerId');;
                var path = mode === 'relative' ? '/api/evaluation/relative' : '/api/evaluation/absolute';
                return $http({
                        method: 'POST',
                        headers: {
                            'workerId': workerId
                        },
                        data: userEvaluation,
                        url: url + path
                });
            }

            function getUserEvaluation(evalCode) {
                var workerId = localStorage.getItem('workerId');
                return $http({
                        method: 'GET',
                        headers: {
                            'workerId': workerId
                        },
                        url: url + '/api/evaluation/absolute/' + evalCode
                });
            }

            function validateUserResponses() {
                var workerId = localStorage.getItem('workerId');
                return $http({
                        method: 'GET',
                        headers: {
                            'workerId': workerId
                        },
                        url: url + '/api/validate'
                });
            }

            function postLogin() {
                var workerId = localStorage.getItem('workerId');
                return $http({
                        method: 'POST',
                        headers: {
                            'workerId': workerId
                        },
                        url: url + '/api/activity/login'
                });
            }

            function postLogout() {
                var workerId = localStorage.getItem('workerId');
                return $http({
                        method: 'POST',
                        headers: {
                            'workerId': workerId
                        },
                        url: url + '/api/activity/logout'
                });
            }

            function getProgress() {
                var workerId = localStorage.getItem('workerId');
                return $http({
                        method: 'GET',
                        headers: {
                            'workerId': workerId
                        },
                        url: url + '/api/status/progress'
                });
            }

            function sendEmail(from, subject, body) {
                var workerId = localStorage.getItem('workerId');
                var supportMailDetails = {
                    from: from,
                    subject: subject,
                    body: body
                }
                return $http({
                        method: 'POST',
                        data: supportMailDetails,
                        headers: {
                            'workerId': workerId
                        },
                        url: url + '/api/communication/send-support-email'
                });
            }

            function getParticipantProfile() {
                var workerId = localStorage.getItem('workerId');
                return $http({
                        method: 'GET',
                        headers: {
                            'workerId': workerId
                        },
                        url: url + '/api/participant-profile/'
                });
            }

            function requestInvite(req) {
                var workerId = localStorage.getItem('workerId');
                return $http({
                    method: 'POST',
                    headers: {
                        'workerId': workerId
                    },
                    data: req,
                    url: url + '/api/status/request-invite'
                });
            }

            function getInvitationStatus() {
                var workerId = localStorage.getItem('workerId');
                return $http({
                    method: 'GET',
                    headers: {
                        'workerId': workerId
                    },
                    url: url + '/api/status/invitation-status'
                })
            }

            function recordGroupAttentionCheck(payload) {
                var workerId = localStorage.getItem('workerId');
                return $http({
                    method: 'POST',
                    headers: {
                        'workerId': workerId
                    },
                    data: payload,
                    url: url + '/api/group-att-check'
                });
            }

            function groupAttentionCheckComplete() {
                var workerId = localStorage.getItem('workerId');
                return $http({
                    method: 'GET',
                    headers: {
                        'workerId': workerId
                    },
                    url: url + '/api/group-att-check/is-complete'
                });
            }

            return {
                getReviews: getReviews,
                getExpertEvaluation: getExpertEvaluation,
                getParticipantProfile: getParticipantProfile,
                postUserDemographic: postUserDemographic,
                getUserDemographics: getUserDemographics,
                postUserExperience: postUserExperience,
                postUserRecipOrientation: postUserRecipOrientation,
                getUserRecipOrientation: getUserRecipOrientation,
                getUserExperience: getUserExperience,
                postUserEvaluation: postUserEvaluation,
                getUserEvaluation: getUserEvaluation,
                validateUserResponses: validateUserResponses,
                stepIsCompleted: stepIsCompleted,
                postLogin: postLogin,
                postLogout: postLogout,
                getProgress: getProgress,
                sendEmail: sendEmail,
                requestInvite: requestInvite,
                getInvitationStatus: getInvitationStatus,
                recordGroupAttentionCheck: recordGroupAttentionCheck,
                groupAttentionCheckComplete: groupAttentionCheckComplete
            }
        };

        this.$get = appcon_service;
    }

    module.exports = appcon_provider;
})();