var submitStatus = true;


var exportLink;
var exportId;

 function addId(id) { 
    exportId = id;
 } 


angular.module('exportModule', ['ui.bootstrap'])
    .controller('exportSoldCtrl',function($scope,$http,$modal){
        
     $scope.openCancelModal = function(link) {
         var cancelModalInstance = $modal.open({
                 templateUrl : 'cancelExportAdd.html',
                 controller : cancelModalInstanceCtrl,
                 size : 'lg'
         });
        exportLink = link;
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
function submitButtonClick(){
    
    submitStatus = true;
    if(document.getElementById("title").value==""){
        document.getElementById("titleAlert").style.display = "table-row";
        submitStatus = false;
        document.addSold.title.focus();
    }else  document.getElementById("titleAlert").style.display= "none";

    if(document.getElementById("contractNo").value ==""){
        document.getElementById("contractNoAlert").style.display = "table-row";
        submitStatus = false;
        document.addSold.contractNo.focus();
    }else  document.getElementById("contractNoAlert").style.display= "none";

    if(document.getElementById("approveDate").value ==""){
        document.getElementById("approveDateAlert").style.display = "table-row";
        submitStatus = false;
        document.addSold.approveDate.focus();
    }else  document.getElementById("approveDateAlert").style.display= "none";

    if(document.getElementById("soldDestination").value==""){
        document.getElementById("soldDestinationAlert").style.display = "table-row";
        submitStatus = false;
        document.addSold.soldDestination.focus();
    }else  document.getElementById("soldDestinationAlert").style.display= "none";

    return submitStatus;
}
