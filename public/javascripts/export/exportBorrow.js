angular.module('exportBorrowApp', ['ui.bootstrap'])
    .controller('exportDel',function($scope,$modal){

            $scope.openDelModal = function() {
                var delModalInstance = $modal.open({
                        templateUrl : 'cancelList.html',
                        controller : deleteModalInstanceCtrl,
                        size : 'lg'
                });
            };
        }
);

var deleteModalInstanceCtrl = function($scope, $modalInstance){
    $scope.name = "Delete List(s).";

   $scope.ok = function () {
        $modalInstance.close();
    };

    $scope.cancel = function () {
        $modalInstance.dismiss();
    };
}

var deleteList = [];

function addDeleteList(code){
    if(deleteList.indexOf(code) > -1){
        deleteList.remove(code);
        document.getElementById("row" + code).style.color = "";
        document.getElementById("checkbox" + code).checked = false;
    }
    else{
        deleteList.push(code);
        document.getElementById("row" + code).style.color = "#cc3300";
        document.getElementById("checkbox" + code).checked = true;
    }
}

function deleteBorrow(){
    var dataDetail = {};
    dataDetail.detail = deleteList;
    $.ajax({
        url:'/export/borrow/deletes',
        type: 'post',
        data: JSON.stringify(dataDetail),
        contentType: 'application/json',
        dataType: 'json',
        success: function(result){
            var status = result["status"];
            if(status == "SUCCESS"){
                var deleteList = [];
            }
            else{
                alert('save detail error : ' + data["message"]);
            }
        }
    });
}