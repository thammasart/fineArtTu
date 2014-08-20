angular.module('ExperimentCreator', ['ui.bootstrap'])
    .controller('ExController', function($scope, $rootScope, $http){
        $scope.word = /^[a-zA-Z0-9ก-๙_ \-]*$/;
        $scope.value = 3;
        $scope.isEditName = false;
        $scope.isEditDate = false;
        $scope.startDateOpened = false;
        $scope.expireDateOpened = false;
        $scope.inProcess = false;
        $scope.today = new Date();

        $scope.init = function(){
            $rootScope.exp = {
                name : '',
                startDate : $scope.today,
                expireDate :$scope.today
            }
        }

        $scope.getInputStatus = function(input){
            if(input.$pristine)
                return 2;
            if(input.$valid)
                return 0;
            return 1;
        };

        $scope.checkDisabled = function(disabled){
            return disabled ? 'disabled' : '';
        }

        $scope.editName = function(){
            if($scope.isEditName){
                $scope.save('name');
            }

            $scope.isEditName = !$scope.isEditName;

        }

        $scope.textEdit = function(editable){
            return editable ? 'save' : 'edit';
        }

        $scope.save =function(field){
            var name = '', startDate = -1, expireDate = -1;
            if(field == 'name')
                name = $rootScope.exp.name;
            else if(field == 'date') {
                $scope.isEditDate = false;
                startDate = new Date($rootScope.exp.startDate).getTime();
                expireDate = new Date($rootScope.exp.expireDate).getTime();
            }

            $http({method:'PUT',url:'saveExperiment',params:{id:$rootScope.exp.id,name:name, startDate:startDate, expireDate:expireDate}})
                .success(function(result){
                    $scope.inProcess = false;
                    console.log(result);
                }).error(function(result){
                    console.log('error:' + result);
                    $scope.inProcess = false;
                });
        }

        $rootScope.exp = {};

        $scope.showWeeks = true;
        $scope.toggleWeeks = function () {
            $scope.showWeeks = ! $scope.showWeeks;
        };

        // Disable weekend selection
        $scope.disabled = function(date, mode) {
            return ( mode === 'day' && ( date.getDay() === 0 || date.getDay() === 6 ) );
        };

        $scope.toggleMin = function() {
            $scope.minDate = ( $scope.minDate ) ? null : new Date();
        };
        $scope.toggleMin();

        $scope.open = function($event, target) {
            $event.preventDefault();
            $event.stopPropagation();

            if(target === 0 ) {
                $scope.startDateOpened = true;
                $scope.isEditDate = true;
            }else if(target === 1) {
                $scope.expireDateOpened = true;
                $scope.isEditDate = true;
            }
        };

        $scope.dateOptions = {
            'year-format': "'yy'",
            'starting-day': 1
        };

        $scope.formats = ['dd-MMMM-yyyy', 'yyyy/MM/dd', 'shortDate', 'dd-MM-yyyy'];
        $scope.format = $scope.formats[3];

    })
    .controller('BrownPetersonCtrl', function($scope, $rootScope, $http){
        $scope.trigramTypes = ['word', 'nonsense'];
        $scope.trigramLanguages = ['english','thai'];
        $scope.trials = [];
        $scope.nonsenseThai = [];
        $scope.nonsenseEnglish = [];
        $scope.wordThai = [];
        $scope.wordEnglish = [];
        $scope.inProcess = false;
        $rootScope.exp = {};

        $scope.init = function(expId) {
            $scope.inProcess = true;
            $http({method: 'GET', url: 'brownPetersonInit', params: {expId: expId}}).success(function (result) {
                $scope.trials = result.trials;
                $rootScope.exp = $scope.trials[0].schedule;
                $scope.nonsenseThai = result.nonsenseThai;
                $scope.nonsenseEnglish = result.nonsenseEng;
                $scope.wordThai = result.wordThai;
                $scope.wordEnglish = result.wordEnglish;
                $scope.inProcess = false;
                console.log($scope.trials);
            }).error(function (result) {
                console.log('error:' + result);
                $scope.inProcess = false;
            });
        }

        $scope.refresh = function(trial){
            for(var i=0; i<trial.quizzes.length; i++){
                trial.quizzes[i].question = findQuestion(trial.trigramType, trial.trigramLanguage);
            }
        }

        $scope.saveAll = function(){
            $scope.inProcess = true;
            $http({method:'PUT',url:'saveBrownPetersonTrials',data:$scope.trials})
                .success(function(result){
                    $scope.inProcess = false;
                    console.log(result);
                }).error(function(result){
                    console.log('error:' + result);
                    $scope.inProcess = false;
                });
        }
        function findQuestion(trigramType, trigramLanguage){
            if(trigramType == 'word'){
                if(trigramLanguage == 'english'){
                    return $scope.wordEnglish[Math.floor(Math.random() * $scope.wordEnglish.length)];
                }else{
                    return $scope.wordThai[Math.floor(Math.random() * $scope.wordThai.length)];
                }
            }else{
                if(trigramLanguage == 'english'){
                    return $scope.nonsenseEnglish[Math.floor(Math.random() * $scope.nonsenseEnglish.length)];
                }else{
                    return $scope.nonsenseThai[Math.floor(Math.random() * $scope.nonsenseThai.length)];
                }
            }
        }

    })
    .controller('AttentionBlinkCtrl', function($scope, $rootScope, $http){
        $scope.trials = [];
        $scope.word = /^[0-9]*\.?[0-9]+$/;
        $scope.inProcess = false;

        $scope.questionTypes = ['THAI','ENGLISH','NUMBER'];

        $scope.init = function(expId) {
            $http({method: 'GET', url: 'attentionBlinkInit', params: {expId: expId}}).success(function (result) {
                $scope.trials = result.trials;
                $rootScope.exp = $scope.trials[0].schedule;
                $scope.inProcess = false;
                console.log($scope.trials);
            }).error(function (result) {
                console.log('error:' + result);
                $scope.inProcess = false;
            });
        }

        $scope.refresh = function(trial){
            for(var i=0; i<trial.quizzes.length; i++){
                $scope.generateQuestion(trial.quizzes[i], trial.questionType);
            }
        }

        $scope.generateQuestion = function(quiz, questionType){
            var ENGLISH_CASE = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ';
            var THAI_CASE = "กขคฆงจฉชฌญฎฐฒณดถทธนบผพภมยรลวศษสหฬอ";
            var NUMBER_CASE = '0123456789';
            if(questionType == 'THAI'){
                quiz.question.questionType = 'THAI';
                generateTextQuestion(quiz, THAI_CASE);
            }else if(questionType == 'ENGLISH'){
                quiz.question.questionType = 'ENGLISH';
                generateTextQuestion(quiz, ENGLISH_CASE);
            }else if(questionType == 'NUMBER'){
                quiz.question.questionType = 'NUMBER';
                generateTextQuestion(quiz, NUMBER_CASE);
            }
        }

        $scope.saveAll = function(){
            $scope.inProcess = true;
            $http({method:'PUT',url:'saveAttentionBlinkTrials',data:$scope.trials})
                .success(function(result){
                    $scope.inProcess = false;
                    console.log(result);
                }).error(function(result){
                    console.log('error:' + result);
                    $scope.inProcess = false;
                });
        }

        function generateTextQuestion(quiz, CASE){

            var text = generateText(CASE,quiz.numberOfTarget);
            quiz.question.letter = text;
            if(quiz.isCorrect){
                text = generateText(CASE,quiz.length);
                var start = Math.floor(Math.random() * (quiz.length - quiz.numberOfTarget));
                var sub = text.substr(start, quiz.numberOfTarget);
                quiz.question.set = text.replace(sub, quiz.question.letter);
            }else{
                text = generateText(CASE,quiz.length);
                while(text.search(quiz.question.letter) != -1)
                    text = generateText(CASE,quiz.length);
                quiz.question.set = text;
            }
        }

        function generateText(CASE, length){
            var text = '';
            for(var i=0; i<length; i++){
                text += CASE.charAt(Math.floor(Math.random() * CASE.length));
            }
            return text;
        }
    })
    .controller('SignalDetectionCtrl', function($scope, $rootScope, $http){
        $scope.single = /^[a-zA-Z0-9ก-ฮ]{1}$/;
        $scope.floatPattern = /^[0-9]*\.?[0-9]+$/;
        $rootScope.exp = {};
        $scope.trials = [];
        $scope.inProcess = false;

        $scope.init = function(expId) {
            $http({method: 'GET', url: 'signalDetectionInit', params: {expId: expId}}).success(function (result) {
                $scope.trials = result.trials;
                $rootScope.exp = $scope.trials[0].schedule;
                $scope.inProcess = false;
                console.log($scope.trials);
            }).error(function (result) {
                console.log('error:' + result);
                $scope.inProcess = false;
            });
        }

        $scope.saveAll = function(){
            $scope.inProcess = true;
            $http({method:'PUT',url:'saveSignalDetectionTrials',data:$scope.trials})
                .success(function(result){
                    $scope.inProcess = false;
                    console.log(result);
                }).error(function(result){
                    console.log('error:' + result);
                    $scope.inProcess = false;
                });
        }
    })
    .controller('StroofEffectCtrl', function($scope, $rootScope, $http){
        $scope.questionTypes = ['THAI', 'ENGLISH'];
        $scope.trials = [];
        $scope.inProcess = false;

        var matchEnglish = [];
        var matchThai = [];
        var notMatchEnglish = [];
        var notMatchThai = [];

        $scope.init = function(expId) {
            $scope.inProcess = true;
            $http({method: 'GET', url: 'stroofEffectInit', params: {expId: expId}}).success(function (result) {
                $scope.trials = result.trials;
                $rootScope.exp = $scope.trials[0].schedule;
                initQuestion(result.questions);
                $scope.inProcess = false;
                console.log($scope.trials);
            }).error(function (result) {
                console.log('error:' + result);
                $scope.inProcess = false;
            });
        }

        $scope.randomQuestion = function(quiz, questionType){
            if(questionType == 'ENGLISH'){
                if(quiz.question.match){
                    quiz.question = angular.copy(matchEnglish[Math.floor(Math.random() * matchEnglish.length)]);
                }else{
                    quiz.question = angular.copy(notMatchEnglish[Math.floor(Math.random() * matchEnglish.length)]);
                }
            }else{
                if(quiz.question.match){
                    quiz.question = angular.copy(matchThai[Math.floor(Math.random() * matchEnglish.length)]);
                }else{
                    quiz.question = angular.copy(notMatchThai[Math.floor(Math.random() * matchEnglish.length)]);
                }
            }
        }

        $scope.refresh = function(trial){
            for(var i=0; i<trial.quizzes.length; i++){
                $scope.randomQuestion(trial.quizzes[i], trial.questionType);
            }
        }

        $scope.saveAll = function(){
            $scope.inProcess = true;
            resetQuestion();
            $http({method:'PUT',url:'saveStroofEffectTrials',data:$scope.trials})
                .success(function(result){
                    $scope.inProcess = false;
                    console.log(result);
                }).error(function(result){
                    console.log('error:' + result);
                    $scope.inProcess = false;
                });
        }

        function resetQuestion(){
            for(var i=0; i<$scope.trials.length; i++){
                for(var j=0; j<$scope.trials[i].quizzes.length; j++){
                    $scope.trials[i].quizzes[j].question = getNewQuestion($scope.trials[i].quizzes[j].question);
                }
            }
        }

        function getNewQuestion(question){
            return {
                id:question.id,
                colorWord:question.colorWord,
                inkColor:question.inkColor,
                questionType: question.questionType
            }
        }

        function initQuestion(questions){
            for(var i=0; i<questions.length; i++){
                if(questions[i].questionType == 'ENGLISH'){
                    if(questions[i].match){
                        matchEnglish.push(questions[i]);
                    }else{
                        notMatchEnglish.push(questions[i])
                    }
                }else{
                    if(questions[i].match){
                        matchThai.push(questions[i]);
                    }else{
                        notMatchThai.push(questions[i])
                    }
                }
            }

        }
    })
    .controller('PositionErrorCtrl', function($scope, $rootScope, $http){
        $scope.word = /^[0-9]*\.?[0-9]+$/;
        $scope.memorySet = [ 3, 5, 7, 10, 12];
        $scope.questionTypes = ['THAI', 'ENGLISH', 'NUMBER'];
        $scope.trials = [];
        $scope.inProcess = false;
        $scope.isEditable = [];

        $scope.init = function(expId) {
            $scope.inProcess = true;
            $http({method: 'GET', url: 'PositionErrorInit', params: {expId: expId}}).success(function (result) {
                $scope.trials = result.trials;
                $rootScope.exp = $scope.trials[0].schedule;
                initEditable();
                $scope.inProcess = false;
                console.log($scope.trials);
            }).error(function (result) {
                console.log('error:' + result);
                $scope.inProcess = false;
            });
        }

        function initEditable(){
            for(var i=0; i<$scope.trials.length; i++){
                $scope.isEditable.push([]);
                for(var j=0; j<$scope.trials[i].quizzes.length; j++){
                    $scope.isEditable[i].push(false);
                }
            }
            console.log($scope.isEditable);
        }

        $scope.edit = function(i, j){
            if($scope.isEditable[i][j]){
                console.log('save');
            }else{
                console.log('edit');
            }
            $scope.isEditable[i][j] = !$scope.isEditable[i][j];
        }

        $scope.editText = function(i, j){
            if($scope.isEditable[i][j]){
                return 'ok';
            }
            return 'edit';
        }

        $scope.generateQuestion = function(quiz, questionType){
            var ENGLISH_CASE = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ';
            var THAI_CASE = "กขคฆงจฉชฌญฎฐฒณดถทธนบผพภมยรลวศษสหฬอ";
            var NUMBER_CASE = '0123456789';

            if(questionType == 'THAI'){
                quiz.question.questionType = 'THAI';
                generateText(quiz, THAI_CASE);
            }else if(questionType == 'ENGLISH'){
                quiz.question.questionType = 'ENGLISH';
                generateText(quiz, ENGLISH_CASE);
            }else{
                quiz.question.questionType = 'NUMBER';
                generateText(quiz, NUMBER_CASE);
            }
        }

        $scope.saveAll = function(){
            $scope.inProcess = true;
            $http({method:'PUT',url:'savePositionErrorTrials',data:$scope.trials})
                .success(function(result){
                    $scope.inProcess = false;
                    console.log(result);
                }).error(function(result){
                    console.log('error:' + result);
                    $scope.inProcess = false;
                });
        }

        function generateText(quiz, CASE){
            var text = '';
            for(var i=0; i<quiz.length; i++){
                text += CASE.charAt(Math.floor(Math.random() * CASE.length));
            }
            quiz.question.memorySet = text;
        }

        $scope.refresh = function(trial){
            for(var i=0; i<trial.quizzes.length; i++){
                $scope.generateQuestion(trial.quizzes[i], trial.questionType);
            }
        }

    })
    .controller('SternbergSearchCtrl', function($scope, $rootScope, $http){
        $scope.word = /^[0-9]*\.?[0-9]+$/;
        $scope.showQuiz = true;
        var ENGLISH_CASE = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ';
        var THAI_CASE = "กขคฆงจฉชฌญฎฐฒณดถทธนบผพภมยรลวศษสหฬอ";
        var NUMBER_CASE = '0123456789';
        $scope.questionTypes = ['THAI', 'ENGLISH', 'NUMBER'];
        $scope.trials = [];
        $scope.inProcess = false;

        $scope.init = function(expId) {
            $scope.inProcess = true;
            $http({method: 'GET', url: 'sternbergInit', params: {expId: expId}}).success(function (result) {
                $scope.trials = result.trials;
                $rootScope.exp = $scope.trials[0].schedule;
                $scope.inProcess = false;
                console.log($scope.trials);
            }).error(function (result) {
                console.log('error:' + result);
                $scope.inProcess = false;
            });
        }

        $scope.generateQuestion = function(trial){

            if(trial.questionType == 'THAI'){
                trial.quizzes[0].question.questionType = 'THAI';
                generateText(trial, THAI_CASE);
            }else if(trial.questionType == 'ENGLISH'){
                trial.quizzes[0].question.questionType = 'ENGLISH';
                generateText(trial, ENGLISH_CASE);
            }else{
                trial.quizzes[0].question.questionType = 'NUMBER';
                generateText(trial, NUMBER_CASE);
            }

            generateQuiz(trial);
        }

        function generateText(trial, CASE){
            var charSet = generateCharSet(CASE);
            var temp = angular.copy(charSet);
            var text = '';
            for(var i=0; i<trial.length; i++){
                if(temp.length == 0){
                    temp = angular.copy(charSet);
                }
                var index = Math.floor(Math.random() * temp.length);
                text += temp[index];
                temp.splice(index, 1);
            }
            trial.quizzes[0].question.memorySet = text;
        }

        function generateQuiz(trial){
            var question = trial.quizzes[0].question;
            trial.quizzes = [];
            generateOneCharCorrectQuiz(trial, question);
            generateTwoCharCorrectQuiz(trial, question);
            if(trial.questionType == 'THAI'){
                generateOneCharInCorrectQuiz(trial, question, THAI_CASE);
                generateTwoCharInCorrectQuiz(trial, question, THAI_CASE);
            }else if(trial.questionType == 'ENGLISH'){
                generateOneCharInCorrectQuiz(trial, question, ENGLISH_CASE);
                generateTwoCharInCorrectQuiz(trial, question, ENGLISH_CASE);
            }else{
                generateOneCharInCorrectQuiz(trial, question, NUMBER_CASE);
                generateTwoCharInCorrectQuiz(trial, question, NUMBER_CASE);
            }

            trial.quizzes = shuffle(trial.quizzes);
        }

        function generateTwoCharInCorrectQuiz(trial, question, CASE){
            if(trial.length < 2)
                return;

            var quizSet = generateCharSetExceptMemorySet(CASE, question.memorySet);
            var temp = angular.copy(quizSet);
            for(var i=0; i<trial.twoCharIsInCorrect; i++){
                var text = question.memorySet.charAt(Math.floor(Math.random() * (question.memorySet.length - 1) ));
                var index = Math.floor(Math.random() * temp.length);
                text += temp[index];
                temp.splice(index, 1);
                trial.quizzes.push(Quiz(text, false, question));
            }
        }

        function generateCharSetExceptMemorySet(CASE, memorySet){
            var quizSet = [];
            for(var i=0; i<CASE.length; i++){
                var char = CASE.charAt(i);
                if(memorySet.search(char) == -1)
                    quizSet.push(CASE.charAt(i));
            }
            return quizSet;
        }

        function generateTwoCharCorrectQuiz(trial, question){
            if(trial.length < 2)
                return;
            var temp = [];
            for(var i=0; i<trial.twoCharIsCorrect; i++){
                var text = generateTwoChar(temp, question.memorySet);
                var quiz = Quiz(text, true, question);
                trial.quizzes.push(quiz);
            }
        }

        function generateTwoChar(array, memorySet){
            var index = Math.floor(Math.random() * (memorySet.length - 1));
            var text = '';
            text += memorySet.charAt(index);
            text += memorySet.charAt(index + 1);
            if(duplicateQuestion(array, text)){
                text = generateTwoChar(array, memorySet);
            }else{
                array.push(text);
            }
            return text;
        }
        function duplicateQuestion(array, text){
            for(var i=0; i<array.length; i++){
                if(array[i] == text)
                    return true;
            }
            return false;
        }

        function generateOneCharCorrectQuiz(trial, question){
            var quizSet = generateCharSet(question.memorySet);
            var temp = angular.copy(quizSet);
            for(var i=0; i<trial.oneCharIsCorrect; i++){
                if(temp.length == 0){
                    temp = angular.copy(quizSet);
                }
                var index = Math.floor(Math.random() * temp.length);
                var quiz = Quiz(temp[index], true, question);
                temp.splice(index, 1);
                trial.quizzes.push(quiz);
            }
        }

        function generateOneCharInCorrectQuiz(trial, question, CASE){
            var quizSet = generateCharSet(CASE);
            var temp = angular.copy(quizSet);
            for(var i=0; i<trial.oneCharIsInCorrect; i++){
                if(temp.length == 0){
                    temp = angular.copy(quizSet);
                }
                var questionChar = generateChar(temp, question.memorySet);
                var quiz = Quiz(questionChar, false, question);
                trial.quizzes.push(quiz);
            }

        }

        function generateChar(quizSet, memorySet){
            var index = Math.floor(Math.random() * quizSet.length);
            var questionChar = quizSet[index];
            if(memorySet.search(questionChar) > -1){
                questionChar = generateChar(quizSet, memorySet);
            }else{
                quizSet.splice(index, 1);
            }

            return questionChar;
        }

        function generateCharSet(CASE){
            var quizSet = [];
            for(var i=0; i<CASE.length; i++){
                quizSet.push(CASE.charAt(i));
            }
            return quizSet;
        }

        function Quiz(questionChar, isTrue, question){
            return {
                questionChar: questionChar,
                isTrue: isTrue,
                question: question
            }
        }

        $scope.getQuestion = function(questionChar){
            if(questionChar.length <= 1){
                return questionChar;
            }
            var text = questionChar.charAt(0);
            text += ' และ ';
            text += questionChar.charAt(1);
            return text;
        }
        $scope.saveAll = function(){
            $scope.inProcess = true;
            $http({method:'PUT',url:'saveSternBergTrials',data:$scope.trials})
                .success(function(result){
                    $scope.inProcess = false;
                    console.log(result);
                }).error(function(result){
                    console.log('error:' + result);
                    $scope.inProcess = false;
                });
        }

        function shuffle(array) {
            var currentIndex = array.length
                , temporaryValue
                , randomIndex
                ;

            // While there remain elements to shuffle...
            while (0 !== currentIndex) {

                // Pick a remaining element...
                randomIndex = Math.floor(Math.random() * currentIndex);
                currentIndex -= 1;

                // And swap it with the current element.
                temporaryValue = array[currentIndex];
                array[currentIndex] = array[randomIndex];
                array[randomIndex] = temporaryValue;
            }

            return array;
        }

    })
    .controller('MagicNumber7Ctrl', function($scope, $rootScope, $http){
        $scope.word = /^[0-9]*\.?[0-9]+$/;
        $scope.questionTypes = ['THAI', 'ENGLISH', 'NUMBER'];
        $scope.trials = [];
        $scope.inProcess = false;

        $scope.init = function(expId) {
            $scope.inProcess = true;
            $http({method: 'GET', url: 'MagicSevenInit', params: {expId: expId}}).success(function (result) {
                $scope.trials = result.trials;
                $rootScope.exp = $scope.trials[0].schedule;
                $scope.inProcess = false;
                console.log($scope.trials);
            }).error(function (result) {
                console.log('error:' + result);
                $scope.inProcess = false;
            });
        }

        $scope.generateQuestion = function(quiz, questionType){
            var ENGLISH_CASE = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ';
            var THAI_CASE = "กขคฆงจฉชฌญฎฐฒณดถทธนบผพภมยรลวศษสหฬอ";
            var NUMBER_CASE = '0123456789';

            if(questionType == 'THAI'){
                quiz.question.questionType = 'THAI';
                generateText(quiz, THAI_CASE);
            }else if(questionType == 'ENGLISH'){
                quiz.question.questionType = 'ENGLISH';
                generateText(quiz, ENGLISH_CASE);
            }else{
                quiz.question.questionType = 'NUMBER';
                generateText(quiz, NUMBER_CASE);
            }
        }

        $scope.saveAll = function(){
            $scope.inProcess = true;
            $http({method:'PUT',url:'saveMagicSevenTrials',data:$scope.trials})
                .success(function(result){
                    $scope.inProcess = false;
                    console.log(result);
                }).error(function(result){
                    console.log('error:' + result);
                    $scope.inProcess = false;
                });
        }

        function generateText(quiz, CASE){
            var text = '';
            for(var i=0; i<quiz.length; i++){
                text += CASE.charAt(Math.floor(Math.random() * CASE.length));
            }
            quiz.question.memorySet = text;
        }

        $scope.refresh = function(trial){
            for(var i=0; i<trial.quizzes.length; i++){
                $scope.generateQuestion(trial.quizzes[i], trial.questionType);
            }
        }
    })
    .controller('SimonEffectCtrl', function($scope, $rootScope, $http){
        $scope.trials = [];
        $scope.floatPattern = /^[0-1]*\.?[0-9]+$/;

        $scope.questionTypes = ['ONEFEATURE','TWOFEATURE'];
        $scope.positions = ['up', 'down', 'right', 'left'];
        $scope.trials = [];
        var questions = [];
        var oneFeatureQuestion = [];

        $scope.inProcess = false;

        $scope.init = function(expId) {
            $scope.inProcess = true;
            $http({method: 'GET', url: 'SimonEffectInit', params: {expId: expId}}).success(function (result) {
                $scope.trials = result.trials;
                $rootScope.exp = $scope.trials[0].schedule;
                questions = result.questions;
                initQuestion();
                $scope.inProcess = false;
                console.log($scope.trials);
            }).error(function (result) {
                console.log('error:' + result);
                $scope.inProcess = false;
            });
        }

        $scope.randomQuestion = function(quiz, questionType){
            if(questionType == 'ONEFEATURE'){
                quiz.question = oneFeatureQuestion[Math.floor(Math.random() * oneFeatureQuestion.length)];
            }else{
                quiz.question = questions[Math.floor(Math.random() * questions.length)];
            }
            quiz.position = $scope.positions[Math.floor(Math.random() * $scope.positions.length)];
        }

        $scope.refresh = function(trial){
            for(var i=0; i<trial.quizzes.length; i++){
                $scope.randomQuestion(trial.quizzes[i], trial.questionType);
            }
        }

        $scope.saveAll = function(){
            $scope.inProcess = true;
            $http({method:'PUT',url:'saveSimonEffectTrials',data:$scope.trials})
                .success(function(result){
                    $scope.inProcess = false;
                    console.log(result);
                }).error(function(result){
                    console.log('error:' + result);
                    $scope.inProcess = false;
                });
        }

        function initQuestion(){
            for(var i=0; i<questions.length; i++){
                if(questions[i].color == 'red' && questions[i].alphabet == 'X'||
                    questions[i].color == 'green' && questions[i].alphabet == 'O'){
                    oneFeatureQuestion.push(questions[i]);
                }
            }
        }
    })
    .controller('VisualSearchCtrl', function($scope, $rootScope, $modal, $http){

        $scope.inProcess = false;
        $scope.overNoise = false;

        $scope.trials = [];

        $scope.init = function(expId){
            $scope.inProcess = true;
            $http({method:'GET',url:'init',params:{exp_id:expId}}).success(function(result){
                $scope.trials = result.trials;
                $rootScope.exp = $scope.trials[0].schedule;
                $scope.inProcess = false;
                console.log($scope.trials);
            }).error(function(result){
                console.log('error:' + result);
                $scope.inProcess = false;
            });
        }

        $scope.save = function(trial){
            $scope.inProcess = true;
            $http({method:'POST',url:'saveVisualSearch',data:trial.quiz.question.sharps,
                params:{trialId:trial.id,
                circleGreen:trial.quiz.circleGreen, circleRed:trial.quiz.circleRed,
                circleBlue: trial.quiz.circleBlue,
                squareBlue:trial.quiz.squareBlue, squareRed:trial.quiz.squareRed,
                squareGreen:trial.quiz.squareGreen,
                positionXofTarget: trial.quiz.positionXofTarget, 
                positionYofTarget: trial.quiz.positionYofTarget,
                frameSize: trial.quiz.frameSize, target: trial.quiz.target}})
            .success(function(result){
                $scope.inProcess = false;
                console.log(result);
            }).error(function(result){
                console.log('error:' + result);
                $scope.inProcess = false;
            });
        }

        $scope.frameSizes = ['SMALLER', 'SMALL', 'MEDIUM', 'BIG', 'EXTRA'];
        $scope.targets = ['SQAURE_GREEN', 'SQAURE_BLUE', 'SQAURE_RED', 
        'CIRCLE_GREEN', 'CIRCLE_BLUE', 'CIRCLE_RED'];

        $scope.changeTargetValue = function(trial){
            if(trial.quiz.target == 'SQAURE_GREEN')
                trial.quiz.squareGreen = 1;
            else if(trial.quiz.target == 'SQAURE_BLUE')
                trial.quiz.squareBlue = 1;
            else if(trial.quiz.target == 'SQAURE_RED')
                trial.quiz.squareRed = 1;
            else if (trial.quiz.target == 'CIRCLE_RED')
                trial.quiz.circleRed = 1;
            else if (trial.quiz.target == 'CIRCLE_BLUE')
                trial.quiz.circleBlue = 1;
            else if (trial.quiz.target == 'CIRCLE_GREEN')
                trial.quiz.circleGreen = 1;
        }

        $scope.open = function(trial){
            var modalInstance = $modal.open({
                templateUrl: 'preview.html',
                controller: ModalInstanceCtrl,
                size: 'lg',
                resolve: {
                    sharps : function(){
                        return $scope.sharps;
                    },
                    width : function(){
                        return $scope.width(trial);
                    },
                    height : function(){
                        return $scope.height(trial);
                    },
                    trial : function(){
                        return trial;
                    }
                }

            });

            modalInstance.result.then(function (result){
               trial.quiz.positionYofTarget = result.top;
               trial.quiz.positionXofTarget = result.left;
            });
        };

        $scope.max = function(trial){
            var frameSize = trial.quiz.frameSize;
            if(frameSize == 'SMALLER')
                return 30;
            if(frameSize == 'SMALL')
                return 50;
            if(frameSize == 'MEDIUM')
                return 90;
            if(frameSize == 'BIG')
                return 140;
            if(frameSize == 'EXTRA')
                return 160;

            return 25;
        };

        $scope.totalNoise = function(trial){
            return trial.quiz.squareRed + trial.quiz.squareBlue + trial.quiz.squareGreen + trial.quiz.circleGreen + trial.quiz.circleRed + trial.quiz.circleGreen;
        }



        $scope.width = function(trial){
            var frameSize = trial.quiz.frameSize;
            if(frameSize == 'SMALLER')
                return 300;
            if(frameSize == 'SMALL')
                return 500;
            if(frameSize == 'MEDIUM')
                return 600;
            if(frameSize == 'BIG')
                return 800;
            if(frameSize == 'EXTRA')
                return 1000;

            return 600;
        };
        $scope.height = function(trial){
            var frameSize = trial.quiz.frameSize;
            if(frameSize == 'SMALLER')
                return 200;
            if(frameSize == 'SMALL')
                return 300;
            if(frameSize == 'MEDIUM')
                return 400;
            if(frameSize == 'BIG')
                return 500;
            if(frameSize == 'EXTRA')
                return 500;

            return 400;
        };



    })
    .controller('MullerLayerCtrl', function($scope, $rootScope, $http, $modal){
        $scope.inProcess = false;
        $scope.floatPattern = /^[0-1]*\.?[0-9]+$/;
        $scope.trials = [];

        $scope.noOfChoices = [3,4,5];
        $scope.lengthTypes = ['SHORT', 'MEDIUM','LONG'];

        $scope.init = function(expId){
            $scope.inProcess = true;
            $http({method:'GET',url:'mullerInit',params:{expId:expId}}).success(function(result){
                $scope.trials = result.trials;
                $rootScope.exp = $scope.trials[0].schedule;
                $scope.inProcess = false;
                console.log($scope.trials);
            }).error(function(result){
                console.log('error:' + result);
                $scope.inProcess = false;
            });
        }

        $scope.open = function(quiz){
            var modalInstance = $modal.open({
                templateUrl: 'preview.html',
                controller: MullerModalInstanceCtrl,
                size: 'lg',
                resolve: {
                    quiz : function(){
                        return quiz;
                    }
                }

            });
        };

        $scope.openSummary = function(){
            var modalInstance = $modal.open({
                templateUrl: 'summary.html',
                controller: MullerSummaryInstanceCtrl,
                size: 'lg',
                resolve: {
                    trials : function(){
                        return $scope.trials;
                    }
                }

            });
        };

        $scope.save = function(){
            save();
        }

        function save(){
            $scope.inProcess = true;
            $http({method:'PUT',url:'saveMullerTrial',data:$scope.trials})
            .success(function(result){
                $scope.inProcess = false;
                console.log(result);
            }).error(function(result){
                console.log('error:' + result);
                $scope.inProcess = false;
            });
        }

        $scope.calculateDifferChoice = function(quiz){
            if(quiz.differChoice > quiz.noOfChoice)
                quiz.differChoice = Math.floor((Math.random() * quiz.noOfChoice) + 1);

        }
    })
    .controller('GarnerController', function($scope, $rootScope, $http, $modal){
        $scope.floatPattern = /^[0-9]*\.?[0-9]+$/;
        $scope.inProcess = false;
        $scope.trials = [];

        $scope.colors = ['red', 'blue', 'yellow', 'green', 'grey'];

        $scope.DARK = 0;
        $scope.LIGHT = 1;

        $scope.allColors = [];
        $scope.showColors = [];
        $scope.targets = [];
        $scope.open = [];
        $scope.modes = [];
        $scope.generateSuccess = [];

        var SIZES = ["big", "small"];
        var COLORS = ["dark", "light"];

        $scope.init = function(expId){
            $scope.inProcess = true;
            initAllColors();

            $http({method:'GET',url:'garnerInit',params:{expId:expId}}).success(function(result){
                $scope.trials = result.trials;
                $rootScope.exp = $scope.trials[0].schedule;
                initOpen();
                calculateAllColors(result.colors);
                $scope.inProcess = false;
                console.log($scope.trials);
            }).error(function(result){
                console.log('error:' + result);
                $scope.inProcess = false;
            });
        }

        function initOpen(){
            for(var i=0; i < $scope.trials.length; i++){
                $scope.open.push(false);
                $scope.showColors.push({colors:[]});
                $scope.modes.push($scope.DARK);
                $scope.generateSuccess.push(false);
            }
        }

        $scope.show = function(color, colorObj, index, mode){
            for(var i=0; i<$scope.allColors.length; i++){
                if(color == $scope.allColors[i].name){
                    $scope.showColors[index].colors = $scope.allColors[i].objs;
                    break;
                }
            }
            $scope.modes[index] = mode;
            $scope.targets[index] = colorObj;
            $scope.open[index] = true;
        }
        $scope.changeColor = function(trial, color, index){
            if($scope.modes[index] == $scope.DARK)
                trial.colorDark = color;
            else
                trial.colorLight = color;
            $scope.open[index] = false;
        }

        $scope.saveAll = function(){
            $scope.inProcess = true;
            $http({method:'PUT',url:'saveGarnerTrials',data:$scope.trials})
                .success(function(result){
                    $scope.inProcess = false;
                    console.log(result);
                }).error(function(result){
                    console.log('error:' + result);
                    $scope.inProcess = false;
                });
        }

        function calculateAllColors(colors){
            for(var i=0; i<colors.length; i++){
                for(var j=0; j < $scope.allColors.length; j++){
                    if(colors[i].color == $scope.allColors[j].name){
                        $scope.allColors[j].objs.push(colors[i]);
                    }

                }
            }
        }

        function initAllColors() {
            for(var i=0; i<$scope.colors.length; i++){
                var obj = {
                    name: $scope.colors[i],
                    objs: []
                }
                $scope.allColors.push(obj);
            }
        }

        $scope.generateColor = function(trial, index){
            $scope.open[index] = false;
            for(var i=0; i<$scope.allColors.length; i++){
                if(trial.color == $scope.allColors[i].name){
                    trial.colorDark =  $scope.allColors[i].objs[Math.floor(Math.random() * $scope.allColors[i].objs.length)];
                    trial.colorLight = calculateColor(trial.colorDark, $scope.allColors[i].objs)
                    if(trial.colorDark.saturation < trial.colorLight.saturation){
                        var temp = trial.colorDark;
                        trial.colorDark = trial.colorLight;
                        trial.colorLight = temp;
                    }
                    break;
                }
            }
        }

        function calculateColor(colorDark, colors){
            var colorLight = colors[Math.floor(Math.random() * colors.length)];
            return colorLight.id != colorDark.id ? colorLight : calculateColor(colorDark, colors);
        }

        $scope.generateQuestion = function(trial, index){
            trial.quizzes = [];
            for(var i=0; i < trial.noOfColorQuestion; i++) {
                trial.quizzes.push(Quiz('COLOR',true));
            }
            for(var i=0; i < trial.noOfFakeColorQuestion; i++) {
                trial.quizzes.push(Quiz('COLOR', false));
            }
            for(var i=0; i < trial.noOfSizeQuestion; i++) {
                trial.quizzes.push(Quiz('SIZE', true));
            }
            for(var i=0; i < trial.noOfFakeSizeQuestion; i++) {
                trial.quizzes.push(Quiz('SIZE', false));
            }
            for(var i=0; i < trial.noOfBiDimensionQuestion; i++) {
                trial.quizzes.push(Quiz('BOTH', true));
            }
            for(var i=0; i < trial.noOfFakeBiDimentsionQuestion; i++) {
                trial.quizzes.push(Quiz('BOTH', false));
            }
            trial.quizzes = shuffle(trial.quizzes);
            $scope.generateSuccess[index] = true;

            console.log(trial);
        }

        function destroyAlert(index){
            console.log('destroy alert - ' + index);
            $scope.generateSuccess[index] = false;
            console.log($scope.generateSuccess);
        }

        function Quiz(questionType, isNotFake){
            return {
              questionType : questionType,
              question : createQuestion(questionType, isNotFake)
            };
        }

        function createQuestion(questionType, isNotFake){
            if(questionType == 'COLOR')
                return createColorQuestion(isNotFake);
            else if(questionType == 'SIZE')
                return createSizeQuestion(isNotFake);
            else
                return createBiDimensionQuestion(isNotFake);

        }
        function Question(){
            return {
                colorPic:'',
                sizePic:'',
                colorQuestion:'',
                sizeQuestion:'',
                colorMatch:false,
                sizeMatch:false
            };
        }

        function createColorQuestion(isNotFake){
            var question = Question();
            question.colorPic = generateColorPic();
            question.colorQuestion = isNotFake ? question.colorPic : calculateColorFake(question.colorPic);
            question.colorMatch = isNotFake ? true : false;
            question.sizePic = generateSizePic();
            return question;
        }
        function createSizeQuestion(isNotFake){
            var question = Question();
            question.sizePic = generateSizePic();
            question.sizeQuestion = isNotFake ? question.sizePic : calculateSizeFake(question.sizePic);
            question.sizeMatch = isNotFake ? true : false;
            question.colorPic = generateColorPic();
            return question;
        }
        function createBiDimensionQuestion(isNotFake){
            var question = Question();
            question.colorPic = generateColorPic();
            question.colorQuestion = isNotFake ? question.colorPic : calculateColorFake(question.colorPic);
            question.colorMatch = isNotFake ? true : false;
            question.sizePic = generateSizePic();
            question.sizeQuestion = isNotFake ? question.sizePic : calculateSizeFake(question.sizePic);
            question.sizeMatch = isNotFake ? true : false;
            return question;
        }

        function generateColorPic(){
            return COLORS[Math.floor(Math.random() * COLORS.length)];
        }
        function calculateColorFake(colorPic){
            return colorPic == COLORS[0] ? COLORS[1] : COLORS[0];
        }
        function generateSizePic(){
            return SIZES[Math.floor(Math.random() * SIZES.length)];
        }
        function calculateSizeFake(sizePic){
            return sizePic == SIZES[0] ? SIZES[1] : SIZES[0];
        }

        $scope.openSummary = function(){
            var modalInstance = $modal.open({
                templateUrl: 'summary.html',
                controller: GarnerSummaryInstanceCtrl,
                size: 'lg',
                resolve: {
                    trials : function(){
                        return $scope.trials;
                    }
                }

            });
        };

        function shuffle(array) {
            var currentIndex = array.length
                , temporaryValue
                , randomIndex
                ;

            // While there remain elements to shuffle...
            while (0 !== currentIndex) {

                // Pick a remaining element...
                randomIndex = Math.floor(Math.random() * currentIndex);
                currentIndex -= 1;

                // And swap it with the current element.
                temporaryValue = array[currentIndex];
                array[currentIndex] = array[randomIndex];
                array[randomIndex] = temporaryValue;
            }

            return array;
        }

    })
    .controller('ChangeBlindnessController', function($scope, $rootScope, $http, $modal) {
        $scope.inProcess = false;
        $scope.trials = [];

        $scope.init = function(expId){
            $http({method:'GET',url:'changeBlindnessInit',params:{expId:expId}}).success(function(result){
                $rootScope.exp = result.trials[0].schedule;
                initTrials(result.trials,result.questions);
                $scope.inProcess = false;
            }).error(function(result){
                $scope.inProcess = false;
            });
        }

        $scope.saveAll = function(){
            var trials = [];
            for(var i=0; i<$scope.trials.length; i++){
                $scope.trials[i].trial.quizzes = [];
                for(var j=0; j<$scope.trials[i].pictures.length; j++){
                    if($scope.trials[i].pictures[j].selected){
                        $scope.trials[i].trial.quizzes.push({question:$scope.trials[i].pictures[j].question});
                    }
                }
                trials.push($scope.trials[i].trial);
            }
            $scope.inProcess = true;
            $http({method:'PUT',url:'saveChangeBlindnessTrials',data:trials})
                .success(function(result){
                    $scope.inProcess = false;
                    console.log(result);
                }).error(function(result){
                    console.log('error:' + result);
                    $scope.inProcess = false;
                });
        }

        $scope.open = function(pic){
            var modalInstance = $modal.open({
                templateUrl: 'preview.html',
                controller: ChangeBlindnessInstanceCtrl,
                size: 'lg',
                resolve: {
                    pic : function(){
                        return pic;
                    }
                }

            });
        };
        function initTrials(trials, questions){
            for(var i=0; i<trials.length; i++){
                var obj = {
                    trial: trials[i],
                    pictures: []
                }
                $scope.trials.push(obj);
            }
            console.log($scope.trials);
            initPictures(questions);
        }

        function initPictures(questions){
            for(var i=0; i<$scope.trials.length; i++){
                for(var j=0; j<questions.length; j++){
                    var obj = {
                        question: questions[j],
                        selected: isCurrentQuestion($scope.trials[i], questions[j])
                    }
                    $scope.trials[i].pictures.push(obj);
                }
            }

        }

        function isCurrentQuestion(obj, question){
            for(var i=0; i<obj.trial.quizzes.length; i++){
                if(obj.trial.quizzes[i].question.id == question.id){
                    return true;
                }
            }
            return false;
        }
    });

