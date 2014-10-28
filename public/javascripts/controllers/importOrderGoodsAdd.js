var submitStatus = true;
var submitNext = true;

var goodsCode=[];
var goodsName=[];
var goodsCodeFsn=[];
var goodsNameFsn=[];
var sourceCode=[];
var sourceName=[];
var pathToRemove;

angular.module('goodsCodeModule', ['ui.bootstrap'])
    .controller('autoCompleteGoods',function($scope,$http,$modal){
        
        $scope.goodsCode =[];
        $scope.goodsName =[];
        
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
        $scope.radioClick =  function(){ 
                document.getElementById("typeOfGoods").value = document.querySelector('input[name="goodsRadio"]:checked').value;
                    if(document.getElementById("typeOfGoods").value == 0){
                        sourceCode = goodsCode;
                        sourceName = goodsName;
                    }else{
                        sourceCode = goodsCodeFsn;
                        sourceName = goodsNameFsn;
                    }
                document.getElementById("description").disabled = false;
                document.getElementById("code").disabled = false;
                document.getElementById("description").value = "";
                document.getElementById("code").value = "";

                $(function() {
                    $( "#description" ).autocomplete({
                      minLength:2,
                      source: sourceName
                    });
                });
                $(function() {
                    $( "#code" ).autocomplete({
                      minLength:2,
                      source: sourceCode
                    });
                });
             };

        $scope.findGoods = function(){
            $http({method : 'GET',url : 'autoCompleteGood' })
            .success(function(result){
                
            goodsCode = result.goodsCode;
            goodsName = result.goodsName;
            goodsCodeFsn = result.goodsCodeFsn;
            goodsNameFsn = result.goodsNameFsn;

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
function mapDescriptionToCode(){
    var id = document.getElementById("description").value ;
    if(document.getElementById("typeOfGoods").value == 0){
        for(var j = 0; j <goodsName.length;j++){
            if(id ==goodsName[j]){
                document.getElementById("code").value =goodsCode[j];            
        }
    }
    }else{
        for(var z = 0; z <goodsNameFsn.length;z++){
            if(id ==goodsNameFsn[z]){
                document.getElementById("code").value =goodsCodeFsn[z];            
            }
        }
    }
}
function mapCodeToDescription(){
    var id = document.getElementById("code").value ;
    if(document.getElementById("typeOfGoods").value == 0){
        for(var q = 0; q <goodsCode.length;q++){
            if(id ==goodsCode[q]){
                document.getElementById("description").value = goodsName[q];            
            }
        }
    }else{
        for(var x = 0; x <goodsCode.length;x++){
            if(id ==goodsCodeFsn[x]){
                document.getElementById("description").value = goodsNameFsn[x];            
            }
        }
    }
}

 function reDisableRadio () { 
    document.getElementById("description").disabled = true;
    document.getElementById("code").disabled = true;
 } 

function submitButtonClicks(){
    
    submitStatus = true;
    if(document.getElementById("title").value==""){
        document.getElementById("titleAlert").style.display = "table-row";
        submitStatus = false;
        document.saveOrderGood.title.focus();
    }else  document.getElementById("titleAlert").style.display= "none";

    if(document.getElementById("contractNo").value ==""){
        document.getElementById("contractNoAlert").style.display = "table-row";
        submitStatus = false;
        document.saveOrderGood.contractNo.focus();
    }else  document.getElementById("contractNoAlert").style.display= "none";

    if(document.getElementById("addDate_p").value ==""){
        document.getElementById("addDate_pAlert").style.display = "table-row";
        submitStatus = false;
    }else  document.getElementById("addDate_pAlert").style.display= "none";

    if(document.getElementById("checkDate_p").value==""){
        document.getElementById("checkDate_pAlert").style.display = "table-row";
        submitStatus = false;
    }else  document.getElementById("checkDate_pAlert").style.display= "none";

    if(document.getElementById("years").value ==""){
        document.getElementById("yearsAlert").style.display = "table-row";
        submitStatus = false;
    }else  document.getElementById("yearsAlert").style.display= "none";


    return submitStatus;
}
