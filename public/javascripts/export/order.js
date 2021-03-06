$(function () {
    $('#dateP').datetimepicker({
  	  language:'th'
    })
});

var requisition = {
	'id': 0,
	'title': "",
	'number': ""
};

var detail = {};
var detailEdit = {};
var details = [];
var checkedDetail = [];
var isViewDetail = false;
var titleInHeader = "เพิ่มรายการเบิกจ่าย";

function addDetailButton(){
	document.getElementById("code").value = '';
	document.getElementById("groupCode").value = '';
	document.getElementById("quantity").value = '';
	document.getElementById("description").value = '';
	document.getElementById("withdrawer").value = '';
	document.getElementById("withdrawerPosition").value = '';
	document.getElementById("withdrawerPosition").value = '';
	document.getElementById("addWindows").style.display = "none";
	document.getElementById("addDetailWindows").style.display = "block";
	document.getElementById("editDetailWindows").style.display = "none";
	document.getElementById("titleInHeader").innerHTML = "เพิ่มรายละเอียดการเบิกจ่าย";
	document.getElementById("code").focus();
}

function addOrderButton(){
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
	document.getElementById("codeEdit").value = detailEdit.code.code;
	document.getElementById("groupCodeEdit").value = detailEdit.code.description;
	document.getElementById("quantityEdit").value = detailEdit.quantity;
	document.getElementById("descriptionEdit").value = detailEdit.description;
  	document.getElementById("withdrawerEdit").value = detailEdit.withdrawer.firstName;
  	document.getElementById("withdrawerLastnameEdit").value = detailEdit.withdrawer.lastName;
  	document.getElementById("withdrawerPositionEdit").value = detailEdit.withdrawer.position;
}