var ChangeBlindnessInstanceCtrl = function($scope, $rootScope, $modalInstance, pic) {

    $scope.pic = pic;

    $scope.ok = function () {
        $modalInstance.close();
    };
}

var GarnerSummaryInstanceCtrl = function($scope, $rootScope, $modalInstance, trials){

    $scope.trials = trials;

    $scope.ok = function () {
        $modalInstance.close();
    };

    $scope.cancel = function () {
        $modalInstance.dismiss();
    };

    $scope.showTextQuestion = function(quiz){
        if(quiz.questionType == 'COLOR')
            return quiz.question.colorQuestion == 'dark' ? 'สีเข้ม' : 'สีอ่อน';
        if(quiz.questionType == 'SIZE')
            return quiz.question.sizeQuestion == 'big' ? 'ขนาดใหญ่' : 'ขนาดเล็ก';
        if(quiz.questionType == 'BOTH')
            var text = quiz.question.colorQuestion == 'dark' ? 'สีเข้ม' : 'สีอ่อน';
            text += quiz.question.sizeQuestion == 'big' ? 'ขนาดใหญ่' : 'ขนาดเล็ก';
            return  text ;
    }

    $scope.showTextPicDisplay = function(quiz){
        var text = quiz.question.colorQuestion == 'dark' ? 'สีเข้ม' : 'สีอ่อน';
        text += quiz.question.sizeQuestion == 'big' ? 'ขนาดใหญ่' : 'ขนาดเล็ก';
        return  text ;
    }
    $scope.showAnswer = function(quiz){
        if(quiz.questionType == 'COLOR')
            return quiz.question.colorMatch ? 'glyphicon-ok' : 'glyphicon glyphicon-remove';
        if(quiz.questionType == 'SIZE')
            return quiz.question.sizeMatch ? 'glyphicon-ok' : 'glyphicon glyphicon-remove';
        if(quiz.questionType == 'BOTH')
            return quiz.question.colorMatch && quiz.question.sizeMatch ? 'glyphicon-ok' : 'glyphicon glyphicon-remove';
    }
}

