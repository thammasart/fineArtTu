var submitStatus = true;
var availableId = [];
var availableDes = [];
var availableClassDes = [];
var availableClassId = [];

var desId;
angular.module('materialConsumableGoodsAddFsn', ['ui.bootstrap'])
    .controller('autoCompleteController',function($scope,$http){
        
        $scope.groupId= [];
        $scope.groupDes= [];
        $scope.classId= [];
        $scope.classDes= [];
        $scope.lastDes;
        $scope.typeId = document.getElementById("typeId").value.length;
        $scope.descriptionId;

        function findNextFsnNumber(key){
            $http({method : 'GET',url : 'findNextFsnNumber' ,params : { fsnKey: key}})
            .success(function(result){
                $scope.lastDes= result;
                console.log($scope.lastDes);
                desId = $scope.lastDes.lastDes;
                plusOne(desId);
            });
        }

        function plusOne(desId) { 
            var number = desId[9]+desId[10]+desId[11]+desId[12];
             number = parseInt(number)+1;
            if(number<10){
                number = "-"+"0"+"0"+"0"+number;
            }else  if(number<100){
                number = "-"+"0"+"0"+number;
            }else  if(number<1000){
                number = "-"+"0"+number;
            }else  if(number<10000){
                number = "-"+number;
            }


            $scope.descriptionId = $scope.typeId + (number);
        } 
        $scope.matchTypeToDes = function(){
            if(document.getElementById("typeId").value.length == 8){
                findNextFsnNumber(document.getElementById("typeId").value);
            }else document.getElementById("descriptionId").value = document.getElementById("typeId").value +"-";
        };
        $scope.findFsn =function(){
            $http({method : 'GET',url : 'findFsn' })
            .success(function(result){
                $scope.groupId = result.groupId;
                $scope.groupDes= result.groupDes;
                $scope.classId = result.classId;
                $scope.classDes = result.classDes;
                availableId = $scope.groupId;
                availableDes = $scope.groupDes;
                availableClassId = $scope.classId;
                availableClassDes = $scope.classDes;

                $(function() {
                    $( "#groupId" ).autocomplete({
                              source: function( request, response ) {
                                      var matcher = new RegExp( "^" + $.ui.autocomplete.escapeRegex( request.term ), "i" );
                                      response( $.grep( availableId, function( item ){
                                          return matcher.test( item );
                                      }) );
                              }
                    });
                });
                $(function() {
                        $( "#groupDes" ).autocomplete({
                              minLength:2,
                              source: availableDes
                        });
                });

                $(function() {
                        $( "#classId" ).autocomplete({
                              minLength:2,
                              source: function( request, response ) {
                                      var matcher = new RegExp( "^" + $.ui.autocomplete.escapeRegex( request.term ), "i" );
                                      response( $.grep( availableClassId, function( item ){
                                          return matcher.test( item );
                                      }) );
                              }
                        });
                });

                $(function() {
                        $( "#classDescription" ).autocomplete({
                              minLength:3,
                              source: availableClassDes
                        });
                });

            });
        };
    }
);

function matchIDToDes() { 
    for(var i = 0; i < availableId.length;i++){
        if(document.getElementById("groupId").value  == availableId[i]){
            document.getElementById("groupDes").value = availableDes[i];
            document.getElementById("classId").value = groupId.value;
                break;
        }  
    }
} 
function matchDesToID() { 
    for(var i = 0; i < availableId.length;i++){
        if(document.getElementById("groupDes").value  == availableDes[i]){
            document.getElementById("groupId").value = availableId[i];
            document.getElementById("classId").value = groupId.value;
                break;
        }  
    }
} 
function matchClassIdToDes(){
    var temp;
    if(document.getElementById("classId").value.length > 3){
        for(var j = 0;j<availableClassDes.length;j++){
            if(document.getElementById("classId").value == availableClassId[j]){
                document.getElementById("classDescription").value = availableClassDes[j];
                temp = document.getElementById("classId").value;
                document.getElementById("typeId").value = temp + "-";
                temp = temp[0] + temp[1];
                document.getElementById("groupId").value = temp;
                findGroupDesByid(temp);
                break;
            }
        }
    }
}
function matchClassDesToId(){
    var temp;
    if(document.getElementById("classDescription").value.length > 3){
        for(var j = 0;j<availableClassDes.length;j++){
            if(document.getElementById("classDescription").value == availableClassDes[j]){
                document.getElementById("classId").value = availableClassId[j];
                temp = document.getElementById("classId").value;
                document.getElementById("typeId").value = temp + "-";
                temp  = temp[0] + temp[1];
                findGroupDesByid(temp);
                document.getElementById("groupId").value = temp;
                break;
            }
        }
    }
}
function findGroupDesByid(id){
    
    for(var i = 0; i < availableId.length;i++){
        if(id  == availableId[i]){
            document.getElementById("groupDes").value = availableDes[i];
                break;
        }  
    }
}
function submitButtonClick(){
    
    submitStatus = true;
    if(document.getElementById("groupId").value.length !=2){
        document.getElementById("groupIdAlert").style.display = "table-row";
        submitStatus = false;
    }else  document.getElementById("groupIdAlert").style.display= "none";

    if(document.getElementById("groupDes").value ==""){
        document.getElementById("groupDesAlert").style.display = "table-row";
        submitStatus = false;
    }else  document.getElementById("groupDesAlert").style.display= "none";

    if(document.getElementById("classId").value.length != 4){
        document.getElementById("classIdAlert").style.display = "table-row";
        submitStatus = false;
    }else  document.getElementById("classIdAlert").style.display= "none";

    if(document.getElementById("classDescription").value ==""){
        document.getElementById("classDescriptionAlert").style.display = "table-row";
        submitStatus = false;
    }else  document.getElementById("classDescriptionAlert").style.display= "none";

    if(document.getElementById("typeId").value.length !=8){
        document.getElementById("typeIdAlert").style.display = "table-row";
        submitStatus = false;
    }else  document.getElementById("typeIdAlert").style.display= "none";

    if(document.getElementById("typeDescription").value ==""){
        document.getElementById("typeDescriptionAlert").style.display = "table-row";
        submitStatus = false;
    }else  document.getElementById("typeDescriptionAlert").style.display= "none";

    if(document.getElementById("descriptionId").value.length != 13){
        document.getElementById("descriptionIdAlert").style.display = "table-row";
        submitStatus = false;
    }else  document.getElementById("descriptionIdAlert").style.display= "none";

    if(document.getElementById("descriptionDescription").value ==""){
        document.getElementById("descriptionDescriptionAlert").style.display = "table-row";
        submitStatus = false;
    }else  document.getElementById("descriptionDescriptionAlert").style.display= "none";
    return submitStatus;
}
