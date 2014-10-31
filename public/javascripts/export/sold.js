$(function () {
    $('#dateP').datetimepicker({
  	  language:'th'
    })
});

var auction = {
	'id': 0,
	'title': "",
	'contractNo': ""
};

var newDetail = [];
var oldDetail = [];
var checkedDetail = [];
var isViewDetail = false;

var titleInHeader = "เพิ่มรายการจำหน่าย";

function addDetailButton(){
	newDetail = [];
	destroyTable();
	document.getElementById("searchResultTable").innerHTML = "";
	updateTable();
	document.getElementById("addWindows").style.display = "none";
	document.getElementById("addDetailWindows").style.display = "block";
	document.getElementById("titleInHeader").innerHTML = "เพิ่มรายละเอียดการจำหน่าย";
	document.getElementById("fsnCode").focus();
}

function addSoldButton(){
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
							s += '	<th>'+ allArticles[i].detail.price + ' / ' + allArticles[i].remainingPriceToString +'</th>';
							s += '	<th>'+ allArticles[i].detail.llifeTime + ' ปี / ' + allArticles[i].remainLifetimeToString +'</th>';
							s += '	<th>'+ allArticles[i].department + '</th>';
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
		url: "/export/sold/loadDetail",
		data: {'id': auction.id},
		success: function(data){
		   	//alert(JSON.stringify(data));
		   	var summaryTotal = 0
			var summaryBudgetType = [];
			var summaryRemaining = [];

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
					var budgetType = details[i].durableArticles.detail.procurement.budgetType;
					var remaining = details[i].durableArticles.remaining;
					summaryTotal += remaining;
					if(budgetType){
						var position = summaryBudgetType.indexOf(budgetType);
						if(position > -1){
							summaryRemaining[position] += remaining;
						}
						else{
							summaryBudgetType.push(budgetType);
							summaryRemaining.push(remaining);
						}
					}
					s += '	<th>'+ budgetType +'</th>';
					s += '	<th>'+ remaining.toFixed(2) +'</th>';
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

	   		var summary = '';
		   	summary += '<div class="input-group width300px">';
		   	summary += '  <span class="input-group-addon width100px">ราคารวม</span>';
        	summary += '    <input type="text" class="form-control width150px" value="' + summaryTotal.toFixed(2) +'" disabled>';
        	summary += '  <span class="input-group-addon" > บาท </span>';
      		summary += '</div>';
      		for (var i = 0; i < summaryBudgetType.length; i++) {
          		summary += '<div class="input-group width300px">';
			   	summary += '  <span class="input-group-addon width100px">งบ' + summaryBudgetType[i] + '</span>';
            	summary += '    <input type="text" class="form-control width150px" value="' + summaryRemaining[i].toFixed(2) +'" disabled>';
            	summary += '  <span class="input-group-addon" > บาท </span>';
          		summary += '</div>';
          	}
			document.getElementById("summaryTable").innerHTML = summary;
		}
	});
}

function saveDetail(){
	var dataDetail = {};
	dataDetail.id = auction.id;
	dataDetail.detail = newDetail;
	$.ajax({
		url:'/export/sold/saveDetail',
	    type: 'post',
	    data: JSON.stringify(dataDetail),
	    contentType: 'application/json',
	    dataType: 'json',
    	success: function(result){
    		var status = result["status"];
		    if(status == "SUCCESS"){
	    		document.getElementById("fsnCode").value = "";
				document.getElementById("fsnDescription").value = "";
				addSoldButton();
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
	dataDetail.id = auction.id;
	dataDetail.detail = checkedDetail;
	$.ajax({
		url:'/export/sold/deleteDetail',
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
	auction.id = id;
	getDetail();
	addSoldButton();
}

function initViewDetial(id){
	isViewDetail = true;
	document.getElementById("title").disabled = true;
	document.getElementById("contractNo").disabled = true;
	document.getElementById("approveDate").disabled = true;
	document.getElementById("soldDestination").disabled = true;
	document.getElementById("buildingNo").disabled = true;
	document.getElementById("village").disabled = true;
	document.getElementById("alley").disabled = true;
	document.getElementById("road").disabled = true;
	document.getElementById("parish").disabled = true;
	document.getElementById("district").disabled = true;
	document.getElementById("province").disabled = true;
	document.getElementById("telephoneNumber").disabled = true;
	document.getElementById("fax").disabled = true;
	document.getElementById("postCode").disabled = true;
	document.getElementById("email").disabled = true;

	document.getElementById("initViewDetial").style.display = "none";
	document.getElementById("editDetail").style.display = "none";
	document.getElementById("saveExport").style.display = "none";

	init(id);
	titleInHeader = "แสดงรายละเอียดการการจำหน่าย";
	document.getElementById("titleInHeader").innerHTML = titleInHeader;
}

function changeToEdit(){
	isViewDetail = false;
	document.getElementById("title").disabled = false;
	document.getElementById("contractNo").disabled = false;
	document.getElementById("approveDate").disabled = false;
	document.getElementById("soldDestination").disabled = false;
	document.getElementById("buildingNo").disabled = false;
	document.getElementById("village").disabled = false;
	document.getElementById("alley").disabled = false;
	document.getElementById("road").disabled = false;
	document.getElementById("parish").disabled = false;
	document.getElementById("district").disabled = false;
	document.getElementById("province").disabled = false;
	document.getElementById("telephoneNumber").disabled = false;
	document.getElementById("fax").disabled = false;
	document.getElementById("postCode").disabled = false;
	document.getElementById("email").disabled = false;

	document.getElementById("initViewDetial").style.display = "table-cell";
	document.getElementById("editDetail").style.display = "block";
	document.getElementById("saveExport").style.display = "block";
	document.getElementById("changeToEditButton").style.display = "none";

	titleInHeader = "แก้ไขรายละเอียดการการจำหน่าย";
	document.getElementById("titleInHeader").innerHTML = titleInHeader;
}