var submitStatus = true;

var set;
var exportLink;
var exportId;

var prefixNameList=[];
var nameList=[];
var lastnameList=[];
var positionList=[];

var userAll=[];

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
     $scope.openDeleteDetailModal = function(link) {
         var cancelDetailModalInstance = $modal.open({
                 templateUrl : 'cancelList.html',
                 controller : cancelDetailModalCtrl,
                 size : 'lg'
         });
     };

         function combine() { 
            for(var i = 0; i < nameList.length ; i++){
                userAll[i] = prefixNameList[i]+" "+nameList[i]+" "+lastnameList[i]+" "+positionList[i] ;     
            }
         } 
        $scope.findUser=function(){
            $http({method : 'GET',url : 'autocompleteImportOrderCommitee' })
            .success(function(result){
                $scope.name= result.name;
                $scope.PrefixName = result.namePrefix;
                $scope.lastname= result.lastname;
                $scope.position= result.position;

                code= result.code;

                prefixNameList = $scope.PrefixName;
                nameList = $scope.name;
                lastnameList= $scope.lastname;
                positionList= $scope.position;
            
                combine();
                $(function() {
                    $( "#FF_firstName0" ).autocomplete({
                      source: userAll,
                      focus: function(event, ui) {
                          $("input#FF_firstName0").val(ui.item.label);
                      },
                      select: function(event, ui) {
                         $("#searchform button").click(); 
                         setTimeout(mapInputFF("FF_firstName0"),100);
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
                         setTimeout(setInput2,20);
                      }
                    });
                });
            });
        };

});
function mapInputFF(id){
    var temp = id[id.length-1];
    var id = document.getElementById(id).value ;
        for(var j = 0; j < userAll.length;j++){
            if(id == userAll[j]){
                document.getElementById("FF_namePrefix"+temp).value = prefixNameList[j];            
                document.getElementById("FF_firstName"+temp).value = nameList[j];            
                document.getElementById("FF_lastName"+temp).value = lastnameList[j];
                document.getElementById("FF_position"+temp).value = positionList[j];
            }
        }
}
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
