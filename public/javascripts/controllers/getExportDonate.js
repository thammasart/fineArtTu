var submitStatus = true;

var prefixNameList=[];
var nameList=[];
var lastnameList=[];
var positionList=[];

var userAll=[];

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
            userAll[i] =prefixNameList[i] + " " + nameList[i]+" "+lastnameList[i]+" "+positionList[i] ;     
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
                         setTimeout(mapInputCommittee("FF",0),200);
                      }
                    });
                });
                $(function() {
                    $( "#D_firstName0" ).autocomplete({
                      source: userAll,
                      focus: function(event, ui) {
                          $("input#D_firstName0").val(ui.item.label);
                      },
                      select: function(event, ui) {
                         $("#searchform button").click(); 
                         setTimeout(mapInputCommittee("D",0),200);
                      }
                    });
                });
            });
        };

});

function initAutocomplete(key,number) { 
       $(function() {
           $( "#"+key+"_firstName"+number ).autocomplete({
             source: userAll,
             focus: function(event, ui) {
                 $("input#"+key+"_firstName"+number).val(ui.item.label);
             },
             select: function(event, ui) {
                $("#searchform button").click(); 
                setTimeout(mapInputCommittee(key,number),100);
             }
           });
       });
} 
function mapInputCommittee(key,num){
    var id = document.getElementById(key+"_firstName"+num).value ;
        for(var j = 0; j < userAll.length;j++){
            if(id == userAll[j]){
                document.getElementById(key+"_namePrefix"+num).value = prefixNameList[j];            
                document.getElementById(key+"_lastName"+num).value = lastnameList[j];
                document.getElementById(key+"_position"+num).value = positionList[j];
                document.getElementById(key+"_firstName"+num).value = nameList[j];            
                setTimeout(function(){ document.getElementById(key+"_firstName"+num).value = nameList[j];},500);
                break;
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
