var submitStatus ;

var fsnName=[];
var fsnCode=[];

var desId;
angular.module('fsnAutoComplete', ['ui.bootstrap'])
    .controller('getFsnName',function($scope,$http){
        
        $scope.fsnName=[];
        $scope.fsnCode=[];

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
function submitButtonClick(){
    
    submitStatus = true;
    return submitStatus;
}
