var donation = {
	'id': 0,
	'title': "",
	'number': ""
};

function init(id){
	addDonateButton();
	//alert("init");
}

function addDetailButton(){
	document.getElementById("addWindows").style.display = "none";
	document.getElementById("addDetailWindows").style.display = "block";
	document.addDetail.fsnCode.focus();
}

function addDonateButton(){
	document.getElementById("addWindows").style.display = "block";
	document.getElementById("addDetailWindows").style.display = "none";
}