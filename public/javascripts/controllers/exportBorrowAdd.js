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
         $scope.openDeleteDetailModal = function(link) {
             var cancelDetailModalInstance = $modal.open({
                     templateUrl : 'cancelList.html',
                     controller : cancelDetailModalCtrl,
                     size : 'lg'
             });
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
                    $( "#withdrawerNmae" ).autocomplete({
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
var cancelModalInstanceCtrl = function($scope, $modalInstance){

   $scope.ok = function () {
        window.location.href = exportLink + "?id="+exportId ;
        $modalInstance.close();
    };

    $scope.cancel = function () {
        $modalInstance.dismiss();
    };
}
var cancelDetailModalCtrl= function($scope, $modalInstance){

   $scope.ok = function () {
        deleteDetail();
        $modalInstance.close();
    };

    $scope.cancel = function () {
        $modalInstance.dismiss();
    };
}
function mapInput1(){
    var id = document.getElementById("withdrawerNmae").value ;
    for(var j = 0; j < userAll.length;j++){
        if(id == userAll[j]){
            document.getElementById("withdrawerNmae").value = nameList[j];            
            document.getElementById("withdrawerLastname").value = lastnameList[j];
            document.getElementById("withdrawerPosition").value = positionList[j];
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

