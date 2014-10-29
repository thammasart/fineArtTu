var submitStatus ;

var fsnName=[];
var fsnCode=[];

var pathToRemove;
var autoCompleteNum;
var keyId;

var desId;
angular.module('fsnAutoComplete', ['ui.bootstrap'])
    .controller('getFsnName',function($scope,$http,$modal){
        
        $scope.fsnName=[];
        $scope.fsnCode=[];

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
function submitButtonClick(){
    
    submitStatus = true;
    return submitStatus;
}
