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

var detail = {
	'code' : "00000",
	'quantity' : 0,
	'description' : "des",
  	'withdrawerNmae' : "",
  	'withdrawerLastname' : "",
  	'withdrawerPosition' : "",
  	'requisitionId': -1
};

function addDetailButton(){
	document.getElementById("code").value = '';
	document.getElementById("groupCode").value = '';
	document.getElementById("quantity").value = '';
	document.getElementById("withdrawer").value = '';
	document.getElementById("withdrawerPosition").value = ''
	document.getElementById("addWindows").style.display = "none";
	document.getElementById("addDetailWindows").style.display = "block";
	document.getElementById("titleInHeader").innerHTML = "เพิ่มรายละเอียดการเบิกจ่าย"
	document.getElementById("code").focus();
}

function addOrderButton(){
	document.getElementById("addWindows").style.display = "block";
	document.getElementById("addDetailWindows").style.display = "none";
	document.getElementById("titleInHeader").innerHTML = "เพิ่มรายการเบิกจ่าย"
}

function update(){
	requisition.title = document.getElementById("title").value;
	requisition.number = document.getElementById("number").value;
}

function updateDetail(){
	detail.quantity = document.getElementById("quantity").value;
	//detail.description = document.getElementById("description").value;
	detail.code = document.getElementById("code").value;
  	detail.withdrawerNmae = document.getElementById("withdrawer").value;
  	detail.withdrawerLastname = document.getElementById("withdrawerLastname").value;
  	detail.withdrawerPosition = document.getElementById("withdrawerPosition").value;
  	detail.requisitionId = requisition.id;

}

function getDetail(id){
	$.ajax({
		type: "GET",
		url: "/export/order/loadDetail",
		data: {'id': id},
		success: function(data){
		   	//details = JSON.stringify(data);
		    if(data["status"] == "SUCCESS"){
			   	var tr = data["details"];
			   	var arrayLength = tr.length;
			   	var s = "";
			   	destroyTable();
				for (var i = 0; i < arrayLength; i++) {
					s += '<tr>';
					s += '				<th>'+(i+1)+'</th>';
					if(tr[i].code){
						s += '				<th>'+ tr[i].code.code +'</th>';
						s += '				<th>'+ tr[i].code.description +'</th>';
						s += '				<th>'+ tr[i].quantity+'</th>';
						s += '				<th>'+ tr[i].code.classifier +'</th>';
					}
					else{
						s += '				<th> null </th>';
						s += '				<th> null </th>';
						s += '				<th>'+tr[i].quantity+'</th>';
						s += '				<th> null </th>';
					}
					if(tr[i].withdrawer){
						s += '				<th>'+ tr[i].withdrawer.firstName + ' ' + tr[i].withdrawer.lastName +'</th>';
					}
					else{
						s += '				<th> null </th>';
					}
					s += '				<th> </th>';
					s += '</tr>';
			   	}
			   	document.getElementById("detailInTable").innerHTML = s;
			   	updateTable();
			}
			else{
		    	alert("get detail error : " + data["message"]);
		    }
		}
	});
}

function saveDetail(){
	updateDetail();
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
	    		getDetail(requisition.id);
	    	}
	    	else{
	    		alert('save detail error : ' + result["message"]);
	    	}
    	}
	});
}

function init(id){
	requisition.id = id;
	getDetail(id);
	document.addOrder.title.focus();
}

function initViewDetial(id){
	document.getElementById("title").disabled = true;
	document.getElementById("number").disabled = true;
	document.getElementById("approveDate").disabled = true;
	document.getElementById("firstName").disabled = true;
	document.getElementById("lastName").disabled = true;
	document.getElementById("position").disabled = true;
	document.getElementById("approverName").disabled = true;
	document.getElementById("approverLastName").disabled = true;
	document.getElementById("approverPosition").disabled = true;

	document.getElementById("datepickerbutton").style.display = "none";
	document.getElementById("editDetail").style.display = "none";

	init(id);
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