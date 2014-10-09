var other = {
	'id': 0,
	'title': "",
	'number': ""
};

var newDetail = [];	
var oldDetail = [];

function addDetailButton(){
	document.getElementById("addWindows").style.display = "none";
	document.getElementById("addDetailWindows").style.display = "block";
	document.addDetail.fsnCode.focus();
}

function addOtherButton(){
	document.getElementById("addWindows").style.display = "block";
	document.getElementById("addDetailWindows").style.display = "none";
}

function init(id){
	other.id = id;
	addOtherButton();
}