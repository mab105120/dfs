(function() {

    'use strict';

   function focusonshow_directive() {
            console.log('Registering directive');
           return {
               restrict: 'A',
               link: function($scope, $element, $attr) {
                    console.log('focusOnShow directive fired.');
                   if ($attr.ngShow){
                       $scope.$watch($attr.ngShow, function(newValue){
                           if(newValue){ // scroll up to the alert div only when the div appears
                               $('html, body').animate({ scrollTop: $('#alert').offset().top }, 'slow');
                           }
                       });
                   }
               }
           }
       }

    module.exports = focusonshow_directive;
})();