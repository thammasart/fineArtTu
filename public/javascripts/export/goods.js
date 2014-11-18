$(function () {
    $('#dateP').datetimepicker({
  	  language:'th'
    })
});

var orderGoods = {
	'id': 0,
	'title': "",
	'number': ""
};

var newDetail = [];
var oldDetail = [];

var detail = {};
var detailEdit = {};
var details = [];
var checkedDetail = [];
var isViewDetail = false;
var titleInHeader = "เพิ่มรายการเบิกจ่าย";

function addDetailButton(){
	newDetail = [];
	destroyTable();
	document.getElementById("searchResultTable").innerHTML = "";
	updateTable();
	document.getElementById("fsnCode").value = '';
	document.getElementById("description").value = '';
	document.getElementById("addWindows").style.display = "none";
	document.getElementById("addDetailWindows").style.display = "block";
	document.getElementById("editDetailWindows").style.display = "none";
	document.getElementById("titleInHeader").innerHTML = "เพิ่มรายละเอียดการเบิกจ่าย";
	document.getElementById("fsnCode").focus();
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
	document.getElementById("edit_fsn_Number").innerHTML = detailEdit.goods.codes;
    document.getElementById("edit_fsn_description").innerHTML = detailEdit.goods.detail.description;
    document.getElementById("edit_price").innerHTML = detailEdit.goods.detail.price;


    var x  = document.getElementById("edit_sentToDepartment");
	for (var i = 0; i < x.length; i++) {
         if(detailEdit.department == x.options[i].value){
         	x.options[i].selected = "true";
         }
    }
	document.getElementById("edit_room").value = detailEdit.room ;
    document.getElementById("edit_floorLevel").value = detailEdit.floorLevel;
    document.getElementById("edit_firstName").value = detailEdit.firstName;
    document.getElementById("edit_lastName").value = detailEdit.lastName;
    document.getElementById("edit_position").value = detailEdit.position;
}

function update(){
	orderGoods.title = document.getElementById("title").value;
	orderGoods.number = document.getElementById("number").value;
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
		url: "/export/orderGoods/loadDetail",
		data: {'id': orderGoods.id},
		async:   false,
		success: function(data){
			//details = JSON.stringify(data);
			if(data["status"] == "SUCCESS"){
				details = data["details"]; 
				var arrayLength = details.length;
				var s = "";
				oldDetail = [];
				destroyTable();
				for (var i = 0; i < arrayLength; i++) {
					oldDetail.push(details[i].goods.id);
					s += '<tr id="detailRow' + details[i].id + '">';
					s += ' <th onclick="addCheckedDetail(' + details[i].id + ')">' +
					' <input type="checkbox" id="detail' + details[i].id + '"> </th>';
					s += ' <th onclick="addCheckedDetail(' + details[i].id + ')">'+(i+1)+'</th>';
					if(details[i].goods){
						s += ' <th onclick="addCheckedDetail(' + details[i].id + ')"> '+ details[i].goods.codes +'</th>';
						s += ' <th onclick="addCheckedDetail(' + details[i].id + ')">'+ details[i].goods.detail.description +'</th>';
					}
					else{
						s += ' <th> null </th>';
						s += ' <th> null </th>';
					}
					s += ' <th>'+ details[i].firstName+'</th>';
					s += ' <th>'+ details[i].lastName +'</th>';
					s += ' <th>'+ details[i].department +'</th>';
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
	var dataDetail = {};
	dataDetail.id = orderGoods.id;
	dataDetail.department = document.getElementById("department").value;
	dataDetail.room = document.getElementById("room").value;
	dataDetail.floorLevel = document.getElementById("floorLevel").value;
	dataDetail.recieveTitle = "---";//document.getElementById("recieveTitle").value;
	dataDetail.recieveFirstName = document.getElementById("recieveFirstName").value;
	dataDetail.recieveLastName = document.getElementById("recieveLastName").value;
	dataDetail.recievePosition = document.getElementById("recievePosition").value;
	dataDetail.detail = newDetail;

	$.ajax({
		url:'/export/orderGoods/saveDetail',
	    type: 'post',
	    data: JSON.stringify(dataDetail),
	    contentType: 'application/json',
	    dataType: 'json',
    	success: function(result){
    		var status = result["status"];
		    if(status == "SUCCESS"){
		    	document.getElementById("fsnCode").value = "";
				document.getElementById("description").value = "";
				document.getElementById("department").value = "";
				document.getElementById("room").value = "";
				document.getElementById("floorLevel").value = "";
				document.getElementById("recieveFirstName").value = "";
				document.getElementById("recieveLastName").value = "";
				document.getElementById("recievePosition").value = "";
				newDetail = [];
				
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
	detailEdit.department  = document.getElementById("edit_sentToDepartment").value;
	detailEdit.room  = document.getElementById("edit_room").value;
    detailEdit.floorLevel = document.getElementById("edit_floorLevel").value;
   	detailEdit.title = '---';//document.getElementById("recieveTitle").value;
  	detailEdit.firstName = document.getElementById("edit_firstName").value;
  	detailEdit.lastName = document.getElementById("edit_lastName").value;
  	detailEdit.position = document.getElementById("edit_position").value;
  	detailEdit.orderGoodsId = orderGoods.id;

	$.ajax({
		url:'/export/orderGoods/editDetail',
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
	dataDetail.id = orderGoods.id;
	dataDetail.detail = checkedDetail;
	$.ajax({
		url:'/export/orderGoods/deleteDetail',
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
	orderGoods.id = id;
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