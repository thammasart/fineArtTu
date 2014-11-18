$(function () {
    $('#dateP1').datetimepicker({
  	  language:'th'
    })
});

$(function () {
    $('#dateP2').datetimepicker({
  	  language:'th'
    })
});


var borrow = {
	'id': 0,
	'title': "",
	'number': ""
};

var newDetail = [];	
var oldDetail = [];
var checkedDetail = [];
var isViewDetail = false;

var titleInHeader = "เพิ่มรายการยืม";

function addDetailButton(){
	newDetail = [];
	destroyTable();
	document.getElementById("searchResultTable").innerHTML = "";
	updateTable();
	document.getElementById("addWindows").style.display = "none";
	document.getElementById("addDetailWindows").style.display = "block";
	document.getElementById("titleInHeader").innerHTML = "เพิ่มรายละเอียดการยืม";
	document.getElementById("fsnCode").focus();
}

function addBorrowButton(){
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
		url: "/export/borrow/loadDetail",
		data: {'id': borrow.id},
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
					s += '	<th onclick="addCheckedDetail(' + details[i].id + ')">' +
								' <input type="checkbox" id="detail' + details[i].id + '"> </th>';
					s += '	<th>'+(i+1)+'</th>';
					s += '	<th>'+ details[i].durableArticles.code +'</th>';
					if(details[i].durableArticles.detail){
						s += '	<th>'+ details[i].durableArticles.detail.fsn.descriptionDescription +'</th>';
					}
					else{
						s += '	<th>'+ 'ไม่มี' +'</th>';
					}
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
	dataDetail.id = borrow.id;
	dataDetail.description = document.getElementById("fsnDescription").value;
	dataDetail.detail = newDetail;
	$.ajax({
		url:'/export/borrow/saveDetail',
	    type: 'post',
	    data: JSON.stringify(dataDetail),
	    contentType: 'application/json',
	    dataType: 'json',
    	success: function(result){
    		var status = result["status"];
		    if(status == "SUCCESS"){
	    		document.getElementById("fsnCode").value = "";
				document.getElementById("fsnDescription").value = "";
				addBorrowButton();
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
	dataDetail.id = borrow.id;
	dataDetail.detail = checkedDetail;
	$.ajax({
		url:'/export/borrow/deleteDetail',
	    type: 'post',
	    data: JSON.stringify(dataDetail),
	    contentType: 'application/json',
	    dataType: 'json',
    	success: function(result){
    		var status = result["status"];
		    if(status == "SUCCESS"){
		    	newDetail = [];	
				oldDetail = [];
				checkedDetail = [];
				getDetail();
			}
			else{
				alert('save detail error : ' + data["message"]);
			}
    	}
	});
}

function init(id){
	borrow.id = id;
	getDetail();
	addBorrowButton();
}

function initReturn(id){
	isViewDetail = true;
	document.getElementById("title").disabled = true;
	document.getElementById("number").disabled = true;
	document.getElementById("dateOfStartBorrow").disabled = true;
	document.getElementById("withdrawerNmae").disabled = true;
	document.getElementById("withdrawerLastname").disabled = true;
	document.getElementById("withdrawerPosition").disabled = true;
	document.getElementById("approverName").disabled = true;
	document.getElementById("approverLastName").disabled = true;
	document.getElementById("approverPosition").disabled = true;

	document.getElementById("datepickerbuttonP2").style.display = "none";
	document.getElementById("editDetail").style.display = "none";
	document.getElementById("saveExport").style.display = "none";
	
	init(id);
	titleInHeader = "รับคืน";
	document.getElementById("titleInHeader").innerHTML = titleInHeader;
}

function initViewDetial(id){
	isViewDetail = true;
	document.getElementById("title").disabled = true;
	document.getElementById("number").disabled = true;
	document.getElementById("dateOfStartBorrow").disabled = true;
	document.getElementById("dateOfEndBorrow").disabled = true;
	document.getElementById("withdrawerNmae").disabled = true;
	document.getElementById("withdrawerLastname").disabled = true;
	document.getElementById("withdrawerPosition").disabled = true;
	document.getElementById("approverName").disabled = true;
	document.getElementById("approverLastName").disabled = true;
	document.getElementById("approverPosition").disabled = true;

	document.getElementById("datepickerbuttonP1").style.display = "none";
	document.getElementById("datepickerbuttonP2").style.display = "none";
	document.getElementById("editDetail").style.display = "none";
	document.getElementById("saveExport").style.visibility = "hidden";
	
	init(id);
	titleInHeader = "แสดงรายละเอียดการยืม";
	document.getElementById("titleInHeader").innerHTML = titleInHeader;
}

function changeToEdit(){
	isViewDetail = false;
	document.getElementById("title").disabled = false;
	document.getElementById("number").disabled = false;
	document.getElementById("dateOfStartBorrow").disabled = false;
	document.getElementById("dateOfEndBorrow").disabled = false;
	document.getElementById("withdrawerNmae").disabled = false;
	document.getElementById("withdrawerLastname").disabled = false;
	document.getElementById("withdrawerPosition").disabled = false;
	document.getElementById("approverName").disabled = false;
	document.getElementById("approverLastName").disabled = false;
	document.getElementById("approverPosition").disabled = false;

	document.getElementById("datepickerbuttonP1").style.display = "table-cell";
	document.getElementById("datepickerbuttonP2").style.display = "table-cell";
	document.getElementById("editDetail").style.display = "block";
	document.getElementById("saveExport").style.visibility = "visible";
	document.getElementById("changeToEditButton").style.display = "none";

	titleInHeader = "แก้ไขรายละเอียดการยืม";
	document.getElementById("titleInHeader").innerHTML = titleInHeader;
}