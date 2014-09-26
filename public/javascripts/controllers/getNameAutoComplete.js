var submitStatus = true;
var nameList= [];
var lastnameList= [];
var positionList= [];
var userAll= [] ;

var desId;
angular.module('userAccountModule', ['ui.bootstrap'])
    .controller('autoCompleteUserController',function($scope,$http){
        
        $scope.name= [];
        $scope.lastname= [];
        $scope.position= [];

         function combine() { 
            for(var i = 0; i < nameList.length ; i++){
                userAll[i] = nameList[i]+" "+lastnameList[i]+" "+positionList[i] ;     
            }
         } 
        $scope.findUser=function(){
            $http({method : 'GET',url : 'autocompleteExportCommitee' })
            .success(function(result){
                $scope.name= result.name;
                $scope.lastname= result.lastname;
                $scope.position= result.position;
                nameList = $scope.name ;
                lastnameList= $scope.lastname;
                positionList= $scope.position;
            
                combine();

                $(function() {
                    $( "#firstName" ).autocomplete({
                      source: userAll
                    });
                });
                $(function() {
                    $( "#approverName" ).autocomplete({
                      source: userAll
                    });
                });
            });
        };
    }
);

function mapInput(){
    var id = document.getElementById("firstName").value ;
    for(var j = 0; j < userAll.length;j++){
        if(id == userAll[j]){
            document.getElementById("firstName").value = nameList[j];            
            document.getElementById("lastName").value = lastnameList[j];
            document.getElementById("position").value = positionList[j];
        }
    }
}
function mapInput2(){
    var id = document.getElementById("approverName").value ;
    for(var j = 0; j < userAll.length;j++){
        if(id == userAll[j]){
            document.getElementById("approverName").value = nameList[j];            
            document.getElementById("approverLastName").value = lastnameList[j];
            document.getElementById("approverPosition").value = positionList[j];
        }
    }
}
$(function() {
    $( "#withdrawer" ).autocomplete({
      source: userAll
    });
});
function mapInput3(){
    var id = document.getElementById("withdrawer").value ;
    for(var j = 0; j < userAll.length;j++){
        if(id == userAll[j]){
            document.getElementById("withdrawer").value = nameList[j];            
            document.getElementById("withdrawerLastname").value = lastnameList[j];
            document.getElementById("withdrawerPosition").value = positionList[j];
        }
    }
}

function submitButtonClick(){
    
    submitStatus = true;
    if(document.getElementById("title").value.length ==""){
        document.getElementById("titleAlert").style.display = "table-row";
        submitStatus = false;
    }else  document.getElementById("titleAlert").style.display= "none";

    if(document.getElementById("number").value ==""){
        document.getElementById("numberAlert").style.display = "table-row";
        submitStatus = false;
    }else  document.getElementById("numberAlert").style.display= "none";

    if(document.getElementById("date").value ==""){
        document.getElementById("dateAlert").style.display = "table-row";
        submitStatus = false;
    }else  document.getElementById("dateAlert").style.display= "none";

    if(document.getElementById("firstName").value.length ==""){
        document.getElementById("firstNameAlert").style.display = "table-row";
        submitStatus = false;
    }else  document.getElementById("firstNameAlert").style.display= "none";

    if(document.getElementById("lastName").value ==""){
        document.getElementById("lastNameAlert").style.display = "table-row";
        submitStatus = false;
    }else  document.getElementById("lastNameAlert").style.display= "none";

    if(document.getElementById("position").value ==""){
        document.getElementById("positionAlert").style.display = "table-row";
        submitStatus = false;
    }else  document.getElementById("positionAlert").style.display= "none";

    if(document.getElementById("approverName").value.length ==""){
        document.getElementById("approverNameAlert").style.display = "table-row";
        submitStatus = false;
    }else  document.getElementById("approverNameAlert").style.display= "none";

    if(document.getElementById("approverLastName").value ==""){
        document.getElementById("approverLastNameAlert").style.display = "table-row";
        submitStatus = false;
    }else  document.getElementById("approverLastNameAlert").style.display= "none";

    if(document.getElementById("approverPosition").value ==""){
        document.getElementById("approverPositionAlert").style.display = "table-row";
        submitStatus = false;
    }else  document.getElementById("approverPositionAlert").style.display= "none";

    return submitStatus;
}