var MullerSummaryInstanceCtrl = function($scope, $rootScope, $modalInstance, trials){

    $scope.trials = trials;

    $scope.ok = function () {
        $modalInstance.close();
    };

    $scope.cancel = function () {
        $modalInstance.dismiss();
    };

    $scope.calculateBackground = function(type, show){

        if(!show)
            return ''

        if(type == 'NONE')
            return 'active'
        if(type == 'LEFT')
            return 'success'
        if(type == 'RIGHT')
            return 'info'
        if(type == 'IN')
            return 'warning'
        if(type == 'OUT')
            return 'danger'

        return '';
    }
}

var MullerModalInstanceCtrl = function($scope, $rootScope, $modalInstance, quiz){

    var canvas = {};

    $scope.ok = function () {
        $modalInstance.close();
    };

    $scope.cancel = function () {
        $modalInstance.dismiss();
    };

    $scope.init = function(){

        canvas = document.getElementById("arrowPreview");
        var width = 800;
        var height = 400;
        var length = calculateLength(quiz);
        var x = (800/2) - (length/2);
        var y = 30;

        var question = quiz.question;
        drawLine(calculateStartX(1, x), y, calculateDifferenceLength(1,length), question.line1, 1);
        y += 70;
        drawLine(calculateStartX(2, x), y, calculateDifferenceLength(2,length), question.line2, 2);
        y += 70;
        drawLine(calculateStartX(3, x), y, calculateDifferenceLength(3,length), question.line3, 3);
        y += 70;
        if(quiz.noOfChoice > 3) {
            drawLine(calculateStartX(4, x), y, calculateDifferenceLength(4,length), question.line4, 4);
            y += 70;
        }
        if(quiz.noOfChoice > 4) {
            drawLine(calculateStartX(5, x), y, calculateDifferenceLength(5,length), question.line5, 5);
        }


    }

    $scope.shuffle = function(){
        var question = quiz.question;
        var array = [question.line1, question.line2, question.line3,
                    question.line4, question.line5];
        array = shuffle(array);
        question.line1 = array[0];
        question.line2 = array[1];
        question.line3 = array[2];
        question.line4 = array[3];
        question.line5 = array[4];

        quiz.differChoice = Math.floor((Math.random() * quiz.noOfChoice) + 1);

        canvas.width = canvas.width;
        $scope.init();
    }

    function shuffle(array) {
      var currentIndex = array.length
        , temporaryValue
        , randomIndex
        ;

      // While there remain elements to shuffle...
      while (0 !== currentIndex) {

        // Pick a remaining element...
        randomIndex = Math.floor(Math.random() * currentIndex);
        currentIndex -= 1;

        // And swap it with the current element.
        temporaryValue = array[currentIndex];
        array[currentIndex] = array[randomIndex];
        array[randomIndex] = temporaryValue;
      }

      return array;
    }

    function calculateDifferenceLength(no, length){
        if(!quiz.isPositive)
            return no == quiz.differChoice ? length - Math.floor((quiz.differLength * 38)) : length;
        else
            return no == quiz.differChoice ? length + Math.floor((quiz.differLength * 38)) : length;
    }
    function calculateStartX(no, x){
        if(!quiz.isPositive)
            return no == quiz.differChoice ? x + Math.floor((quiz.differLength * 38 / 2)) : x;
        else
            return no == quiz.differChoice ? x - Math.floor((quiz.differLength * 38 / 2)) : x;
    }

    function calculateLength(quiz){
        if(quiz.lengthType == 'SHORT'){
            return 300;
        }else if(quiz.lengthType == 'MEDIUM'){
            return 400;
        }else if(quiz.lengthType == 'LONG'){
            return 500;
        }
        return 300;
    }

    function drawLine(x, y, length, type, choice){
        var context = canvas.getContext("2d");
        context.beginPath();
        context.moveTo(x, y);
        drawLeftArrow(x, y, type, context);
        context.lineTo(x + length, y);
        drawRightArrow(x + length, y, type, context);
        context.closePath();
        var color = choice == quiz.differChoice ? 'blue' : 'black';
        context.strokeStyle = color;
        context.lineWidth=2;
        context.stroke();
    }

    function drawLeftArrow(x, y, type, context){
        if(type == 'LEFT' || type == 'IN'){
            context.lineTo(x + 30, y - 30);
            context.moveTo(x, y);
            context.lineTo(x + 30, y + 30);
            context.moveTo(x, y);
        }
        if(type == 'OUT'){
            context.lineTo(x - 30, y - 30);
            context.moveTo(x, y);
            context.lineTo(x - 30, y + 30);
            context.moveTo(x, y);
        }
    }
    function drawRightArrow(x, y, type, context){
        if(type == 'RIGHT' || type == 'IN'){
            context.lineTo(x - 30, y - 30);
            context.moveTo(x, y);
            context.lineTo(x - 30, y + 30);
        }
        if(type == 'OUT'){
            context.lineTo(x + 30, y - 30);
            context.moveTo(x, y);
            context.lineTo(x + 30, y + 30);
        }
    }
}

