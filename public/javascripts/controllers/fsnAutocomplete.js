var submitStatus ;

var fsnName=[];
var fsnCode=[];


var pathToRemove;
var autoCompleteNum;

var keyId;
var keyIdEo;

var prefixNameList= [];
var nameList= [];
var lastnameList= [];
var positionList= [];
var userAll=[];

var desId;
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
        $scope.findFsn=function(){
            $http({method : 'GET',url : 'autoCompleteFsn' })
            .success(function(result){

                fsnName = result.fsnName;
                fsnCode = result.fsnCode;

                
                $(function() {
                    $( "#description" ).autocomplete({
                      minLength:2,
                      source: fsnName
                    });
                });
                $(function() {
                    $( "#code" ).autocomplete({
                      minLength:2,
                      source: fsnCode
                    });
                });
            });
        };
    }
);

var resultModalInstanceCtrl= function($scope, $modalInstance){

   $scope.name = " ลบรายการสัสดุ ";

   $scope.ok = function () {
        removeProcurementDetail(pathToRemove);
        $modalInstance.close();
    };

    $scope.cancel = function () {
        $modalInstance.dismiss();
    };
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
function initAutoCompleteName () { 
    keyId = "aiFirstName"+autoCompleteNum;
    $(function() {
        $('#'+keyId).autocomplete({
          source: userAll
        });
    })
    console.log(keyId);
} 
function initAutoCompleteNameEo () { 
    keyIdEo = "eoFirstName"+autoCompleteNum;
    $(function() {
        $('#'+keyIdEo).autocomplete({
          source: userAll
        });
    })
    console.log(keyIdEo);
} 
function mapInput(id){
    var temp = id[id.length-1];
    var type = id[0];
    var id = document.getElementById(id).value ;
    if(type =="a"){
        for(var j = 0; j < userAll.length;j++){
            if(id == userAll[j]){
                document.getElementById("aiPrefixName"+temp).value = prefixNameList[j];            
                document.getElementById("aiFirstName"+temp).value = nameList[j];            
                document.getElementById("aiLastName"+temp).value = lastnameList[j];
                document.getElementById("aiPosition"+temp).value = positionList[j];
            }
        }
    }else {
        for(var j = 0; j < userAll.length;j++){
            if(id == userAll[j]){
                document.getElementById("eoPrefixName"+temp).value = prefixNameList[j];            
                document.getElementById("eoFirstName"+temp).value = nameList[j];            
                document.getElementById("eoLastName"+temp).value = lastnameList[j];
                document.getElementById("eoPosition"+temp).value = positionList[j];
            }
        }
    }
}
function submitButtonClick(){
    
    submitStatus = true;
    return submitStatus;
}
