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
                         setTimeout(mapInputCommittee("FF",0),200);
                      }
                    });
                });
                $(function() {
                    $( "#E_firstName0" ).autocomplete({
                      source: userAll,
                      focus: function(event, ui) {
                          $("input#E_firstName0").val(ui.item.label);
                      },
                      select: function(event, ui) {
                         $("#searchform button").click(); 
                         setTimeout(mapInputCommittee("E",0),200);
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
