var submitStatus = true;
var nameList= [];
var lastnameList= [];
var positionList= [];
var userAll= [] ;

var exportLink;
var exportId;

 function addId(id) { 
    exportId = id;
 } 

var desId;
angular.module('userAccountModule', ['ui.bootstrap'])
    .controller('autoCompleteUserController',function($scope,$http,$modal){
        
        $scope.name= [];
        $scope.lastname= [];
        $scope.position= [];

         $scope.openCancelModal = function(link) {
             var cancelModalInstance = $modal.open({
                     templateUrl : 'cancelExportAdd.html',
                     controller : cancelModalInstanceCtrl,
                     size : 'lg'
             });
            exportLink = link;
         };

         function combine() { 
            for(var i = 0; i < nameList.length ; i++){
                userAll[i] = nameList[i]+" "+lastnameList[i]+" "+positionList[i] ;     
            }
         } 
        $scope.findUser=function(){
            $http({method : 'GET',url : 'autocompleteRepairCommitee' })
            .success(function(result){
                $scope.name= result.name;
                $scope.lastname= result.lastname;
                $scope.position= result.position;

                nameList = $scope.name ;
                lastnameList= $scope.lastname;
                positionList= $scope.position;
            
                combine();

                $(function() {
                    $( "#recieveFirstName" ).autocomplete({
                      source: userAll
                    });
                });
                $(function() {
                    $( "#approverFirstName" ).autocomplete({
                      source: userAll
                    });
                });
            });
        };
    }
);
var cancelModalInstanceCtrl = function($scope, $modalInstance){

   $scope.ok = function () {
        window.location.href = exportLink + "?id="+exportId ;
        $modalInstance.close();
    };

    $scope.cancel = function () {
        $modalInstance.dismiss();
    };
}
function mapInput1(){
    var id = document.getElementById("recieveFirstName").value ;
    for(var j = 0; j < userAll.length;j++){
        if(id == userAll[j]){
            document.getElementById("recieveFirstName").value = nameList[j];            
            document.getElementById("recieveLastName").value = lastnameList[j];
            document.getElementById("recievePosition").value = positionList[j];
        }
    }
}
function mapInput2(){
    var id = document.getElementById("approverFirstName").value ;
    for(var j = 0; j < userAll.length;j++){
        if(id == userAll[j]){
            document.getElementById("approverFirstName").value = nameList[j];            
            document.getElementById("approverLastName").value = lastnameList[j];
            document.getElementById("approverPosition").value = positionList[j];
        }
    }
}

