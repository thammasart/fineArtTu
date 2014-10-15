var internalTransfer = {
	'id': 0,
	'title': "",
	'number': ""
};

var newDetail = [];	
var oldDetail = [];

function addDetailButton(){
	document.getElementById("addWindows").style.display = "none";
	document.getElementById("addDetailWindows").style.display = "block";
	document.addDetail.room.focus();
}

function addInternalTransferButton(){
	document.getElementById("addWindows").style.display = "block";
	document.getElementById("addDetailWindows").style.display = "none";
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
		   	var status = data["status"];
		    if(status == "SUCCESS"){
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
		    	alert("find FSN :" + data["message"]);
		    }
		}
	});
}

function getDetail(){
	$.ajax({
		type: "GET",
		url: "/export/transferInside/loadDetail",
		data: {'id': internalTransfer.id},
		success: function(data){
		   	//alert(JSON.stringify(data));
		   	var status = data["status"];
		    if(status == "SUCCESS"){
			   	var details = data["details"];
			   	var detailLength = details.length;
			   	var s = "";
			   	destroyTable();
			   	oldDetail = [];
				for (var i = 0; i < detailLength; i++) {
					oldDetail.push(details[i].durableArticles.id);
					s += '<tr>';
					s += '	<th>'+(i+1)+'</th>';
					s += '	<th>'+ details[i].durableArticles.code +'</th>';
					if(details[i].durableArticles.detail){
						s += '	<th>'+ details[i].durableArticles.detail.fsn.descriptionDescription +'</th>';
					}
					else{
						s += '	<th>'+ 'ไม่มี' +'</th>';
					}
					s += '	<th>'+ details[i].department +'</th>';
					s += '	<th>'+ details[i].floorLevel +'</th>';
					s += '	<th>'+ details[i].room +'</th>';
					s += '</tr >';
			   	}
			   	document.getElementById("detailInTable").innerHTML = s;
			   	updateTable();
		    }
		    else{
		    	alert("get detail :" + data["message"]);
		    }
		}
	});
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

function saveDetail(){
	var dataDetail = {};
	dataDetail.id = internalTransfer.id;
	dataDetail.department = document.getElementById("department").value;
	dataDetail.room = document.getElementById("room").value;
	dataDetail.floorLevel = document.getElementById("floorLevel").value;
	dataDetail.detail = newDetail;
	$.ajax({
		url:'/export/transferInside/saveDetail',
	    type: 'post',
	    data: JSON.stringify(dataDetail),
	    contentType: 'application/json',
	    dataType: 'json',
    	success: function(result){
    		//alert(JSON.stringify(result));
    		document.getElementById("fsnCode").value = "";
			document.getElementById("fsnDescription").value = "";
			document.getElementById("department").value = "";
			document.getElementById("room").value = "";
			document.getElementById("floorLevel").value = "";
			addInternalTransferButton();
			newDetail = [];
			getDetail();
    	}
	});

}

function init(id){
	internalTransfer.id = id;
	addInternalTransferButton();
	getDetail();
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
