var submitStatus = true;
var submitNext = true;

var goodsCode=[];
var goodsName=[];
var goodsCodeFsn=[];
var goodsNameFsn=[];
var sourceCode=[];
var sourceName=[];
var pathToRemove;

var prefixNameList= [];
var nameList= [];
var lastnameList= [];
var positionList= [];
var userAll= [] ;

var keyId;
var autoCompleteNum;
angular.module('goodsCodeModule', ['ui.bootstrap'])
    .controller('autoCompleteGoods',function($scope,$http,$modal){
        
        $scope.goodsCode =[];
        $scope.goodsName =[];
        
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
                      source: sourceName,
                      focus: function(event, ui) {
                          $("input#description").val(ui.item.label);
                          mapDescriptionToCode();
                      },
                      select: function(event, ui) {
                         $("#searchform button").click(); 
                         setTimeout(mapDescriptionToCode,100);
                      }
                    });
                });
                $(function() {
                    $( "#code" ).autocomplete({
                      minLength:2,
                      source: sourceCode,
                      focus: function(event, ui) {
                          $("input#code").val(ui.item.label);
                          mapCodeToDescription();
                      },
                      select: function(event, ui) {
                         $("#searchform button").click(); 
                         setTimeout(mapCodeToDescription,100);
                      }
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

function initAutoCompleteName () { 
    keyId = "aiFirstName"+autoCompleteNum;
    $(function() {
        $('#'+keyId).autocomplete({
              source: userAll,
              /*focus: function(event, ui) {
                  $("input#"+keyId).val(ui.item.label);
              },*/
              select: function(event, ui) {
                 $("#searchform button").click();
                 $("input#"+$(this).attr('id')).val(ui.item.label);
                 setTimeout(mapInput($(this).attr('id'),88),200);
              }
        });
    })
    //console.log(keyId);
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

function mapAi(j,temp){
        setTimeout(function(){document.getElementById("aiFirstName"+temp).value = nameList[j];},200);
}
function mapInput(id){
    temp = id[id.length-1];
    var id = document.getElementById(id).value ;
    for(var j = 0; j < userAll.length;j++){
        if(id == userAll[j]){
            document.getElementById("aiPrefixName"+temp).value = prefixNameList[j];            
            document.getElementById("aiFirstName"+temp).value = nameList[j];            
            document.getElementById("aiLastName"+temp).value = lastnameList[j];
            document.getElementById("aiPosition"+temp).value = positionList[j];
            mapAi(j,temp);
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
function setI(num){
   autoCompleteNum = num; 
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
