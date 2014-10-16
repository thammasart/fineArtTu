var submitStatus = true;

var goodsCode=[];
var goodsName=[];

angular.module('goodsCodeModule', ['ui.bootstrap'])
    .controller('autoCompleteGoods',function($scope,$http){
        
        $scope.goodsCode =[];
        $scope.goodsName =[];
        
        $scope.findGoods = function(){
            $http({method : 'GET',url : 'autoCompleteGood' })
            .success(function(result){
                
            goodsCode = result.goodsCode;
            goodsName = result.goodsName;
            console.log(goodsCode);

                $(function() {
                    $( "#description" ).autocomplete({
                      minLength:2,
                      source: goodsName
                    });
                });
                $(function() {
                    $( "#code" ).autocomplete({
                      minLength:3,
                      source: goodsCode
                    });
                });
            });
        };
    }
);
function mapDescriptionToCode(){
    var id = document.getElementById("description").value ;
    for(var j = 0; j <goodsName.length;j++){
        if(id ==goodsName[j]){
            document.getElementById("code").value =goodsCode[j];            
        }
    }
}
function mapCodeToDescription(){
    var id = document.getElementById("code").value ;
    for(var j = 0; j <goodsCode.length;j++){
        if(id ==goodsCode[j]){
            document.getElementById("description").value = goodsName[j];            
        }
    }
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
    if(document.getElementById("description").value ==""){
        document.getElementById("descriptionAlert").style.display = "table-row";
        submitStatus = false;
    }else  document.getElementById("descriptionAlert").style.display= "none";

    if(document.getElementById("code").value==""){
        document.getElementById("codeAlert").style.display = "table-row";
        submitStatus = false;
    }else  document.getElementById("codeAlert").style.display= "none";

    if(document.getElementById("price").value ==""){
        document.getElementById("priceAlert").style.display = "table-row";
        submitStatus = false;
    }else  document.getElementById("priceAlert").style.display= "none";

    if(document.getElementById("priceNoVat").value ==""){
        document.getElementById("priceNoVatAlert").style.display = "table-row";
        submitStatus = false;
    }else  document.getElementById("priceNoVatAlert").style.display= "none";

    if(document.getElementById("quantity").value==""){
        document.getElementById("quantityAlert").style.display = "table-row";
        submitStatus = false;
    }else  document.getElementById("quantityAlert").style.display= "none";

    if(document.getElementById("seller").value ==""){
        document.getElementById("sellerAlert").style.display = "table-row";
        submitStatus = false;
    }else  document.getElementById("sellerAlert").style.display= "none";

    if(document.getElementById("phone").value ==""){
        document.getElementById("phoneAlert").style.display = "table-row";
        submitStatus = false;
    }else  document.getElementById("phoneAlert").style.display= "none";

    return submitStatus;
}
