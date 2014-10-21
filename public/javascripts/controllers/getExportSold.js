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
    }else  document.getElementById("titleAlert").style.display= "none";

    if(document.getElementById("number").value ==""){
        document.getElementById("numberAlert").style.display = "table-row";
        submitStatus = false;
    }else  document.getElementById("numberAlert").style.display= "none";

    if(document.getElementById("date").value ==""){
        document.getElementById("dateAlert").style.display = "table-row";
        submitStatus = false;
    }else  document.getElementById("dateAlert").style.display= "none";

    if(document.getElementById("firstName").value==""){
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

    if(document.getElementById("approverName").value==""){
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
