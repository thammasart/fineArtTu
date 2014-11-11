$(function () {
    $('#dateP').datetimepicker({
  	  language:'th'
    })
});

var internalTransfer = {
	'id': 0,
	'title': "",
	'number': ""
};

var newDetail = [];	
var oldDetail = [];
var details = [];
var detailEdit = {};
var checkedDetail = [];
var isViewDetail = false;

var titleInHeader = "เพิ่มรายการโอนย้ายภายใน";

function addDetailButton(){
	newDetail = [];
	destroyTable();
	document.getElementById("searchResultTable").innerHTML = "";
	updateTable();
	document.getElementById("addWindows").style.display = "none";
	document.getElementById("addDetailWindows").style.display = "block";
	document.getElementById("editDetailWindows").style.display = "none";
	document.getElementById("titleInHeader").innerHTML = "เพิ่มรายละเอียดการโอนย้ายภายใน";
	document.getElementById("fsnCode").focus();
}

function addInternalTransferButton(){
	document.getElementById("addWindows").style.display = "block";
	document.getElementById("addDetailWindows").style.display = "none";
	document.getElementById("editDetailWindows").style.display = "none";
	document.getElementById("titleInHeader").innerHTML = titleInHeader;
}

function editDetail(code){
	document.getElementById("addWindows").style.display = "none";
	document.getElementById("addDetailWindows").style.display = "none";
	document.getElementById("editDetailWindows").style.display = "block";
	document.getElementById("titleInHeader").innerHTML = "แก้ไขรายละเอียดการโอนย้ายภายใน";
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

	var x  = document.getElementById("edit_sentToDepartment");
	for (var i = 0; i < x.length; i++) {
         if(detailEdit.newDepartment == x.options[i].value){
         	x.options[i].selected = "true";
         }
    }
    document.getElementById("edit_room").value = detailEdit.newRoom ;
    document.getElementById("edit_floorLevel").value = detailEdit.newFloorLevel;
    document.getElementById("edit_firstName").value = detailEdit.newFirstName;
    document.getElementById("edit_lastName").value = detailEdit.newLastName;
    document.getElementById("edit_position").value = detailEdit.newPosition;
    // document.getElementById("edit_description").focus();

	if(document.getElementById("edit_cost")){
		document.getElementById("edit_cost").value = detailEdit.price;
		document.getElementById("edit_cost").focus();
	}
}

