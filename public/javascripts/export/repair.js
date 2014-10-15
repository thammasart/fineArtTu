$(function () {
    $('#dateP').datetimepicker({
  	  language:'th'
    })
});

var repair = {
	'id': 0,
	'title': "",
	'contractNo': ""
};

var newDetail = [];	
var oldDetail = [];

function addDetailButton(){

	destroyTable();
	document.getElementById("searchResultTable").innerHTML = "";
	updateTable();

	document.getElementById("addWindows").style.display = "none";
	document.getElementById("addDetailWindows").style.display = "block";
	document.addDetail.fsnCode.focus();
}

function addRepairButton(){
	document.getElementById("addWindows").style.display = "block";
	document.getElementById("addDetailWindows").style.display = "none";
}

function addNewDetai(code){
	if(newDetail.indexOf("fsn" + code) > -1){
		newDetail.remove("fsn" + code);
		document.getElementById("fsn" + code).style.color = "";
	}
	else{
		newDetail.push("fsn" + code);
		document.getElementById("fsn" + code).style.color = "#cc3300";
	}
}

function getDetail(id){
	$.ajax({
		type: "GET",
		url: "/export/repair/lodeDetail",
		data: {'id': id},
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
					s += '	<th>'+ details[i].description +'</th>';
					s += '</tr>';
			   	}
			   	document.getElementById("detailInTable").innerHTML = s;
			   	updateTable();
		    }
		    else{
		    	alert(data["message"]);
		    }
		}
	});
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
						s += '<tr id="' + 'fsn' + allArticles[i].id + '">';
						s += '	<th> <input type=\"checkbox\" ';
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
		    	alert(data["message"]);
		    }
		}
	});

}

function submitButtonJsClick(){
    var submitStatusjs = true;
    if(document.getElementById("natureOfDamage").value ==""){
        document.getElementById("natureOfDamageAlert").style.display = "table-row";
        submitStatusjs = false;
        document.addSold.title.focus();
    }else  document.getElementById("natureOfDamageAlert").style.display= "none";

    if(submitStatusjs == true){
        saveDetail();
    }
    
}
function saveDetail(){
	var dataDetail = {};
	dataDetail.id = repair.id;
	dataDetail.description = document.getElementById("natureOfDamage").value;
	dataDetail.detail = newDetail;
	$.ajax({
		url:'/export/repair/saveDetail',
	    type: 'post',
	    data: JSON.stringify(dataDetail),
	    contentType: 'application/json',
	    dataType: 'json',
    	success: function(result){
    		//alert(JSON.stringify(result));
    		document.getElementById("fsnCode").value = "";
			document.getElementById("fsnDescription").value = "";
			document.getElementById("natureOfDamage").value = "";
			addRepairButton();
			newDetail = [];
			getDetail(repair.id);
    	}
	});
}

function init(id){
	repair.id = id;
	getDetail(id);
	addRepairButton();
}
function submitButtonClick(){
    
    submitStatus = true;
    if(document.getElementById("title").value ==""){
        document.getElementById("titleAlert").style.display = "table-row";
        submitStatus = false;
        document.addSold.title.focus();
    }else  document.getElementById("titleAlert").style.display= "none";

    if(document.getElementById("number").value ==""){
        document.getElementById("numberAlert").style.display = "table-row";
        submitStatus = false;
        document.addSold.number.focus();
    }else  document.getElementById("numberAlert").style.display= "none";

    if(document.getElementById("dateOfSentToRepair").value ==""){
        document.getElementById("dateOfSentToRepairAlert").style.display = "table-row";
        submitStatus = false;
        document.addSold.dateOfSentToRepair.focus();
    }else  document.getElementById("dateOfSentToRepairAlert").style.display= "none";

    if(document.getElementById("approverFirstName").value ==""){
        document.getElementById("approverFirstNameAlert").style.display = "table-row";
        submitStatus = false;
        document.addSold.approverFirstName.focus();
    }else  document.getElementById("approverFirstNameAlert").style.display= "none";

    if(document.getElementById("approverLastName").value ==""){
        document.getElementById("approverLastNameAlert").style.display = "table-row";
        submitStatus = false;
        document.addSold.approverLastName.focus();
    }else  document.getElementById("approverLastNameAlert").style.display= "none";

    if(document.getElementById("approverPosition").value ==""){
        document.getElementById("approverPositionAlert").style.display = "table-row";
        submitStatus = false;
        document.addSold.approverPosition.focus();
    }else  document.getElementById("approverPositionAlert").style.display= "none";

    return submitStatus;
}
