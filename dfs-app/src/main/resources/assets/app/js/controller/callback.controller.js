(function() {

    'use-strict';

    callback_controller.$inject = [
            '$scope',
            '$state',
            'toaster',
            'appcon'
        ]

    function callback_controller($scope, $state, toaster, appcon) {
        console.log('This is not running!');
        // implement controller
        if(localStorage.getItem('redirect_state') === null)
            $state.go('home');
        else {
            $state.go(localStorage.getItem('redirect_state'));
            localStorage.removeItem('redirect_url');
        }
    };

    module.exports = callback_controller;
})();