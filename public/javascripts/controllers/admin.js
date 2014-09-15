var usersTick = [];

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
	var data = {
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
		'module5' : document.getElementById("add5").checked
	};

	postData('/Admin/addRole', data);

}

function del(name) {
	var data = {
		'name' : name.textContent
	};
	postData('/Admin/removeRole', data);
}