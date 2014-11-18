var submitStatus = true;
var nameList= [];
var lastnameList= [];
var positionList= [];
var userAll= [] ;
var code = [];
var codeName = [];

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
            $http({method : 'GET',url : 'autocompleteExportCommitee' })
            .success(function(result){
                $scope.name= result.name;
                $scope.lastname= result.lastname;
                $scope.position= result.position;

                code= result.code;
                codeName= result.codeName;
                nameList = $scope.name ;
                lastnameList= $scope.lastname;
                positionList= $scope.position;
            
                combine();

                $(function() {
                    $( "#code" ).autocomplete({
                      minLength:2,
                      source: code,
                      focus: function(event, ui) {
                          $("input#code").val(ui.item.label);
                        mapCodeToCodeName();
                      },
                      select: function(event, ui) {
                         $("#searchform button").click(); 
                         setTimeout(mapCodeToCodeName,100);
                      }
                    });
                });
                $(function() {
                    $( "#codeEdit" ).autocomplete({
                      minLength:2,
                      source: code,
                      focus: function(event, ui) {
                          $("input#codeEdit").val(ui.item.label);
                        mapCodeToCodeNameEdit();
                      },
                      select: function(event, ui) {
                         $("#searchform button").click(); 
                         setTimeout(mapCodeToCodeNameEdit,100);
                      }
                    });
                });
                $(function() {
                    $( "#groupCode" ).autocomplete({
                      minLength:3,
                      source: codeName,
                      focus: function(event, ui) {
                          $("input#groupCode").val(ui.item.label);
                        mapCodeNameToCode();
                      },
                      select: function(event, ui) {
                         $("#searchform button").click(); 
                         setTimeout(mapCodeNameToCode,100);
                      }
                    });
                });
                $(function() {
                    $( "#groupCodeEdit" ).autocomplete({
                      minLength:3,
                      source: codeName,
                      focus: function(event, ui) {
                          $("input#groupCodeEdit").val(ui.item.label);
                        mapCodeNameToCodeEdit();
                      },
                      select: function(event, ui) {
                         $("#searchform button").click(); 
                         setTimeout(mapCodeNameToCodeEdit,100);
                      }
                    });
                });
                $(function() {
                    $( "#firstName" ).autocomplete({
                      source: userAll,
                      focus: function(event, ui) {
                          $("input#firstName").val(ui.item.label);
                      },
                      select: function(event, ui) {
                         $("#searchform button").click(); 
                         setTimeout(mapInput,100);
                      }
                    });
                });
                $(function() {
                    $( "#approverName" ).autocomplete({
                      source: userAll,
                      focus: function(event, ui) {
                          $("input#approverName").val(ui.item.label);
                      },
                      select: function(event, ui) {
                         $("#searchform button").click(); 
                         setTimeout(mapInput2,100);
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
function radioClick(rValue){
    document.getElementById("code").value = rValue;
}
function mapCodeNameToCodeEdit(){
    var id = document.getElementById("groupCodeEdit").value ;
    for(var j = 0; j <code.length;j++){
        if(id == codeName[j]){
            document.getElementById("codeEdit").value = code[j];            
        }
    }
}
function mapCodeToCodeNameEdit(){
    var id = document.getElementById("codeEdit").value ;
    for(var j = 0; j <code.length;j++){
        if(id ==code[j]){
            document.getElementById("groupCodeEdit").value = codeName[j];            
        }
    }
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
          source: userAll,
          focus: function(event, ui) {
              $("input#withdrawer").val(ui.item.label);
            mapInput3();
          },
          select: function(event, ui) {
             $("#searchform button").click(); 
             setTimeout(mapInput3,100);
          }
    });
});
$(function() {
    $( "#recieveFirstName" ).autocomplete({
          source: userAll,
          focus: function(event, ui) {
              $("input#recieveFirstName").val(ui.item.label);
            mapInput5();
          },
          select: function(event, ui) {
             $("#searchform button").click(); 
             setTimeout(mapInput5,100);
          }
    });
});
$(function() {
    $( "#edit_firstName" ).autocomplete({
          source: userAll,
          focus: function(event, ui) {
              $("input#edit_firstName").val(ui.item.label);
            mapInput6();
          },
          select: function(event, ui) {
             $("#searchform button").click(); 
             setTimeout(mapInput6,100);
          }
    });
});
$(function() {
    $( "#withdrawerEdit" ).autocomplete({
          source: userAll,
          focus: function(event, ui) {
              $("input#withdrawerEdit").val(ui.item.label);
            mapInput4();
          },
          select: function(event, ui) {
             $("#searchform button").click(); 
             setTimeout(mapInput4,100);
          }
    });
});
var set3;
var set4;
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
function mapInput4(){
    var id = document.getElementById("withdrawerEdit").value ;
    for(var j = 0; j < userAll.length;j++){
        if(id == userAll[j]){
            document.getElementById("withdrawerEdit").value = nameList[j];            
            document.getElementById("withdrawerLastnameEdit").value = lastnameList[j];
            document.getElementById("withdrawerPositionEdit").value = positionList[j];
        }
    }
}
function mapInput5(){
    var id = document.getElementById("recieveFirstName").value ;
    for(var j = 0; j < userAll.length;j++){
        if(id == userAll[j]){
            document.getElementById("recieveFirstName").value = nameList[j];            
            document.getElementById("recieveLastName").value = lastnameList[j];
            document.getElementById("recievePosition").value = positionList[j];
        }
    }
}
function mapInput6(){
    var id = document.getElementById("edit_firstName").value ;
    for(var j = 0; j < userAll.length;j++){
        if(id == userAll[j]){
            document.getElementById("edit_firstName").value = nameList[j];            
            document.getElementById("edit_lastName").value = lastnameList[j];
            document.getElementById("edit_position").value = positionList[j];
        }
    }
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
