(function() {

    var auth_vars = require('./auth0-vars.js');

    app_config.$inject = [
        '$stateProvider',
        '$locationProvider',
        '$urlRouterProvider',
        'angularAuth0Provider',
        'appconProvider'
    ];

    function app_config($stateProvider, $locationProvider,
                        $urlRouterProvider, angularAuth0Provider, appconProvider) {

//        var URL = 'http://ec2-34-221-155-156.us-west-2.compute.amazonaws.com:7543';
        var URL = 'http://localhost:7543';

        // Configure state provider for UI routes
        $stateProvider
          .state('welcome', {
             url: '/',
             controller: 'welcomeController',
             templateUrl: 'app/template/welcome.html'
          })
          .state('home', {
             url: '/home',
             controller: 'homeController',
             templateUrl: 'app/template/home.html'
           })
          .state('callback', {
            url: '/callback',
            controller: 'callbackController',
            templateUrl: 'app/template/callback.html'
          })
          .state('procedure', {
            url: '/procedure',
            controller: 'procedureController',
            templateUrl: 'app/template/procedure.html'
          })
          .state('scoring-matrix', {
            url: '/scoring-matrix',
            controller: 'scoringMatrixController',
            templateUrl: 'app/template/scoring-matrix.html'
          })
          .state('tenure', {
            url: '/tenure',
            controller: 'tenureController',
            templateUrl: 'app/template/tenure.html'
          })
          .state('questionnaire', {
            url:'/questionnaire',
            controller: 'quesGeneralController',
            templateUrl:  'app/template/questionnaire/general.html'
          })
          .state('experience', {
            url: '/questionnaire/experience',
            controller: 'quesExperienceController',
            templateUrl: 'app/template/questionnaire/experience.html'
          })
          .state('confidence', {
            url: '/questionnaire/confidence',
            controller: 'questConfidenceController',
            templateUrl: 'app/template/questionnaire/confidence.html'
          })
          .state('evaluation', {
            url: '/evaluation/:id',
            controller: 'evaluationController',
            templateUrl: 'app/template/evaluation.html'
          })
          .state('progress', {
            url: '/progress',
            controller: 'progressController',
            templateUrl: 'app/template/progress.html'
          })
          .state('contactus', {
            url: '/contactus',
            controller: 'contactUsController',
            templateUrl: 'app/template/contactus.html'
          });

          // Configure auth provider
          angularAuth0Provider.init({
              clientID: auth_vars.clientID,
              domain: auth_vars.domain,
              responseType: 'token id_token',
              audience: 'https://appraisal-grenoble-bourji.auth0.com/userinfo',
              redirectUri: URL + '/#/callback',
              scope: 'openid profile'
          });

          $urlRouterProvider.otherwise('/');
          $locationProvider.hashPrefix('');

          appconProvider.setUrl(URL);
          console.log('Just configured app con');
    }

    module.exports = app_config;
})();