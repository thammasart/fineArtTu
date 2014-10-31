var submitStatus = true;


var exportLink;
var exportId;

 function addId(id) { 
    exportId = id;
 } 


angular.module('exportModule', ['ui.bootstrap'])
    .controller('exportDonateCtrl',function($scope,$http,$modal){
        
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

});
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
        }
    }
}
function submitButtonClick(){
    
    submitStatus = true;
    if(document.getElementById("title").value==""){
        document.getElementById("titleAlert").style.display = "table-row";
        submitStatus = false;
        document.addDonate.title.focus();
    }else  document.getElementById("titleAlert").style.display= "none";

    if(document.getElementById("contractNo").value ==""){
        document.getElementById("contractNoAlert").style.display = "table-row";
        submitStatus = false;
        document.addDonate.contractNo.focus();
    }else  document.getElementById("contractNoAlert").style.display= "none";

    if(document.getElementById("approveDate").value ==""){
        document.getElementById("approveDateAlert").style.display = "table-row";
        submitStatus = false;
        document.addDonate.approveDate.focus();
    }else  document.getElementById("approveDateAlert").style.display= "none";

    return submitStatus;
}
