$(function () {
    $('#datePๅ').datetimepicker({
  	  language:'th'
    })
});

$(function () {
    $('#dateP/').datetimepicker({
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
var checkedDetail = [];
var isViewDetail = false;

var titleInHeader = "เพิ่มรายการส่งซ่อม";

function addDetailButton(){
	destroyTable();
	document.getElementById("searchResultTable").innerHTML = "";
	updateTable();
	document.getElementById("addWindows").style.display = "none";
	document.getElementById("addDetailWindows").style.display = "block";
	document.getElementById("titleInHeader").innerHTML = "เพิ่มรายละเอียดการส่งซ่อม";
	document.getElementById("fsnCode").focus();
}

function addRepairButton(){
	document.getElementById("addWindows").style.display = "block";
	document.getElementById("addDetailWindows").style.display = "none";
	document.getElementById("titleInHeader").innerHTML = titleInHeader;
}

function addNewDetai(code){
	if(newDetail.indexOf(code) > -1){
		newDetail.remove(code);
		document.getElementById("fsn" + code).style.color = "";
		document.getElementById("addDetail" + code).checked = false;
	}
	else{
		newDetail.push(code);
		document.getElementById("fsn" + code).style.color = "#cc3300";
		document.getElementById("addDetail" + code).checked = true;
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
		    if(data["status"] == "SUCCESS"){
			   	var allArticles = data["result"];
			   	var length = allArticles.length;
			   	var s = "";
			   	destroyTable();
				for (var i = 0; i < length; i++) {
					if((oldDetail.indexOf(allArticles[i].id) < 0)){
						s += '<tr id="fsn' + allArticles[i].id + '" >';
						s += '	<th onclick="addNewDetai(' + allArticles[i].id + ')"> ';
						if(newDetail.indexOf(allArticles[i].id) > -1){
							s += '<input type="checkbox" id="addDetail' + allArticles[i].id + '" checked';
						}
						else{
							s += '<input type="checkbox" id="addDetail' + allArticles[i].id + '"';
						}
						s += '> </th>';
						s += ' <th onclick="addNewDetai(' + allArticles[i].id + ')">'+ allArticles[i].code +'</th>';
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
		    	alert("find FSN error : " + data["message"]);
		    }
		}
	});
}

function addCheckedDetail(code){
	if(!isViewDetail){
		if(checkedDetail.indexOf(code) > -1){
			checkedDetail.remove(code);
			document.getElementById("detailRow" + code).style.color = "";
			document.getElementById("detail" + code).checked = false;
		}
		else{
			checkedDetail.push(code);
			document.getElementById("detailRow" + code).style.color = "#cc3300";
			document.getElementById("detail" + code).checked = true;
		}
	}
}

function getDetail(id){
	$.ajax({
		type: "GET",
		url: "/export/repair/loadDetail",
		data: {'id': id},
		success: function(data){
		   	//alert(JSON.stringify(data));
		    if(data["status"] == "SUCCESS"){
			   	var details = data["details"];
			   	var detailLength = details.length;
			   	var s = "";
			   	oldDetail = [];
			   	destroyTable();
				for (var i = 0; i < detailLength; i++) {
					oldDetail.push(details[i].durableArticles.id);
					s += '<tr id="detailRow' + details[i].id + '">';
					s += '	<th onclick="addCheckedDetail(' + details[i].id + ')"> '+
								' <input type="checkbox" id="detail' + details[i].id + '"> </th>';
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

			   	for (i = 0, len = checkedDetail.length; i < len; i++) {
			   		document.getElementById("detail" + checkedDetail[i]).checked = true;
    				document.getElementById("detailRow" + checkedDetail[i]).style.color = "#cc3300";
				}
		    }
		    else{
		    	alert("get detail error : " + data["message"]);
		    }
		}
	});
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
    		var status = result["status"];
		    if(status == "SUCCESS"){
	    		document.getElementById("fsnCode").value = "";
				document.getElementById("fsnDescription").value = "";
				document.getElementById("natureOfDamage").value = "";
				addRepairButton();
				newDetail = [];
				getDetail(repair.id);
			}
			else{
				alert('save detail error : ' + data["message"]);
			}
    	}
	});
}

function init(id){
	repair.id = id;
	getDetail(id);
	addRepairButton();
}

function initViewDetial(id){
	isViewDetail = true;
	document.getElementById("title").disabled = true;
	document.getElementById("number").disabled = true;
	document.getElementById("dateOfSentToRepair").disabled = true;
	document.getElementById("dateOfResiveFromRepair").disabled = true;
	document.getElementById("approverFirstName").disabled = true;
	document.getElementById("approverLastName").disabled = true;
	document.getElementById("approverPosition").disabled = true;

	document.getElementById("datepickerbuttonP1").style.display = "none";
	document.getElementById("datepickerbuttonP2").style.display = "none";
	document.getElementById("editDetail").style.display = "none";
	document.getElementById("saveExport").style.display = "none";
	
	init(id);
	titleInHeader = "แสดงรายละเอียดการส่งซ่อม";
	document.getElementById("titleInHeader").innerHTML = titleInHeader;
}

function changeToEdit(){
	isViewDetail = false;
	document.getElementById("title").disabled = false;
	document.getElementById("number").disabled = false;
	document.getElementById("dateOfSentToRepair").disabled = false;
	document.getElementById("dateOfResiveFromRepair").disabled = false;
	document.getElementById("approverFirstName").disabled = false;
	document.getElementById("approverLastName").disabled = false;
	document.getElementById("approverPosition").disabled = false;

	document.getElementById("datepickerbuttonP1").style.display = "table-cell";
	document.getElementById("datepickerbuttonP2").style.display = "table-cell";
	document.getElementById("editDetail").style.display = "block";
	document.getElementById("saveExport").style.display = "block";
	document.getElementById("changeToEditButton").style.display = "none";

	titleInHeader = "แก้ไขรายละเอียดการส่งซ่อม";
	document.getElementById("titleInHeader").innerHTML = titleInHeader;
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
