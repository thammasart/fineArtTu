var usersTick = [];
var data;
var editDate;

var userNameEdit;

angular.module('adminApp', ['ui.bootstrap'])
    .controller('delUser',function($scope,$modal){

        $scope.openEditRole = function(name){
            var modalInstance = $modal.open({
                templateUrl: 'adminEditUserRole.html',
                controller: editModalInstanceCtrl,
                size: 'lg'
            });
                userNameEdit = name;
        };

        $scope.open = function(){

            var modalInstance = $modal.open({
                templateUrl: 'adminDelUser.html',
                controller: resultModalInstanceCtrl,
                size: 'lg',
                resolve: {
                    name : function(){
                        return $scope.name;
                    }
                }
            });
        };
    }
);

var editModalInstanceCtrl= function($scope, $modalInstance){
    $scope.name = "Edit User(s) Role.";
    $scope.username = userNameEdit;
    $scope.ok = function () {
            editData = {
                            'editName'	: userNameEdit,
                            'newRole'      : document.getElementById("status").value
            };
	postData('/Admin/editUser',editData);
        $modalInstance.close();
    };

    $scope.cancel = function () {
        $modalInstance.dismiss();
    };
}
var resultModalInstanceCtrl= function($scope, $modalInstance){
    $scope.name = "Delete User(s).";
    $scope.userT= usersTick;
    $scope.ok = function () {
        removeUser();
        $modalInstance.close();
    };

    $scope.cancel = function () {
        $modalInstance.dismiss();
    };
}


function addTick(name){
	var username = name.id;
	console.log(usersTick);
	if(usersTick.indexOf(username) > -1){
		usersTick.remove(username);
	}else{
		usersTick.push(username);
	}
}

function removeUser(){
            data = {
                            'data'	: usersTick
            };
	postData('/Admin/removeUser',data);
}

function add() {
	var result_style = document.getElementById('addRole').style;
	result_style.display = "table-row";
}

function save() {
	var data = {
		'name' : document.getElementById("addName").value,
		'module1' : document.getElementById("add1").checked,
		'module2' : document.getElementById("add2").checked,
		'module3' : document.getElementById("add3").checked,
		'module4' : document.getElementById("add4").checked,
		'module5' : document.getElementById("add5").checked,
		'module6' : document.getElementById("add6").checked
	};

	postData('/Admin/addRole', data);

}

function del(name) {
	var data = {
		'name' : name.textContent
	};
	postData('/Admin/removeRole', data);
}
