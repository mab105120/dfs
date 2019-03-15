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
                var id_token = localStorage.getItem("id_token");
                var config = {
                    method: 'POST',
                    headers: {
                        "Authorization": 'Bearer ' + id_token
                    },
                    data: payload,
                    url:  url + '/api/evaluation/expert/absolute'
                };
                return $http(config);
            }

            function postUserDemographic(user) {
                var id_token = localStorage.getItem("id_token");
                return $http({
                    method: 'POST',
                    headers: {
                        "Authorization": 'Bearer ' + id_token
                    },
                    data: user,
                    url: url + '/api/questionnaire/user-demographic'
                });
            }

            function stepIsCompleted(step) {
                var id_token = localStorage.getItem("id_token");
                return $http({
                    method: 'GET',
                    headers: {
                        "Authorization": 'Bearer ' + id_token
                    },
                    url: url + '/api/status/step-is-completed/' + step
                });
            }

            function getUserDemographics() {
                var id_token = localStorage.getItem("id_token");
                return $http({
                    method: 'GET',
                    headers: {
                        "Authorization": 'Bearer ' + id_token
                    },
                    url: url + '/api/questionnaire/user-demographic'
                })
            }

            function postUserExperience(user) {
                var id_token = localStorage.getItem('id_token');
                return $http({
                    method: 'POST',
                    headers: {
                        'Authorization': 'Bearer ' + id_token
                    },
                    data: user,
                    url: url + '/api/questionnaire/user-experience'
                });
            }

            function getUserExperience() {
                var id_token = localStorage.getItem("id_token");
                return $http({
                    method: 'GET',
                    headers: {
                        "Authorization": 'Bearer ' + id_token
                    },
                    url: url + '/api/questionnaire/user-experience'
                })
            }

            function getUserConfidence() {
                var id_token = localStorage.getItem("id_token");
                return $http({
                    method: 'GET',
                    headers: {
                        "Authorization": 'Bearer ' + id_token
                    },
                    url: url + '/api/questionnaire/user-confidence'
                })
            }

            function postUserConfidence(payload) {
                var id_token = localStorage.getItem('id_token');
                return $http({
                        method: 'POST',
                        headers: {
                            'Authorization': 'Bearer ' + id_token
                        },
                        data: payload,
                        url: url + '/api/questionnaire/user-confidence'
                });
            }

            function postUserEvaluation(userEvaluation, mode) {
                var id_token = localStorage.getItem('id_token');
                var path = mode === 'relative' ? '/api/evaluation/relative' : '/api/evaluation/absolute';
                return $http({
                        method: 'POST',
                        headers: {
                            'Authorization': 'Bearer ' + id_token
                        },
                        data: userEvaluation,
                        url: url + path
                });
            }

            function getUserEvaluation(evalCode, mode) {
                var id_token = localStorage.getItem('id_token');
                var mode = mode === 'relative' ? 'relative' : 'absolute';
                return $http({
                        method: 'GET',
                        headers: {
                            'Authorization': 'Bearer ' + id_token
                        },
                        url: url + '/api/evaluation/' + mode + '/' + evalCode
                });
            }

            function validateUserResponses() {
                var id_token = localStorage.getItem('id_token');
                return $http({
                        method: 'GET',
                        headers: {
                            'Authorization': 'Bearer ' + id_token
                        },
                        url: url + '/api/validate'
                });
            }

            function postLogin() {
                var id_token = localStorage.getItem('id_token');
                return $http({
                        method: 'POST',
                        headers: {
                            'Authorization': 'Bearer ' + id_token
                        },
                        url: url + '/api/activity/login'
                });
            }

            function postLogout() {
                var id_token = localStorage.getItem('id_token');
                return $http({
                        method: 'POST',
                        headers: {
                            'Authorization': 'Bearer ' + id_token
                        },
                        url: url + '/api/activity/logout'
                });
            }

            function getProgress() {
                var id_token = localStorage.getItem('id_token');
                return $http({
                        method: 'GET',
                        headers: {
                            'Authorization': 'Bearer ' + id_token
                        },
                        url: url + '/api/status/progress'
                });
            }

            function sendEmail(from, subject, body) {
                var id_token = localStorage.getItem('id_token');
                var supportMailDetails = {
                    from: from,
                    subject: subject,
                    body: body
                }
                return $http({
                        method: 'POST',
                        data: supportMailDetails,
                        headers: {
                            'Authorization': 'Bearer ' + id_token
                        },
                        url: url + '/api/communication/send-support-email'
                });
            }

            function getParticipantProfile() {
                var id_token = localStorage.getItem('id_token');
                return $http({
                        method: 'GET',
                        headers: {
                            'Authorization': 'Bearer ' + id_token
                        },
                        url: url + '/api/participant-profile/'
                });
            }

            return {
                getReviews: getReviews,
                getExpertEvaluation: getExpertEvaluation,
                getParticipantProfile: getParticipantProfile,
                postUserDemographic: postUserDemographic,
                getUserDemographics: getUserDemographics,
                postUserExperience: postUserExperience,
                postUserConfidence: postUserConfidence,
                getUserConfidence: getUserConfidence,
                getUserExperience: getUserExperience,
                postUserEvaluation: postUserEvaluation,
                getUserEvaluation: getUserEvaluation,
                validateUserResponses: validateUserResponses,
                stepIsCompleted: stepIsCompleted,
                postLogin: postLogin,
                postLogout: postLogout,
                getProgress: getProgress,
                sendEmail: sendEmail
            }
        };

        this.$get = appcon_service;
    }

    module.exports = appcon_provider;
})();