function addNewDetai(code){
	if(!isViewDetail){
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
		url: "/export/transferInside/loadDetail",
		data: {'id': internalTransfer.id},
		async:   false,
		success: function(data){
		   	//alert(JSON.stringify(data));
		    if(data["status"] == "SUCCESS"){
			   	details = data["details"];
			   	s = "";
			   	oldDetail = [];
			   	destroyTable();
				for (var i = 0; i < details.length; i++) {
					oldDetail.push(details[i].durableArticles.id);
					s += '<tr  id="detailRow' + details[i].id + '">';
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
					s += '	<th>'+ details[i].newDepartment +'</th>';
					s += '	<th>'+ details[i].newFloorLevel +'</th>';
					s += '	<th>'+ details[i].newRoom +'</th>';
					s += '  <th onclick="editDetail('+ details[i].id + ')"> <button type="button" class="btn btn-xs btn-warning" id="edit'+i+'"> แก้ไข </button> </th>';
					s += '</tr>';
			   	}
			   	
			   	if(document.getElementById("detailInTable")){
			   		document.getElementById("detailInTable").innerHTML = s;
			   	}
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
	dataDetail.id = internalTransfer.id;
	dataDetail.department = document.getElementById("department").value;
	dataDetail.room = document.getElementById("room").value;
	dataDetail.floorLevel = document.getElementById("floorLevel").value;
	dataDetail.recieveFirstName = document.getElementById("recieveFirstName").value;
	dataDetail.recieveLastName = document.getElementById("recieveLastName").value;
	dataDetail.recievePosition = document.getElementById("recievePosition").value;

	dataDetail.detail = newDetail;
	$.ajax({
		url:'/export/transferInside/saveDetail',
	    type: 'post',
	    data: JSON.stringify(dataDetail),
	    contentType: 'application/json',
	    dataType: 'json',
    	success: function(result){
    		var status = result["status"];
		    if(status == "SUCCESS"){
	    		document.getElementById("fsnCode").value = "";
				document.getElementById("fsnDescription").value = "";
				document.getElementById("department").value = "";
				document.getElementById("room").value = "";
				document.getElementById("floorLevel").value = "";
				document.getElementById("recieveFirstName").value = "";
				document.getElementById("recieveLastName").value = "";
				document.getElementById("recievePosition").value = "";

				addInternalTransferButton();
				newDetail = [];
				getDetail();
			}
			else{
				alert('save detail error : ' + result["message"]);
			}
    	}
	});
}

function deleteDetail(){
	var dataDetail = {};
	dataDetail.id = internalTransfer.id;
	dataDetail.detail = checkedDetail;
	$.ajax({
		url:'/export/transferInside/deleteDetail',
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

function saveEditDetail(){
	detailEdit.department  = document.getElementById("edit_sentToDepartment").value;
	detailEdit.room  = document.getElementById("edit_room").value;
    detailEdit.floorLevel = document.getElementById("edit_floorLevel").value;
    detailEdit.newFirstName = document.getElementById("edit_firstName").value;
    detailEdit.newLastName = document.getElementById("edit_lastName").value;
    detailEdit.newPosition = document.getElementById("edit_position").value;
    detailEdit.transferInsideId = internalTransfer.id;
	$.ajax({
		url:'/export/transferInside/editDetail',
	    type: 'post',
	    data: JSON.stringify(detailEdit),
	    contentType: 'application/json',
	    dataType: 'json',
    	success: function(result){
    		var status = result["status"];
		    if(status == "SUCCESS"){
	    		addInternalTransferButton();
	    		getDetail();
    			document.getElementById("edit_sentToDepartment").value = '';
				document.getElementById("edit_room").value = '';
			    document.getElementById("edit_floorLevel").value = '';
			    document.getElementById("edit_firstName").value = '';
			    document.getElementById("edit_lastName").value = '';
			    document.getElementById("edit_position").value = '';
	    	}
	    	else{
	    		alert('save detail error : ' + result["message"]);
	    	}
    	}
	});
}

function init(id){
	internalTransfer.id = id;
	addInternalTransferButton();
	getDetail();
}

function initViewDetail(id){
	isViewDetail = true;
	document.getElementById("title").disabled = true;
	document.getElementById("number").disabled = true;
	document.getElementById("approveDate").disabled = true;
	document.getElementById("approverFirstName").disabled = true;
	document.getElementById("approverLastName").disabled = true;
	document.getElementById("approverPosition").disabled = true;

	document.getElementById("datepickerbutton").style.display = "none";
	document.getElementById("editDetail").style.display = "none";
	document.getElementById("saveExport").style.display = "none";

	init(id);
	titleInHeader = "แสดงรายละเอียดการโอนย้ายภายใน";
	document.getElementById("titleInHeader").innerHTML = titleInHeader;

	for(i=0; i< details.length; i++){
		document.getElementById("edit"+i).style.display = "none";
	}
}

function changeToEdit(){
	isViewDetail = false;
	document.getElementById("title").disabled = false;
	document.getElementById("number").disabled = false;
	document.getElementById("approveDate").disabled = false;
	document.getElementById("approverFirstName").disabled = false;
	document.getElementById("approverLastName").disabled = false;
	document.getElementById("approverPosition").disabled = false;

	document.getElementById("datepickerbutton").style.display = "table-cell";
	document.getElementById("editDetail").style.display = "block";
	document.getElementById("saveExport").style.display = "block";
	document.getElementById("changeToEditButton").style.display = "none";

	titleInHeader = "แก้ไขรายละเอียดการโอนย้ายภายใน";
	document.getElementById("titleInHeader").innerHTML = titleInHeader;

	for(i=0; i< details.length; i++){
		document.getElementById("edit"+i).style.display = "block";
	}
}

function submitButtonAddClick(){
    
    submitStatus = true;
    if(document.getElementById("floorLevel").value ==""){
        document.getElementById("floorLevelAlert").style.display = "table-row";
        submitStatus = false;
        document.addTransferInside.title.focus();
    }else  document.getElementById("floorLevelAlert").style.display= "none";

    if(document.getElementById("room").value ==""){
        document.getElementById("roomAlert").style.display = "table-row";
        submitStatus = false;
        document.addTransferInside.number.focus();
    }else  document.getElementById("roomAlert").style.display= "none";


    if(document.getElementById("recieveFirstName").value ==""){
        document.getElementById("recieveFirstNameAlert").style.display = "table-row";
        submitStatus = false;
        document.addTransferInside.number.focus();
    }else  document.getElementById("recieveFirstNameAlert").style.display= "none";

    if(document.getElementById("recieveLastName").value ==""){
        document.getElementById("recieveLastNameAlert").style.display = "table-row";
        submitStatus = false;
        document.addTransferInside.number.focus();
    }else  document.getElementById("recieveLastNameAlert").style.display= "none";

    if(document.getElementById("recievePosition").value ==""){
        document.getElementById("recievePositionAlert").style.display = "table-row";
        submitStatus = false;
        document.addTransferInside.number.focus();
    }else  document.getElementById("recievePositionAlert").style.display= "none";

    if(submitStatus == true){
        saveDetail();
    }
}

function submitButtonClick(){
    
    submitStatus = true;
    if(document.getElementById("title").value ==""){
        document.getElementById("titleAlert").style.display = "table-row";
        submitStatus = false;
        document.addTransferInside.title.focus();
    }else  document.getElementById("titleAlert").style.display= "none";

    if(document.getElementById("number").value ==""){
        document.getElementById("numberAlert").style.display = "table-row";
        submitStatus = false;
        document.addTransferInside.number.focus();
    }else  document.getElementById("numberAlert").style.display= "none";

    if(document.getElementById("approveDate").value ==""){
        document.getElementById("approveDateAlert").style.display = "table-row";
        submitStatus = false;
        document.addTransferInside.approveDate.focus();
    }else  document.getElementById("approveDateAlert").style.display= "none";

    if(document.getElementById("approverFirstName").value ==""){
        document.getElementById("approverFirstNameAlert").style.display = "table-row";
        submitStatus = false;
        document.addTransferInside.approverFirstName.focus();
    }else  document.getElementById("approverFirstNameAlert").style.display= "none";

    if(document.getElementById("approverLastName").value ==""){
        document.getElementById("approverLastNameAlert").style.display = "table-row";
        submitStatus = false;
        document.addTransferInside.approverLastName.focus();
    }else  document.getElementById("approverLastNameAlert").style.display= "none";

    if(document.getElementById("approverPosition").value ==""){
        document.getElementById("approverPositionAlert").style.display = "table-row";
        submitStatus = false;
        document.addTransferInside.approverPosition.focus();
    }else  document.getElementById("approverPositionAlert").style.display= "none";

    return submitStatus;
}
