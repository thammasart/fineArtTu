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

var details = [];

function addDetailButton(){
	document.getElementById("addWindows").style.display = "none";
	document.getElementById("addDetailWindows").style.display = "block";
	document.addDetail.groupCode.focus();
}

function addOrderButton(){
	document.getElementById("addWindows").style.display = "block";
	document.getElementById("addDetailWindows").style.display = "none";
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

function saveDetail(){
	updateDetail();
	$.ajax({
		url:'/export/order/saveDetail',
	    type: 'post',
	    data: JSON.stringify(detail),
	    contentType: 'application/json',
	    dataType: 'json',
    	success: function(result){
    		addOrderButton()
    		getDetail(requisition.id);
    		//alert(result);
    		//alert("success");
    	}
	});
}

function getDetail(id){
	$.ajax({
		type: "GET",
		url: "/export/order/loadDetail",
		data: {'id': id},
		success: function(data){
		   	details = JSON.stringify(data);
		   	var tr = data["details"];
		   	var arrayLength = tr.length;
		   	var s = "";
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
		}
	});
}

function init(id){
	requisition.id = id;
	getDetail(id);
	document.addOrder.title.focus();
}




