var submitStatus = true;
var nameList= [];
var lastnameList= [];
var positionList= [];
var userAll= [] ;

var exportLink;
var exportId;
var autoNameTemp;
var desId;

function addId(id) { 
   exportId = id;
} 

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
                    $( "#recieveFirstName" ).autocomplete({
                      source: userAll,
                      focus: function(event, ui) {
                          $("input#recieveFirstName").val(ui.item.label);
                      },
                      select: function(event, ui) {
                         $("#searchform button").click(); 
                         setTimeout(mapInput1,100);
                      }
                    });
                });
                $(function() {
                    $( "#approverFirstName" ).autocomplete({
                      source: userAll,
                      focus: function(event, ui) {
                          $("input#approverFirstName").val(ui.item.label);
                          mapInput2();
                      },
                      select: function(event, ui) {
                         $("#searchform button").click(); 
                         setTimeout(mapInput2,20);
                      }
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
            autoNameTemp = nameList[j];
        }
    }
}

