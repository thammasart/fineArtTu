var submitStatus ;

var fsnName=[];
var fsnCode=[];


var pathToRemove;
var pathToEdit;
var autoCompleteNum;

var keyId;
var keyIdEo;

var prefixNameList= [];
var nameList= [];
var lastnameList= [];
var positionList= [];
var userAll=[];

var desId;

var procumentDetailsTickTemp;
angular.module('fsnAutoComplete', ['ui.bootstrap'])
    .controller('getFsnName',function($scope,$http,$modal){
        
        $scope.fsnName=[];
        $scope.fsnCode=[];

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
            
                console.log(prefixNameList);
                combine();
            });
        };
        $scope.openCancleDel = function(path){
            pathToRemove = path;
            var modalInstance = $modal.open({
                templateUrl: 'cancelList.html',
                controller: resultModalInstanceCtrl,
                size: 'lg',
                resolve: {
                    name : function(){
                        return $scope.name;
                    }
                }
            });
        };
        $scope.openEdit = function(path){
            pathToEdit = path;

            if(document.getElementById("b2").innerHTML.indexOf("แก้ไข") >-1){
                var modalInstance = $modal.open({
                    templateUrl: 'confirmEdit.html',
                    controller: editModalInstanceCtrl,
                    size: 'lg',
                    resolve: {
                        name : function(){
                            return $scope.name;
                        }
                    }
                });
            }else{
                showPage("1");
                submitDetail(pathToEdit);
            }
        };
        $scope.findFsn=function(){
            $http({method : 'GET',url : 'autoCompleteFsn' })
            .success(function(result){

                fsnName = result.fsnName;
                fsnCode = result.fsnCode;

                
                $(function() {
                    $( "#description" ).autocomplete({
                      minLength:2,
                      source: fsnName,
                      focus: function(event, ui) {
                          $("input#description").val(ui.item.label);
                          mapDesToCode();
                      },
                      select: function(event, ui) {
                         $("#searchform button").click(); 
                         setTimeout(mapDesToCode,200);
                      }
                    });
                });
                $(function() {
                    $( "#code" ).autocomplete({
                      minLength:2,
                      source: fsnCode,
                      focus: function(event, ui) {
                          $("input#code").val(ui.item.label);
                          mapCodeToDes();
                      },
                      select: function(event, ui) {
                         $("#searchform button").click(); 
                         setTimeout(mapCodeToDes,200);
                      }
                    });
                });
            });
        };
    }
);

var editModalInstanceCtrl= function($scope, $modalInstance){

   $scope.name = " แก้ไข ";
 
   $scope.ok = function () {
        showPage("1");
        submitDetail(pathToEdit);
        $modalInstance.close();
    };

    $scope.cancel = function () {
        $modalInstance.dismiss();
    };
}
var resultModalInstanceCtrl= function($scope, $modalInstance){
   $scope.name = " ลบรายการวัสดุ ";
   $scope.tick = procumentDetailsTickTemp;
 
   $scope.ok = function () {
        removeProcurementDetail(pathToRemove);
        $modalInstance.close();
    };

    $scope.cancel = function () {
        $modalInstance.dismiss();
    };
}

function getProcumentDetailsTick(tick){
    procumentDetailsTickTemp = tick ;
    console.log(procumentDetailsTickTemp);
}
 function mapDesToCode() { 
    var id = document.getElementById("description").value;

    for(var i = 0; i < fsnCode.length ;i++){ 
        if(id == fsnName[i]){
            document.getElementById("code").value = fsnCode[i];
        }
    } 
 } 
 function mapCodeToDes() { 
    var id = document.getElementById("code").value;

    for(var i = 0; i < fsnCode.length ;i++){ 
        if(id == fsnCode[i]){
            document.getElementById("description").value = fsnName[i];
        }
    } 
 } 
function setI(num){
   autoCompleteNum = num; 
}
function initAutoCompleteNameEo () { 
    keyIdEo = "eoFirstName"+autoCompleteNum;
    $(function() {
        $('#'+keyIdEo).autocomplete({
              source: userAll,
              /*focus: function(event, ui) {
                  //$("input#"+$(this).attr('id')).val(ui.item.label);
              },*/
              select: function(event, ui) {
                 $("#searchform button").click();
                 $("input#"+$(this).attr('id')).val(ui.item.label);
                 setTimeout(mapInput($(this).attr('id'),99),200);
                 //setTimeout(mapInput(keyIdEo,99),200);
              },
        });
    })
    //console.log("keyEo="+keyIdEo);
} 
function initAutoCompleteName () { 
    keyId = "aiFirstName"+autoCompleteNum;
    $(function() {
        $('#'+keyId).autocomplete({
              source: userAll,
              /*focus: function(event, ui) {
                  $("input#"+$(this).attr('id')).val(ui.item.label);
              },*/
              select: function(event, ui) {
                 $("#searchform button").click(); 
                 $("input#"+$(this).attr('id')).val(ui.item.label);
                 setTimeout(mapInput($(this).attr('id'),88),200);
              }
        });
    })
    console.log(keyId);
} 
function mapAi(j,temp){
        setTimeout(function(){document.getElementById("aiFirstName"+temp).value = nameList[j];},200);
}
function mapEo(j,temp){
        setTimeout(function(){document.getElementById("eoFirstName"+temp).value = nameList[j];},200);
}
        
function mapInput(id,form){
    var temp = id.substr(11);
    var type = id[0];
    console.log("temp="+temp+"  type="+type+"  id="+id + " form"+form); 
    var id = document.getElementById(id).value ;
    if(type =="a"){
        for(var j = 0; j < userAll.length;j++){
            if(id == userAll[j]){
                document.getElementById("aiPrefixName"+temp).value = prefixNameList[j];            
                console.log("aiFirstName"+temp);
                document.getElementById("aiFirstName"+temp).value = nameList[j];            
                document.getElementById("aiLastName"+temp).value = lastnameList[j];
                document.getElementById("aiPosition"+temp).value = positionList[j];
                mapAi(j,temp);
            }
        }
    }else {
        for(var j = 0; j < userAll.length;j++){
            if(id == userAll[j]){
                document.getElementById("eoPrefixName"+temp).value = prefixNameList[j];            
                document.getElementById("eoFirstName"+temp).value = nameList[j];            
                document.getElementById("eoLastName"+temp).value = lastnameList[j];
                document.getElementById("eoPosition"+temp).value = positionList[j];
                mapEo(j,temp);
            }
        }
    }
}
function submitButtonClick(){
    
    submitStatus = true;
    return submitStatus;
}
