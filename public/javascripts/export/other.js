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

function addNewDetai(code){
	if(newDetail.indexOf(code) > -1){
		newDetail.remove(code);
	}
	else{
		newDetail.push(code);
	}
}

function findFSN(){
	var fsnCode = document.getElementById("fsnCode").value;
	var des = document.getElementById("fsnDescription").value;
	$.ajax({
		type: "GET",
		url: "/export/searchFSN",
		data: {'code': fsnCode, 'description' : des},
		success: function(data){
		   	//alert(JSON.stringify(data));
		   	var status = data["status"];
		    if(status == "SUCCESS"){
			   	var allArticles = data["result"];
			   	var length = allArticles.length;
			   	var s = "";
			   	destroyTable();
				for (var i = 0; i < length; i++) {
					if((oldDetail.indexOf(allArticles[i].id) < 0)){
						s += '				<th> <input type=\"checkbox\" ';
						if(newDetail.indexOf(allArticles[i].id) > -1){
							s += ' checked';
						}
						s += ' onclick=\"addNewDetai(' + allArticles[i].id + ')\"> </th>';
						s += '				<th>'+ allArticles[i].code +'</th>';
						if(allArticles[i].detail){
							s += '	<th>'+ allArticles[i].detail.fsn.descriptionDescription +'</th>';
							s += '	<th>'+ allArticles[i].detail.llifeTime + ' ปี / ' + allArticles[i].remainLifetimeToString +'</th>';
							s += '	<th>'+ allArticles[i].detail.price + ' / ' + allArticles[i].remainingPriceToString +'</th>';
							s += '	<th>'+ allArticles[i].id + ' : ' + allArticles[i].detail.procurement.checkDate + '</th>';
						}
						else{
							s += '	<th>'+ 'ไม่มี' +'</th>';
							s += '	<th>'+ 'ไม่มี' +'</th>';
							s += '	<th>'+ 'ไม่มี' +'</th>';
							s += '	<th>'+ allArticles[i].id + ' : ' + 'ไม่มี' +'</th>';
						}
						s += '</tr>';
					}
			   	}
			   	document.getElementById("searchResultTable").innerHTML = s;
			   	updateTable();
		    }
		    else{
		    	alert("find FSN :" + data["message"]);
		    }
		}
	});
}

function getDetail(){
	$.ajax({
		type: "GET",
		url: "/export/other/lodeDetail",
		data: {'id': other.id},
		success: function(data){
		   	//alert(JSON.stringify(data));
		   	var status = data["status"];
		    if(status == "SUCCESS"){
			   	var details = data["details"];
			   	var detailLength = details.length;
			   	var s = "";
			   	destroyTable();
			   	oldDetail = [];
				for (var i = 0; i < detailLength; i++) {
					oldDetail.push(details[i].durableArticles.id);
					s += '<tr>';
					s += '	<th>'+(i+1)+'</th>';
					s += '	<th>'+ details[i].durableArticles.code +'</th>';
					if(details[i].durableArticles.detail){
						s += '	<th>'+ details[i].durableArticles.detail.fsn.descriptionDescription +'</th>';
					}
					else{
						s += '	<th>'+ 'ไม่มี' +'</th>';
					}
					s += '	<th>'+ details[i].durableArticles.detail.procurement.budgetType +'</th>';
					s += '	<th>'+ details[i].durableArticles.remainingPriceToString +'</th>';
					s += '</tr>';
			   	}
			   	document.getElementById("detailInTable").innerHTML = s;
			   	updateTable();
		    }
		    else{
		    	alert("get detail :" + data["message"]);
		    }
		}
	});
}

function saveDetail(){
	var dataDetail = {};
	dataDetail.id = other.id;
	dataDetail.detail = newDetail;
	$.ajax({
		url:'/export/other/saveDetail',
	    type: 'post',
	    data: JSON.stringify(dataDetail),
	    contentType: 'application/json',
	    dataType: 'json',
    	success: function(result){
    		//alert(JSON.stringify(result));
    		document.getElementById("fsnCode").value = "";
			document.getElementById("fsnDescription").value = "";
			addOtherButton();
			newDetail = [];
			getDetail();
    	}
	});

}

function init(id){
	other.id = id;
	addOtherButton();
	getDetail();
}