var ModalInstanceCtrl = function ($scope, $rootScope, $modalInstance, sharps, width, height, trial) {

    $scope.sharps = sharps;
    $scope.width = width;
    $scope.height = height;
    $scope.editSharp = {};
    $scope.floatPattern = /^[0-9]*\.?[0-9]+$/;
    $scope.sequence = 1;
    $scope.target = {};
    $scope.editSharp = {};
    $scope.target = null;


    var x_offset;
    var y_offset;


    $scope.generate = function () {
        generateSharps(trial);
    };
    $scope.ok = function () {
        trial.quiz.question.sharps = angular.toJson($scope.sharps);
        $modalInstance.close($scope.target);
    };

    $scope.cancel = function () {
        $modalInstance.dismiss();
    };
    $scope.colorToString = function(color){
        var colorText = '';
        switch (color){
            case 'red' : colorText='#A51B00'; break;
            case 'blue': colorText='#57BAC9';break;
            case 'green': colorText='#2ED2AE'; break;
            case 'error' : colorText="#FF0000";break;
        }
        return colorText;
    }

    $scope.init = function(){
        var row = Math.floor($scope.height / 30);
        var column = Math.floor($scope.width / 30);
        x_offset = Math.floor(100/column);
        y_offset = Math.floor(100/row);

        if(trial.quiz.target == 'CIRCLE_RED'){
            $scope.target = Sharp(trial.quiz.positionYofTarget, trial.quiz.positionXofTarget, 'red', 'circle');
        }
        else if(trial.quiz.target == 'CIRCLE_GREEN'){
            $scope.target = Sharp(trial.quiz.positionYofTarget, trial.quiz.positionXofTarget, 'green', 'circle');
        }
        else if(trial.quiz.target == 'SQAURE_BLUE'){
            $scope.target = Sharp(trial.quiz.positionYofTarget, trial.quiz.positionXofTarget, 'blue', 'square');
        }
        else if(trial.quiz.target == 'SQAURE_RED'){
            $scope.target = Sharp(trial.quiz.positionYofTarget, trial.quiz.positionXofTarget, 'red', 'square');
        }
        else if(trial.quiz.target == 'SQAURE_GREEN'){
            $scope.target = Sharp(trial.quiz.positionYofTarget, trial.quiz.positionXofTarget, 'green', 'square');
        }
        else if(trial.quiz.target == 'CIRCLE_BLUE'){
            $scope.target = Sharp(trial.quiz.positionYofTarget, trial.quiz.positionXofTarget, 'blue', 'circle');
        }
        $scope.sharps = angular.fromJson(trial.quiz.question.sharps);
    }

    var generateSharps = function(trial){
        $scope.sharps = [];
        if(trial.quiz.target != 'CIRCLE_RED'){
            for(var i = 0; i < trial.quiz.circleRed;i++){
                var obj = createSharp('red','circle');
                if(obj != null)
                    $scope.sharps.push(obj);
            }
        }
        if(trial.quiz.target != 'CIRCLE_GREEN'){
            for(var i = 0; i < trial.quiz.circleGreen;i++){
                var obj = createSharp('green','circle');
                if(obj != null)
                    $scope.sharps.push(obj);
            }
        }
        if(trial.quiz.target != 'SQAURE_BLUE'){
            for(var i = 0; i < trial.quiz.squareBlue;i++){
                var obj = createSharp('blue','square');
                if(obj != null)
                    $scope.sharps.push(obj);
            }
        }
        if(trial.quiz.target != 'SQAURE_RED'){
            for(var i = 0; i < trial.quiz.squareRed;i++){

                var obj = createSharp('red','square');
                if(obj != null)
                    $scope.sharps.push(obj);
            }
        }
        if(trial.quiz.target == 'SQAURE_GREEN'){
            for(var i = 0; i < trial.quiz.squareGreen;i++){

                var obj = createSharp('green','square');
                if(obj != null)
                    $scope.sharps.push(obj);
            }
        }
        if(trial.quiz.target == 'CIRCLE_BLUE'){
            for(var i = 0; i < trial.quiz.circleBlue; i++){

                var obj = createSharp('blue', 'circle');
                if(obj != null)
                    $scope.sharps.push(obj);
            }
        }
    }

    function createSharp(color, circle){
        var top = 0;
        var left = 0;

        for(var i=0; i<200; i++){
            top = Math.random() * (100 - y_offset);
            left = Math.random() * (100 - x_offset);
            if(!detectOtherCollission(top,left)){
                return Sharp(top,left,color,circle);
            }
        }

        return null;
    }

    function detectOtherCollission(top1, left1){
        var right1 = left1 + x_offset;
        var bottom1 = top1 + y_offset;
        for(var i=0; i < $scope.sharps.length; i++){
            if($scope.sharps[i] != null) {
                var top2 = $scope.sharps[i].top;
                var bottom2 = $scope.sharps[i].top + y_offset;
                var left2 = $scope.sharps[i].left;
                var right2 = $scope.sharps[i].left + x_offset;
                if (((top1 < top2 && top2 < bottom1) ||
                    (top1 < bottom2 && bottom2 < bottom1)) &&
                    ((left1 < left2 && left2 < right1) ||
                      (left1 < right2 && right2 < right1))){
                    return true;
                }
            }
        }
        return false;
    }
    


    


    var Sharp = function(top,left,color,sharp){
        var sharp = {
            id: $scope.sequence++,
            top: top,
            left: left,
            color: color,
            sharp: sharp
        }
        return sharp;
    }

    $scope.showSharp = function(sharp){
        $scope.editSharp = sharp;
        console.log($scope.editSharp);
    }
};
