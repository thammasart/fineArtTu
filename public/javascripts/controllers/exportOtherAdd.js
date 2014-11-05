var submitStatus = true;
var nameList= [];
var prefixNameList=[];
var lastnameList= [];
var positionList= [];
var userAll= [] ;
var userAllP= [] ;
var code = [];
var codeName = [];

var exportLink;
var exportId;

 function addId(id) { 
    exportId = id;
 } 
var desId;
angular.module('exportOtherApp', ['ui.bootstrap'])
    .controller('exportOtherCtrl',function($scope,$http,$modal){
        
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
                userAllP[i] = prefixNameList[i] + " " +  nameList[i]+" "+lastnameList[i]+" "+positionList[i] ;     
            }
         } 
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
                    $( "#recieveFirstName" ).autocomplete({
                      source: userAll,
                      focus: function(event, ui) {
                          $("input#recieveFirstName").val(ui.item.label);
                          mapInput1();
                      },
                      select: function(event, ui) {
                         $("#searchform button").click(); 
                         setTimeout(mapInput1,200);
                      }
                    });
                });
                $(function() {
                    $( "#approverName" ).autocomplete({
                      source: userAll,
                      focus: function(event, ui) {
                          $("input#approverName").val(ui.item.label);
                          mapInput2();
                      },
                      select: function(event, ui) {
                         $("#searchform button").click(); 
                         setTimeout(mapInput2,200);
                      }
                    });
                });
            });
        };
    }
);
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
function mapCodeNameToCode(){
    var id = document.getElementById("groupCode").value ;
    for(var j = 0; j <code.length;j++){
        if(id == codeName[j]){
            document.getElementById("code").value = code[j];            
        }
    }
}
function mapCodeToCodeName(){
    var id = document.getElementById("code").value ;
    for(var j = 0; j <code.length;j++){
        if(id ==code[j]){
            document.getElementById("groupCode").value = codeName[j];            
        }
    }
}
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

//function submitButtonClick(){
//    
//    submitStatus = true;
//    if(document.getElementById("title").value==""){
//        document.getElementById("titleAlert").style.display = "table-row";
//        submitStatus = false;
//    }else  document.getElementById("titleAlert").style.display= "none";
//
//    if(document.getElementById("number").value ==""){
//        document.getElementById("numberAlert").style.display = "table-row";
//        submitStatus = false;
//    }else  document.getElementById("numberAlert").style.display= "none";
//
//    if(document.getElementById("date").value ==""){
//        document.getElementById("dateAlert").style.display = "table-row";
//        submitStatus = false;
//    }else  document.getElementById("dateAlert").style.display= "none";
//
//    if(document.getElementById("firstName").value==""){
//        document.getElementById("firstNameAlert").style.display = "table-row";
//        submitStatus = false;
//    }else  document.getElementById("firstNameAlert").style.display= "none";
//
//    if(document.getElementById("lastName").value ==""){
//        document.getElementById("lastNameAlert").style.display = "table-row";
//        submitStatus = false;
//    }else  document.getElementById("lastNameAlert").style.display= "none";
//
//    if(document.getElementById("position").value ==""){
//        document.getElementById("positionAlert").style.display = "table-row";
//        submitStatus = false;
//    }else  document.getElementById("positionAlert").style.display= "none";
//
//    if(document.getElementById("approverName").value==""){
//        document.getElementById("approverNameAlert").style.display = "table-row";
//        submitStatus = false;
//    }else  document.getElementById("approverNameAlert").style.display= "none";
//
//    if(document.getElementById("approverLastName").value ==""){
//        document.getElementById("approverLastNameAlert").style.display = "table-row";
//        submitStatus = false;
//    }else  document.getElementById("approverLastNameAlert").style.display= "none";
//
//    if(document.getElementById("approverPosition").value ==""){
//        document.getElementById("approverPositionAlert").style.display = "table-row";
//        submitStatus = false;
//    }else  document.getElementById("approverPositionAlert").style.display= "none";
//
//    return submitStatus;
//}
