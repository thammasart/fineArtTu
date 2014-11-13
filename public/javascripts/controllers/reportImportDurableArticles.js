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
                          },
                          focus: function(event, ui) {
                              $("input#groupId").val(ui.item.label);
                            matchIDToDes();
                          },
                          select: function(event, ui) {
                             $("#searchform button").click(); 
                             setTimeout(matchIDToDes,100);
                          }
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
