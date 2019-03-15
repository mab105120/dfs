(function() {

    'use-strict';

    authService.$inject = [
        '$state',
        'angularAuth0',
        '$http',
        'appcon'
    ]

    function authService($state, angularAuth0, $http, appcon) {

        function login() {
            // remember current state to reroute to after authentication
            if ($state.current.name === 'welcome')
                localStorage.setItem('redirect_state', 'home');
            else {
                var state = $state.current.name;
                if(state === 'evaluation') {
                    localStorage.setItem('redirect_state_param', $state.params.id);
                }
                localStorage.setItem('redirect_state', state);
            }

            angularAuth0.authorize();
        }

        function handleAuthentication() {
            angularAuth0.parseHash(function(err, authResult) {
               if(authResult && authResult.accessToken && authResult.idToken) {
                    setSession(authResult);
                    if (localStorage.getItem('redirect_state') === null)
                        $state.go('home');
                    else {
                        var redirect_state = localStorage.getItem('redirect_state');
                        if(redirect_state === 'evaluation') {
                            var redirect_state_id = localStorage.getItem('redirect_state_param');
                            $state.go('evaluation', {id: redirect_state_id});
                        } else
                            $state.go(localStorage.getItem('redirect_state'));
                        localStorage.removeItem('redirect_state');
                    }

                    appcon.postLogin()
                    .then(function success(response){
                        localStorage.setItem('userId', response.data);
                        console.log('User login recorded successfully');
                    }, function failure(response) {
                        console.log('Unable to record user login!');
                    });

               } else if (err) {
                    alert('An error occurred while trying to parse the URL has. Please see console for more details!');
                    console.log('error details: ');
                    console.log(err);
               }
            });
        }

        function setSession(authResult) {
            console.log('Setting authentication result to local storage');
            var expiresAt = JSON.stringify((authResult.expiresIn * 1000) + new Date().getTime());
            localStorage.setItem('access_token', authResult.accessToken);
            localStorage.setItem('id_token', authResult.idToken);
            localStorage.setItem('expires_at', expiresAt);
        }

        function logout() {
            console.log('user is being logged out!');
            localStorage.clear();
            $state.go('welcome');
        }


        function getUserId() {
            return localStorage.getItem('userId');
        }

        function isAuthenticated() {
            var expiresAt = JSON.parse(localStorage.getItem('expires_at'));
            return new Date().getTime() < expiresAt;
        }

        return {
            login: login,
            handleAuthentication: handleAuthentication,
            logout: logout,
            isAuthenticated: isAuthenticated,
            getUserId: getUserId
        }
    };

    module.exports = authService;
})();