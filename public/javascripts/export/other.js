$(function () {
    $('#dateP').datetimepicker({
  	  language:'th'
    })
});

var other = {
	'id': 0,
	'title': "",
	'number': ""
};

var newDetail = [];	
var oldDetail = [];
var checkedDetail = [];
var isViewDetail = false;

var titleInHeader = "เพิ่มรายการโอนย้ายอื่นๆ";

function addDetailButton(){
	newDetail = [];
	destroyTable();
	document.getElementById("searchResultTable").innerHTML = "";
	updateTable();
	document.getElementById("addWindows").style.display = "none";
	document.getElementById("addDetailWindows").style.display = "block";
	document.getElementById("titleInHeader").innerHTML = "เพิ่มรายละเอียดการโอนย้ายอื่นๆ";
	document.getElementById("fsnCode").focus();
}

function addOtherButton(){
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

function getDetail(){
	$.ajax({
		type: "GET",
		url: "/export/other/loadDetail",
		data: {'id': other.id},
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
					s += '	<th onclick="addCheckedDetail(' + details[i].id + ')"' +
								' <input type="checkbox" id="detail' + details[i].id + '"> </th>';
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
	dataDetail.id = other.id;
	dataDetail.detail = newDetail;
	$.ajax({
		url:'/export/other/saveDetail',
	    type: 'post',
	    data: JSON.stringify(dataDetail),
	    contentType: 'application/json',
	    dataType: 'json',
    	success: function(result){
    		var status = result["status"];
		    if(status == "SUCCESS"){
	    		document.getElementById("fsnCode").value = "";
				document.getElementById("fsnDescription").value = "";
				addOtherButton();
				newDetail = [];
				getDetail();
			}
			else{
				alert('save detail error : ' + data["message"]);
			}
    	}
	});
}

function deleteDetail(){
	var dataDetail = {};
	dataDetail.id = other.id;
	dataDetail.detail = checkedDetail;
	$.ajax({
		url:'/export/other/deleteDetail',
	    type: 'post',
	    data: JSON.stringify(dataDetail),
	    contentType: 'application/json',
	    dataType: 'json',
    	success: function(result){
    		var status = result["status"];
		    if(status == "SUCCESS"){
		    	var newDetail = [];	
				var oldDetail = [];
				getDetail();
			}
			else{
				alert('save detail error : ' + data["message"]);
			}
    	}
	});
}

function init(id){
	other.id = id;
	addOtherButton();
	getDetail();
}

function initViewDetial(id){
	isViewDetail = true;
	document.getElementById("title").disabled = true;
	document.getElementById("number").disabled = true;
	document.getElementById("approveDate").disabled = true;
	document.getElementById("description").disabled = true;
	document.getElementById("approverName").disabled = true;
	document.getElementById("approverLastName").disabled = true;
	document.getElementById("approverPosition").disabled = true;

	document.getElementById("datepickerbutton").style.display = "none";
	document.getElementById("editDetail").style.display = "none";
	document.getElementById("saveExport").style.display = "none";

	init(id);
	titleInHeader = "แสดงรายละเอียดการโอนย้ายอื่นๆ";
	document.getElementById("titleInHeader").innerHTML = titleInHeader;
}

function changeToEdit(){
	isViewDetail = false;
	document.getElementById("title").disabled = false;
	document.getElementById("number").disabled = false;
	document.getElementById("approveDate").disabled = false;
	document.getElementById("description").disabled = false;
	document.getElementById("approverName").disabled = false;
	document.getElementById("approverLastName").disabled = false;
	document.getElementById("approverPosition").disabled = false;

	document.getElementById("datepickerbutton").style.display = "table-cell";
	document.getElementById("editDetail").style.display = "block";
	document.getElementById("saveExport").style.display = "block";
	document.getElementById("changeToEditButton").style.display = "none";

	titleInHeader = "แก้ไขรายละเอียดการโอนย้ายอื่นๆ";
	document.getElementById("titleInHeader").innerHTML = titleInHeader;
}
