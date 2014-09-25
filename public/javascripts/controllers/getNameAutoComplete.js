var submitStatus = true;
var name= [];
var lastname= [];
var position= [];

var desId;
angular.module('userAccountModule', ['ui.bootstrap'])
    .controller('autoCompleteUserController',function($scope,$http){
        
        $scope.name= [];
        $scope.lastname= [];
        $scope.position= [];

        $scope.findUser=function(){
            $http({method : 'GET',url : 'autocompleteExportCommitee' })
            .success(function(result){
                $scope.name= result.name;
                $scope.lastname= result.lastname;
                $scope.position= result.position;
                name = $scope.name ;
                lastname= $scope.lastname;
                position= $scope.position;
                
                console.log(name);
                console.log(lastname);
                console.log(position);

                $(function() {
                    $( "#firstName" ).autocomplete({
                      source: name
                    });
                });
                $(function() {
                    $( "#approverName" ).autocomplete({
                      source: name
                    });
                });
                $(function() {
                        $( "#lastName" ).autocomplete({
                              minLength:2,
                              source: lastname
                        });
                });
                $(function() {
                        $( "#approverLastName" ).autocomplete({
                              minLength:2,
                              source: lastname
                        });
                });

                $(function() {
                        $( "#position" ).autocomplete({
                              minLength:2,
                              source: position
                        });
                });
                $(function() {
                        $( "#approverPosition" ).autocomplete({
                              minLength:2,
                              source: position
                        });
                });

            });
        };
    }
);
