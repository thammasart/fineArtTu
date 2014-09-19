var requisition = {
	'id': 0,
	'title': "",
	'number': ""
};

var detail = {
	'quantity' : 0,
	'description' : "des",
	'code' : "00000",
  	'withdrawer' : "test01",
  	'requisitionId': -1
};

var details = [];

function init(id){
	requisition.id = id;
	getDetail(id);
	document.addOrder.title.focus();
}

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
  	detail.withdrawer = document.getElementById("withdrawer").value;
  	detail.requisitionId = requisition.id;

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
		url: "/export/order/lodeDetail",
		data: {'id': id},
//		cache: false,
		success: function(data){
			//alert(JSON.stringify(data));
		   	details = JSON.stringify(data);
		   	var tr = data["details"];

		   	var arrayLength = tr.length;
		   	var s = "";
			for (var i = 0; i < arrayLength; i++) {
				s += '<tr>' +
					 '				<th>'+(i+1)+'</th>' +
					 '				<th>'+tr[i].code+'</th>' +
					 '				<th>'+tr[i].code+'</th>' +
					 '				<th>'+tr[i].quantity+'</th>' +
					 '				<th>'+tr[i].code+'</th>' +
					 '				<th>'+tr[i].withdrawer+'</th>' +
					 '</tr>';
		   	}
		   	//alert(s);
		   	document.getElementById("detailInTable").innerHTML = s;


		}
	});
}




