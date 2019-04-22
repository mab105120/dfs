(function() {

    evaluation_controller.$inject = [
            '$scope',
            '$state',
            '$stateParams',
            '$window',
            'appcon',
            'authService',
            'toaster',
            '$sce',
            'profileService',
            '$interval'
        ];

    require('bootstrap-slider');

    var _ = require('lodash');

    function evaluation_controller($scope, $state, $stateParams, $window, appcon, authService, toaster, $sce, profileService, $interval) {
        $scope.$parent.startSpinner();

        profileService.getProfile().then(
            function(response) {
                $scope.profile = response.data;
                init();
            }, handleFailure
        );

        function init() {
            if(!authService.isAuthenticated()) {
                alert('You are not logged in. You need to log in to view this page.');
                authService.login();
            }

            $scope.unreadReviews = new Map();
            $scope.unreadReviews.set('SL-SP1', "Student Learning: Supervisor 1");
            $scope.unreadReviews.set('SL-SP2', "Student Learning: Supervisor 2");
            $scope.unreadReviews.set('IP-SP1', "Instructional Practice: Supervisor 1");
            $scope.unreadReviews.set('IP-SP2', "Instructional Practice: Supervisor 2");
            $scope.unreadReviews.set('PF-SP1', "Professionalism: Supervisor 1");
            $scope.unreadReviews.set('PF-SP2', "Professionalism: Supervisor 2");

            $scope.showReviewModal = function(jobFunction, supervisor) {
                var code = jobFunction + '-' + supervisor;
                $scope.reviewModal = {};
                var reviewModal = $scope.reviewModal;
                reviewModal.closeBtnDisabled = true;
                reviewModal.modelCloseDuration = 10;
                reviewModal.showCountDownLabel = false;
                $('#reviewModal').modal({backdrop: 'static', keyboard: false});
                if(!$scope.isFamiliar && $scope.unreadReviews.get(code) !== undefined) {
                    reviewModal.showCountDownLabel = true;
                    $interval(function() {
                        if(reviewModal.modelCloseDuration == 0)
                            reviewModal.closeBtnDisabled = false;
                        else
                            reviewModal.modelCloseDuration--;
                    }, 1000, 11);
                }
                else
                    reviewModal.closeBtnDisabled = false;

                $scope.unreadReviews.delete(code);
            }

            $scope.waitDuration = 75; // seconds before participants can proceed with evaluation

            $scope.showAttentionCheck = false;
            $scope.attentionCheckCode = 'BXFJ98xStH';
            if($stateParams.id === 'P2') {
                $scope.showAttentionCheck = true;
                $('#attentionCheck').bind('paste', function(e) {
                    e.preventDefault();
                });
            }

            initializeCurrentAndTotalEvaluationVars();

            $scope.isPractice = false;
            if($stateParams.id.toLowerCase().startsWith('p'))
                $scope.isPractice = true;

            paintPage();

            appcon.stepIsCompleted('EVALUATION_' + $stateParams.id)
            .then(function success(response) {
                if(response.data === true) {
                    // User has already completed evaluation step.
                    appcon.getUserEvaluation($stateParams.id)
                    .then(function success(response) {
                        $scope.isFamiliar = true;
                        // pre-populate fields
                        $scope.oldRes = response.data;

                        $scope.studentLearningRating = response.data.studentLearning;
                        $('#studentLearningSlider').slider().slider('setValue', response.data.studentLearning);
                        $scope.instructionalPracticeRating = response.data.instructionalPractice;
                        $('#instructionalPracticeSlider').slider().slider('setValue', response.data.instructionalPractice);
                        $scope.professionalismRating = response.data.professionalism;
                        $('#professionalismSlider').slider().slider('setValue', response.data.professionalism);
                        $scope.overallRating = response.data.overall;
                        $('#overallSlider').slider().slider('setValue', response.data.overall);
                        $scope.promotionDecision = response.data.promote;
                        if(!$scope.isExpert && !$scope.isPractice)
                            if(response.data.promote.toUpperCase() === 'Y')
                                $('#promoteYes').prop('checked', true);
                            else $('#promoteNo').prop('checked', true);

                        $scope.getTeacherPerformanceReviews();
                    }, handleFailure);
                }
                else {
                    // User completing evaluation step for first time
                    $scope.countdown = 0;
                    $scope.showTimer=true;
                    $interval(function() {
                        $scope.countdown++;
                        if($scope.countdown >= $scope.waitDuration) $scope.countdown += '+';
                    }, 1000, parseInt($scope.waitDuration));
                    $scope.getTeacherPerformanceReviews();
                }
            }, function failure(response) {
                handleFailure(response);
            });


            function paintPage() {
                $scope.teacherOneLabel = 'Teacher';
                $scope.setClass = function() {
                   return ['col-xs-10', 'col-xs-offset-2', 'text-center'];
                }

                setUpProgressBar();
                setUpSliders();

                $scope.time_in = new Date().toISOString();

                // these vars are set by the eval directive when users click on supervisor reviews.
                $scope.modalCode = '';
                $scope.modalBodyTrusted = $sce.trustAsHtml($scope.modalBody);

                $scope.$watch('modalBody', function(val) {
                    $scope.modalBodyTrusted = $sce.trustAsHtml(val);
                });

                $scope.modalTitle = '';
                // evaluation activity
                $scope.activities = [];
                $scope.time_modal_open;
                $('#reviewModal').on('hidden.bs.modal', function() {
                    var closeTime = new Date().toISOString();
                    $scope.activities.push({
                        evaluationCode: $stateParams.id,
                        selectedReview: $scope.modalCode,
                        openTime: $scope.time_modal_open,
                        closeTime: closeTime
                    });
                });
            }

            function setUpSliders() {
                $('#studentLearningSlider').slider();
                $('#studentLearningSlider').on('change', function(slideEvt) {
                    $scope.studentLearningRating = slideEvt.value.newValue;
                });

                $('#instructionalPracticeSlider').slider();
                $('#instructionalPracticeSlider').on('change', function(slideEvt) {
                    $scope.instructionalPracticeRating = slideEvt.value.newValue;
                });

                $('#professionalismSlider').slider();
                $('#professionalismSlider').on('change', function(slideEvt) {
                    $scope.professionalismRating = slideEvt.value.newValue;
                });

                $('#overallSlider').slider();
                $('#overallSlider').on('change', function(slideEvt) {
                    $scope.overallRating = slideEvt.value.newValue;
                });
            }

            function setUpProgressBar() {
                var progressPercentage = ( $scope.currentEvaluation / $scope.totalEvaluations ) * 100;
                $scope.progressBarStyle = {
                    'width': progressPercentage + '%'
                };
                if ($scope.isPractice)
                    $scope.progressBarStyle['background-color'] = 'green';
            }

            function initializeCurrentAndTotalEvaluationVars() {
                if($stateParams.id.toLowerCase().startsWith('p')) {
                    $scope.currentEvaluation = $stateParams.id.substr(1);
                    $scope.totalEvaluations = $scope.profile.practice;
                    $scope.teacherEvaluationLabel = '(PRACTICE) Teacher Evaluation';
                    $('#evaluationProgressLabel').css('color', 'green');
                } else {
                    $scope.currentEvaluation = $stateParams.id;
                    $scope.totalEvaluations = $scope.profile.evaluations;
                    $scope.teacherEvaluationLabel = 'Teacher Evaluation';
                }
            }
        }

        $scope.getTeacherPerformanceReviews = function() {
            appcon.getReviews($stateParams.id, $scope.profile.mode).then(
                function(response) {
                    console.log('GET /api/performance-review/ ' + response.status);
                    $scope.evaluations = response.data;
                    $scope.$parent.stopSpinner();
                }, handleFailure);
        }

        $scope.saveAndContinue = function() {
            $scope.$parent.startSpinner();
            var checkQuality = true;
            if(!authService.isAuthenticated()) {
                toaster.pop('error', 'Error', 'You have to be logged in to perform this operation');
                $scope.$parent.stopSpinner();
                return;
            }

            // ensure all form required fields are filled (sliders don't work well with angular form validation)
            if(!formIsValid()) {
                $scope.$parent.stopSpinner();
                return;
            }

            // make sure participants are not just providing random answers
            if(checkQuality && !$scope.isFamiliar && !passQualityCheck()) {
                $scope.$parent.stopSpinner();
                return;
            }

            $scope.time_out = new Date().toISOString();
            var userEval = {
                evaluationCode: $stateParams.id,
                studentLearning: $scope.studentLearningRating,
                instructionalPractice: $scope.instructionalPracticeRating,
                professionalism: $scope.professionalismRating,
                overall: $scope.overallRating,
                promote: $scope.promotionDecision
            };
            var payload = {
                recommendation: userEval,
                activities: $scope.activities,
                datetimeIn: $scope.time_in,
                datetimeOut: $scope.time_out,
                mode: $scope.profile.mode
            }

            if($scope.showAttentionCheck) {
                if($scope.attentionCheckInput !== $scope.attentionCheckCode)
                    localStorage.setItem('attentionCheck', false);
                else localStorage.setItem('attentionCheck', true);
            }

            if(!$scope.isPractice) {
                postUserEvaluationIfResponseChanged();
            } else {
                appcon.getExpertEvaluation(userEval).then(
                    function success(response) {
                        $scope.expert = response.data;
                        $scope.feedbackIsAvailable = feedbackIsAvailable();
                        postUserEvaluationIfResponseChanged();
                    },
                    handleFailure);
            }

            function postUserEvaluationIfResponseChanged() {
                if(responseChanged($scope.oldRes, userEval)) {
                    appcon.postUserEvaluation(payload)
                    .then(function success(response) {
                        showFeedback();
                        $scope.$parent.stopSpinner();
                        toaster.pop('success', 'Saved!', 'Your response has been saved successfully!');
                    }, handleFailure);
                } else {
                    showFeedback();
                    $scope.$parent.stopSpinner();
                }
            }

            function showFeedback() {
                if($scope.isPractice) {
                    $scope.feedbackModal = {};
                    var feedbackModal = $scope.feedbackModal;
                    feedbackModal.feedbackModalBtnsDisabled = true;
                    feedbackModal.feedbackModalDuration = 10;
                    feedbackModal.showCountDownLabel = false;
                    $('#feedbackModal').modal({backdrop: 'static', keyboard: false});
                    if(!$scope.isFamiliar) {
                        feedbackModal.showCountDownLabel = true;
                         $interval(function() {
                            if(feedbackModal.feedbackModalDuration == 0)
                                feedbackModal.feedbackModalBtnsDisabled = false;
                            else
                                feedbackModal.feedbackModalDuration--;
                        }, 1000, parseInt(feedbackModal.feedbackModalDuration) + 1);
                    } else
                        feedbackModal.feedbackModalBtnsDisabled = false;
                }
                else
                    $scope.routeToNextPage();
            }

            function feedbackIsAvailable() {
                var id = $stateParams.id;
                if($scope.profile.feedback === 'high')
                    return true;
                // low experience
                if($scope.profile.practice === 2 && id === 'P1')
                    return true;
                // high experience
                if($scope.profile.practice === 4 && (id === 'P1' || id === 'P3'))
                    return true;
                else return false;
            }

            function formIsValid() {
                if($scope.studentLearningRating === undefined ||
                    $scope.instructionalPracticeRating === undefined || $scope.professionalismRating === undefined
                     || $scope.overallRating === undefined) {

                    alert('All fields in the form below are required. Please make sure to fill all fields out');
                    return false;
                }
                if((!$scope.isPractice) && $scope.promotionDecision === undefined) {
                    alert('All fields in the form below are required. Please make sure to fill all fields out');
                    return false;
                }
                return true;
            };

            // Test whether the quality of the answers provided pass the requirements.
            function passQualityCheck() {
                if($scope.unreadReviews.size !== 0) {
                    var msg = "You must read all supervisor reviews. You have not read the following:\n"
                    angular.forEach($scope.unreadReviews, function(item) {
                        msg += item + "\n";
                    });
                    alert(msg);
                    return false;
                }
                if($scope.countdown != undefined && $scope.countdown < parseInt($scope.waitDuration)) {
                    alert("You need to spend at least " + $scope.waitDuration + " seconds on this evaluation before submitting. Please use this time to read teacher reviews below.");
                    return false;
                }
                else return true;
            }

            function responseChanged(oldRes, newRes) {
                if(oldRes === undefined)
                    return true;
                else return ($scope.isRelative ? oldRes.recommendationPick != newRes.recommendationPick : false) ||
                    oldRes.absConfidence != newRes.absConfidence ||
                    oldRes.relConfidence != newRes.relConfidence ||
                    ($scope.isRelative ? false : oldRes.promote != newRes.promote) ||
                    ($scope.isRelative ? false : oldRes.studentLearning != newRes.studentLearning) ||
                    ($scope.isRelative ? false : oldRes.instructionalPractice != newRes.instructionalPractice) ||
                    ($scope.isRelative ? false : oldRes.professionalism != newRes.professionalism) ||
                    ($scope.isRelative ? false : oldRes.overall != newRes.overall);
            }
        }

        $scope.routeToNextPage = function() {
            $window.scrollTo(0, 0); // scroll to top
            $('#feedbackModal').modal('hide');
            $('body').removeClass('modal-open');
            $(".modal-backdrop").remove();
            var currentEval = parseInt($scope.currentEvaluation);
            if($scope.isPractice) {
                if(currentEval < $scope.profile.practice)
                    $state.go('evaluation', {id: 'P' + (currentEval + 1)});
                else
                    $state.go('evaluation', {id: 1});
            } else {
                var nextEvaluationCode = parseInt($stateParams.id) + 1;
                if(nextEvaluationCode > $scope.totalEvaluations) // reached the end of the experiment
                    $state.go('progress');
                else
                    $state.go('evaluation', {id: nextEvaluationCode});
            }
        };

        function handleFailure(response) {
            console.log(response);
            var error = response.data === null ? 'Server unreachable' : response.data.message;
            toaster.pop('error', 'Error', 'Oops! we are having a bit of trouble! Details: ' + error);
            $scope.$parent.stopSpinner();
        }

    };

    module.exports = evaluation_controller;

})();