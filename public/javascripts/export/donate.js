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

function findFSN(){
	var fsnCode = document.getElementById("fsnCode").value;
	var des = document.getElementById("fsnDescription").value;

	$.ajax({
		type: "GET",
		url: "/export/searchFSN",
		data: {'code': fsnCode, 'description' : des},
		success: function(data){
		   	details = JSON.stringify(data);
		   	var allArticles = data["result"];

		   	var length = allArticles.length;
		   	var s = "";
		   	
		   	destroyTable();
			for (var i = 0; i < length; i++) {
				s += '				<th>'+ "<input type=\"checkbox\"/>" +'</th>';
				s += '				<th>'+ allArticles[i].code +'</th>';
				if(allArticles[i].detail){
					s += '				<th>'+ allArticles[i].detail.fsn.descriptionDescription +'</th>';//descriptionDescription +'</th>';
				}
				else{
					s += '				<th>'+ 'ไม่มี' +'</th>';
				}
				s += '				<th>'+ allArticles[i].remainingLifetime +'</th>';
				s += '				<th>'+ allArticles[i].remainingPrice +'</th>';
				s += '				<th>'+'</th>';
				s += '</tr>';
		   	}
		   	document.getElementById("searchResultTable").innerHTML = s;
		   	updateTable();
		}
	});

}