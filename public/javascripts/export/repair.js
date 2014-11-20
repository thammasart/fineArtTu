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

var repair = {
	'id': 0,
	'title': "",
	'contractNo': ""
};

var newDetail = [];	
var oldDetail = [];
var details = [];
var detailEdit = {};
var checkedDetail = [];
var isViewDetail = false;

var titleInHeader = "เพิ่มรายการส่งซ่อม";

function getTotalPrice(){
	numberOfDetail =  parseInt(document.getElementById("numberOfDetail").value);
	totle = 0.00;
	for(i = 0, len = numberOfDetail; i < len; i++){
		if(document.getElementById("price"+i.toString()).value != "")
			totle += parseFloat(document.getElementById("price"+i.toString()).value);
	}
	document.getElementById("repairCosts").value = totle;
}

function addDetailButton(){
	newDetail = [];
	destroyTable();
	document.getElementById("searchResultTable").innerHTML = "";
	updateTable();
	document.getElementById("addWindows").style.display = "none";
	document.getElementById("addDetailWindows").style.display = "block";
	document.getElementById("editDetailWindows").style.display = "none";
	document.getElementById("titleInHeader").innerHTML = "เพิ่มรายละเอียดการส่งซ่อม";
	document.getElementById("fsnCode").focus();
}

function addRepairButton(){
	document.getElementById("addWindows").style.display = "block";
	document.getElementById("addDetailWindows").style.display = "none";
	document.getElementById("editDetailWindows").style.display = "none";
	document.getElementById("titleInHeader").innerHTML = titleInHeader;
}

function editDetail(code){
	document.getElementById("addWindows").style.display = "none";
	document.getElementById("addDetailWindows").style.display = "none";
	document.getElementById("editDetailWindows").style.display = "block";
	document.getElementById("titleInHeader").innerHTML = "แก้ไขรายละเอียดการเบิกจ่าย";
	for(i = 0, len = details.length; i < len; i++){
		if(details[i].id == code){
			detailEdit = details[i];
		}
	}
	detailEdit.id = code;

	document.getElementById("edit_fsn_Number").innerHTML = detailEdit.durableArticles.code;
    document.getElementById("edit_fsn_description").innerHTML = detailEdit.durableArticles.detail.fsn.descriptionDescription;
    document.getElementById("edit_price").innerHTML = detailEdit.durableArticles.detail.price;
    document.getElementById("edit_llifeTime").innerHTML = detailEdit.durableArticles.detail.llifeTime;
    document.getElementById("edit_department").innerHTML = detailEdit.durableArticles.department;
    document.getElementById("edit_description").value = detailEdit.description ;
    document.getElementById("edit_description").focus();

	if(document.getElementById("edit_cost")){
		document.getElementById("edit_cost").value = detailEdit.price;
		document.getElementById("edit_cost").focus();
	}
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
		url: "/export/repair/loadDetail",
		data: {'id': repair.id},
		async:   false,
		success: function(data){
		   	//alert(JSON.stringify(data));
		    if(data["status"] == "SUCCESS"){
			   	details = data["details"];
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
					if(details[i].repairing.status == "SUCCESS"){
						s += '<th style="text-align:center;" >'+ details[i].price +'</th>';
					}
					s += '  <th onclick="editDetail('+ details[i].id + ')"> <button type="button" class="btn btn-xs btn-warning" id="edit'+i+'"> แก้ไข </button> </th>';
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
				getDetail();
			}
			else{
				alert('save detail error : ' + data["message"]);
			}
    	}
	});
}

function saveEditDetail(){
	detailEdit.description = document.getElementById("edit_description").value;
	detailEdit.repairingId = repair.id;
	if(document.getElementById("edit_cost")){
		detailEdit.cost = document.getElementById("edit_cost").value;
	}

	$.ajax({
		url:'/export/repair/editDetail',
	    type: 'post',
	    data: JSON.stringify(detailEdit),
	    contentType: 'application/json',
	    dataType: 'json',
    	success: function(result){
    		var status = result["status"];
		    if(status == "SUCCESS"){
	    		addRepairButton()
	    		getDetail();
	    		if(document.getElementById("edit_cost")){
	    			totle = 0;
					for(i=0; i<details.length; i++){
						totle += details[i].price;
					}
					document.getElementById("repairCosts").value = totle;
				}
	    	}
	    	else{
	    		alert('save detail error : ' + result["message"]);
	    	}
    	}
	});
}

function deleteDetail(){
	var dataDetail = {};
	dataDetail.id = repair.id;
	dataDetail.detail = checkedDetail;
	$.ajax({
		url:'/export/repair/deleteDetail',
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
	repair.id = id;
	getDetail();
	addRepairButton();
}

function initReceive(id){
	isViewDetail = true;
	document.getElementById("title").disabled = true;
	document.getElementById("number").disabled = true;
	document.getElementById("repairShop").disabled = true;
	document.getElementById("dateOfSentToRepair").disabled = true;
	document.getElementById("approverFirstName").disabled = true;
	document.getElementById("approverLastName").disabled = true;
	document.getElementById("approverPosition").disabled = true;

	document.getElementById("datepickerbuttonP1").style.display = "none";
	document.getElementById("editDetail").style.display = "none";
	
	init(id);
	titleInHeader = "รับคืน";
	document.getElementById("titleInHeader").innerHTML = titleInHeader;
}

function initViewDetial(id){
	isViewDetail = true;
	document.getElementById("title").disabled = true;
	document.getElementById("number").disabled = true;
	document.getElementById("repairShop").disabled = true;
	document.getElementById("dateOfSentToRepair").disabled = true;
	document.getElementById("dateOfResiveFromRepair").disabled = true;
	document.getElementById("repairCosts").disabled = true;
	document.getElementById("approverFirstName").disabled = true;
	document.getElementById("approverLastName").disabled = true;
	document.getElementById("approverPosition").disabled = true;

	document.getElementById("datepickerbuttonP1").style.display = "none";
	document.getElementById("datepickerbuttonP2").style.display = "none";
	document.getElementById("editDetail").style.display = "none";
	document.getElementById("saveExport").style.visibility = "hidden";
	
	init(id);
	titleInHeader = "แสดงรายละเอียดการส่งซ่อม";
	document.getElementById("titleInHeader").innerHTML = titleInHeader;

	for(i=0; i< details.length; i++){
		document.getElementById("edit"+i).style.display = "none";
	}
}

function changeToEdit(){
	isViewDetail = false;
	document.getElementById("title").disabled = false;
	document.getElementById("number").disabled = false;
	document.getElementById("repairShop").disabled = false;
	document.getElementById("dateOfSentToRepair").disabled = false;
	document.getElementById("dateOfResiveFromRepair").disabled = false;
	document.getElementById("repairCosts").disabled = false;
	document.getElementById("approverFirstName").disabled = false;
	document.getElementById("approverLastName").disabled = false;
	document.getElementById("approverPosition").disabled = false;

	document.getElementById("datepickerbuttonP1").style.display = "table-cell";
	document.getElementById("datepickerbuttonP2").style.display = "table-cell";
	document.getElementById("editDetail").style.display = "block";
	document.getElementById("saveExport").style.visibility = "visible";
	document.getElementById("changeToEditButton").style.display = "none";

	titleInHeader = "แก้ไขรายละเอียดการส่งซ่อม";
	document.getElementById("titleInHeader").innerHTML = titleInHeader;

	for(i=0; i< details.length; i++){
		document.getElementById("edit"+i).style.display = "block";
	}
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
