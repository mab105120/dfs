(function(){

    'use strict';

    function likert_directive() {
        return {
            restrict: 'E',
            scope: {
                'items': '=',
                'choices': '='
            },
            templateUrl: 'app/template/likert.html'
        }
    }

    module.exports = likert_directive;
})();