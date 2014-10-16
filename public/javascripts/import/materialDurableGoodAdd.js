var submitStatus = true;

var availableId = [];
var availableDes = [];
var availableClassDes = [];
var availableClassId = [];

var desId;
angular.module('materialConsumableGoodsAddMatCode', ['ui.bootstrap'])
    .controller('autoCompleteController',function($scope,$http){
        
        $scope.lastDes;

        function findNextMaterialNumber(key){
            $http({method : 'GET',url : 'findNextMaterialNumber' ,params : { matKey: key}})
            .success(function(result){
                $scope.lastDes = result;
                desId = $scope.lastDes.lastDes;
                plusOne(desId);
            });
        }

        function plusOne(desId) { 
            var number = desId[2]+desId[3]+desId[4];
             number = parseInt(number)+1;
            if(number<10){
                number = "0"+"0"+number;
            }else  if(number<100){
                number = "0"+number;
            }else {
                number = number;
            }

            $scope.code = desId[0]+desId[1] + (number);
        } 
        $scope.genCode = function(){
            if(document.getElementById("materialCode").value.length == 2){
                findNextMaterialNumber(document.getElementById("materialCode").value);
            }
        };

    }
);

function submitButtonClick(){
    submitStatus = true;
    if(document.getElementById("description").value ==""){
        document.getElementById("descriptionAlert").style.display = "table-row";
        submitStatus = false;
    }else  document.getElementById("descriptionAlert").style.display= "none";

    if(document.getElementById("materialCode").value.length != 5 ){
        document.getElementById("materialCodeAlert").style.display = "table-row";
        submitStatus = false;
    }else  document.getElementById("materialCodeAlert").style.display= "none";

    return submitStatus;
}