function update(){
	requisition.title = document.getElementById("title").value;
	requisition.number = document.getElementById("number").value;
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
		url: "/export/order/loadDetail",
		data: {'id': requisition.id},
		async:   false,
		success: function(data){
			//details = JSON.stringify(data);
			if(data["status"] == "SUCCESS"){
				details = data["details"]; 
				var arrayLength = details.length;
				var s = "";
				destroyTable();
				for (var i = 0; i < arrayLength; i++) {
					s += '<tr id="detailRow' + details[i].id + '">';
					s += ' <th onclick="addCheckedDetail(' + details[i].id + ')">' +
					' <input type="checkbox" id="detail' + details[i].id + '"> </th>';
					s += ' <th onclick="addCheckedDetail(' + details[i].id + ')">'+(i+1)+'</th>';
					if(details[i].code){
						s += ' <th onclick="addCheckedDetail(' + details[i].id + ')"> '+ details[i].code.code +'</th>';
						s += ' <th onclick="addCheckedDetail(' + details[i].id + ')">'+ details[i].code.description +'</th>';
						s += ' <th>'+ details[i].quantity+'</th>';
						s += ' <th>'+ details[i].code.classifier +'</th>';
					}
					else{
						s += ' <th> null </th>';
						s += ' <th> null </th>';
						s += ' <th>'+details[i].quantity+'</th>';
						s += ' <th> null </th>';
					}
					if(details[i].withdrawer){
						s += ' <th>'+ details[i].withdrawer.firstName + ' ' + details[i].withdrawer.lastName +'</th>';
					}
					else{
						s += ' <th> null </th>';
					}
					s += ' <th>'+details[i].description+'</th>';
					s += ' <th onclick="editDetail('+ details[i].id + ')"> <button type="button" class="btn btn-xs btn-warning" id="edit'+i+'"> แก้ไข </button> </th>';
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
	detail.code = document.getElementById("code").value;
	detail.quantity = document.getElementById("quantity").value;
	detail.description = document.getElementById("description").value;
  	detail.withdrawerNmae = document.getElementById("withdrawer").value;
  	detail.withdrawerLastname = document.getElementById("withdrawerLastname").value;
  	detail.withdrawerPosition = document.getElementById("withdrawerPosition").value;
  	detail.requisitionId = requisition.id;
	$.ajax({
		url:'/export/order/saveDetail',
	    type: 'post',
	    data: JSON.stringify(detail),
	    contentType: 'application/json',
	    dataType: 'json',
    	success: function(result){
    		var status = result["status"];
		    if(status == "SUCCESS"){
	    		addOrderButton()
	    		getDetail();
	    	}
	    	else{
	    		alert('save detail error : ' + result["message"]);
	    	}
    	}
	});
}

function saveEditDetail(){
	detailEdit.code = document.getElementById("codeEdit").value;
	detailEdit.quantity = document.getElementById("quantityEdit").value;
	detailEdit.description = document.getElementById("descriptionEdit").value;
  	detailEdit.withdrawerNmae = document.getElementById("withdrawerEdit").value;
  	detailEdit.withdrawerLastname = document.getElementById("withdrawerLastnameEdit").value;
  	detailEdit.withdrawerPosition = document.getElementById("withdrawerPositionEdit").value;
  	detailEdit.requisitionId = requisition.id;

	$.ajax({
		url:'/export/order/editDetail',
	    type: 'post',
	    data: JSON.stringify(detailEdit),
	    contentType: 'application/json',
	    dataType: 'json',
    	success: function(result){
    		var status = result["status"];
		    if(status == "SUCCESS"){
	    		addOrderButton()
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
	dataDetail.id = requisition.id;
	dataDetail.detail = checkedDetail;
	$.ajax({
		url:'/export/order/deleteDetail',
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
	requisition.id = id;
	getDetail();
	addOrderButton();
	document.addOrder.title.focus();
}

function initViewDetial(id){
	isViewDetail = true;
	document.getElementById("title").disabled = true;
	document.getElementById("number").disabled = true;
	document.getElementById("date").disabled = true;
	document.getElementById("firstName").disabled = true;
	document.getElementById("lastName").disabled = true;
	document.getElementById("position").disabled = true;
	document.getElementById("approverName").disabled = true;
	document.getElementById("approverLastName").disabled = true;
	document.getElementById("approverPosition").disabled = true;

	document.getElementById("datepickerbutton").style.display = "none";
	document.getElementById("editDetail").style.display = "none";
	document.getElementById("saveExport").style.visibility = "hidden";

	init(id);
	titleInHeader = "แสดงรายละเอียดการเบิกจ่าย";
	document.getElementById("titleInHeader").innerHTML = titleInHeader;

	for(i=0; i< details.length; i++){
		document.getElementById("edit"+i).style.display = "none";
	}
}

function changeToEdit(){
	isViewDetail = false;
	document.getElementById("title").disabled = false;
	document.getElementById("number").disabled = false;
	document.getElementById("date").disabled = false;
	document.getElementById("firstName").disabled = false;
	document.getElementById("lastName").disabled = false;
	document.getElementById("position").disabled = false;
	document.getElementById("approverName").disabled = false;
	document.getElementById("approverLastName").disabled = false;
	document.getElementById("approverPosition").disabled = false;

	document.getElementById("datepickerbutton").style.display = "table-cell";
	document.getElementById("editDetail").style.display = "block";
	document.getElementById("saveExport").style.visibility = "visible";
	document.getElementById("changeToEditButton").style.display = "none";

	titleInHeader = "แก้ไขรายละเอียดการเบิกจ่าย";
	document.getElementById("titleInHeader").innerHTML = titleInHeader;

	for(i=0; i< details.length; i++){
		document.getElementById("edit"+i).style.display = "block";
	}
}


function validateSaveDetail(){
    var submit = true;
    if(document.getElementById("groupCode").value ==""){
        document.getElementById("groupCodeAlert").style.display = "table-row";
        submit= false;
    }else  document.getElementById("groupCodeAlert").style.display= "none";

    if(document.getElementById("code").value ==""){
        document.getElementById("codeAlert").style.display = "table-row";
        submit= false;
    }else  document.getElementById("codeAlert").style.display= "none";

    if(document.getElementById("quantity").value ==""){
        document.getElementById("quantityAlert").style.display = "table-row";
        submit= false;
    }else  document.getElementById("quantityAlert").style.display= "none";

    if(document.getElementById("withdrawer").value ==""){
        document.getElementById("withdrawerAlert").style.display = "table-row";
        submit= false;
    }else  document.getElementById("withdrawerAlert").style.display= "none";

    if(document.getElementById("withdrawerLastname").value ==""){
        document.getElementById("withdrawerLastnameAlert").style.display = "table-row";
        submit= false;
    }else  document.getElementById("withdrawerLastnameAlert").style.display= "none";

    if(document.getElementById("withdrawerPosition").value ==""){
        document.getElementById("withdrawerPositionAlert").style.display = "table-row";
        submit= false;
    }else  document.getElementById("withdrawerPositionAlert").style.display= "none";

    if(submit){
        saveDetail();
    }
}