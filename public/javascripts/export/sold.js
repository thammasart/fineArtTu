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

function add_FF_committree(){
	var i = parseInt(document.getElementById("numberOf_FF_committee").value);
	destroyTable();

	var table = document.getElementById("FF_committee_table");
    var tr = table.insertRow();
    tr.id = 'FF'+i;

	var s = '<th style ="text-align:center;" > <input id="FF_namePrefix'+i+'" name="FF_namePrefix'+i+'" type="text" class="form-control textAlignCenter width75px" placeholder="ใส่ค่า"> </th>';
	   s += '<th style ="text-align:center;" > <input id="FF_firstName'+i+'" name="FF_firstName'+i+'" type="text" class="form-control textAlignCenter width175px" placeholder="ใส่ค่า"> </th>';
	   s += '<th style ="text-align:center;" > <input id="FF_lastName'+i+'" name="FF_lastName'+i+'" type="text" class="form-control textAlignCenter width175px" placeholder="ใส่ค่า"> </th>';
	   s += '<th style ="text-align:center;" > <input id="FF_position'+i+'" name="FF_position'+i+'" type="text" class="form-control textAlignCenter width175px" placeholder="ใส่ค่า"> </th>';
	   s += '<th style ="text-align:center;" > ';
	   s += '  <select id="FF_cType'+i+'" name="FF_cType'+i+'" class="form-control textAlignCenter width175px">';
	   s += '    <option>---เลือก---</option>';
	   s += '    <option>ข้าราชการ</option>';
	   s += '    <option>พนักงานมหาลัย</option>';
	   s += '  </select>';
	   s += '</th>';
	   s += '<th style ="text-align:center;" > ';
	   s += '  <select id="FF_cPosition'+i+'" name="FF_cPosition'+i+'" class="form-control textAlignCenter width175px">';
	   s += '    <option>---เลือก---</option>';
	   s += '    <option>ประธานกรรมการ</option>';
	   s += '    <option>กรรมการ</option>';
	   s += '    <option>กรรมการและเลขานุการ</option>';
	   s += '  </select>';
	   s += '</th> ';
	   s += '<th style ="text-align:center;" > <button type="button" class="btn btn btn-danger" id="FF_delete'+i+'" onclick="delete_FF_committree('+i+')"> ลบ </button> </th>';

	document.getElementById(tr.id).innerHTML = s;
	updateTable();
	i++;
	document.getElementById("numberOf_FF_committee").value = i;
}

function delete_FF_committree(num){
	destroyTable();
	document.getElementById("FF"+num).remove();
	updateTable();
}

function add_E_committree(){
	var i = parseInt(document.getElementById("numberOf_E_committee").value);
	destroyTable();

	var table = document.getElementById("E_committee_table");
    var tr = table.insertRow();
    tr.id = 'E'+i;

    var s = '<th style ="text-align:center;" > <input id="E_namePrefix'+i+'" name="E_namePrefix'+i+'" type="text" class="form-control textAlignCenter width75px" placeholder="ใส่ค่า"> </th>';
	   s += '<th style ="text-align:center;" > <input id="E_firstName'+i+'" name="E_firstName'+i+'" type="text" class="form-control textAlignCenter width175px" placeholder="ใส่ค่า"> </th>';
	   s += '<th style ="text-align:center;" > <input id="E_lastName'+i+'" name="E_lastName'+i+'" type="text" class="form-control textAlignCenter width175px" placeholder="ใส่ค่า"> </th>';
	   s += '<th style ="text-align:center;" > <input id="E_position'+i+'" name="E_position'+i+'" type="text" class="form-control textAlignCenter width175px" placeholder="ใส่ค่า"> </th>';
	   s += '<th style ="text-align:center;" > ';
	   s += '  <select id="E_cType'+i+'" name="E_cType'+i+'" class="form-control textAlignCenter width175px">';
	   s += '	  <option>---เลือก---</option>';
	   s += '	  <option>ข้าราชการ</option>';
	   s += '	  <option>พนักงานมหาลัย</option>';
	   s += '  </select>';
	   s += '</th>';
	   s += '<th style ="text-align:center;" > ';
	   s += '  <select id="E_cPosition'+i+'" name="E_cPosition'+i+'" class="form-control textAlignCenter width175px">';
	   s += '    <option>---เลือก---</option>';
	   s += '    <option>ประธานกรรมการ</option>';
	   s += '    <option>กรรมการ</option>';
	   s += '    <option>กรรมการและเลขานุการ</option>';
	   s += '  </select>';
	   s += '</th> ';
	   s += '<th style ="text-align:center;" > <button type="button" class="btn btn btn-danger" id="E_delete'+i+'" onclick="delete_E_committree('+i+')"> ลบ </button> </th>';
  
	document.getElementById('E'+i).innerHTML = s;
	updateTable();
	i++;
	document.getElementById("numberOf_E_committee").value = i;
}

function delete_E_committree(num){
	destroyTable();
	document.getElementById("E"+num).remove();
	updateTable();
}

function add_D_committree(){
	var i = parseInt(document.getElementById("numberOf_D_committee").value);
	destroyTable();

	var table = document.getElementById("D_committee_table");
    var tr = table.insertRow();
    tr.id = 'D'+i;

    var s = '<th style ="text-align:center;" > <input id="D_namePrefix'+i+'" name="D_namePrefix'+i+'" type="text" class="form-control textAlignCenter width75px" placeholder="ใส่ค่า"> </th>';
	   s += '<th style ="text-align:center;" > <input id="D_firstName'+i+'" name="D_firstName'+i+'" type="text" class="form-control textAlignCenter width175px" placeholder="ใส่ค่า"> </th>';
	   s += '<th style ="text-align:center;" > <input id="D_lastName'+i+'" name="D_lastName'+i+'" type="text" class="form-control textAlignCenter width175px" placeholder="ใส่ค่า"> </th>';
	   s += '<th style ="text-align:center;" > <input id="D_position'+i+'" name="D_position'+i+'" type="text" class="form-control textAlignCenter width175px" placeholder="ใส่ค่า"> </th>';
	   s += '<th style ="text-align:center;" > ';
	   s += '  <select id="D_cType'+i+'" name="D_cType'+i+'" class="form-control textAlignCenter width175px">';
	   s += '    <option>---เลือก---</option>';
	   s += '    <option>ข้าราชการ</option>';
	   s += '    <option>พนักงานมหาลัย</option>';
	   s += '  </select>';
	   s += '</th>';
	   s += '<th style ="text-align:center;" > ';
	   s += '  <select id="D_cPosition'+i+'" name="D_cPosition'+i+'" class="form-control textAlignCenter width175px">';
	   s += '    <option>---เลือก---</option>';
	   s += '    <option>ประธานกรรมการ</option>';
	   s += '    <option>กรรมการ</option>';
	   s += '    <option>กรรมการและเลขานุการ</option>';
	   s += '  </select>';
	   s += '</th> ';
	   s += '<th style ="text-align:center;" > <button type="button" class="btn btn btn-danger" id="D_delete'+i+'" onclick="delete_D_committree('+i+')"> ลบ </button> </th>';

	document.getElementById(tr.id).innerHTML = s;
	updateTable();
	i++;
	document.getElementById("numberOf_D_committee").value = i;
}

function delete_D_committree(num){
	destroyTable();
	document.getElementById("D"+num).remove();
	updateTable();
}

function init(id){
	auction.id = id;
	getDetail();
	addSoldButton();

	var i;
	i = parseInt(document.getElementById("numberOf_FF_committee").value);
	if(i == 0 ){
		add_FF_committree();
	}
	i = parseInt(document.getElementById("numberOf_E_committee").value);
	if(i == 0 ){
		add_E_committree();
	}
	i = parseInt(document.getElementById("numberOf_D_committee").value);
	if(i == 0 ){
		add_D_committree();
	}
}

function initViewDetial(id){
	isViewDetail = true;
	document.getElementById("title").disabled = true;
	document.getElementById("contractNo").disabled = true;
	document.getElementById("approveDate").disabled = true;
	document.getElementById("soldDestination").disabled = true;
	// document.getElementById("buildingNo").disabled = true;
	// document.getElementById("village").disabled = true;
	// document.getElementById("alley").disabled = true;
	// document.getElementById("road").disabled = true;
	// document.getElementById("parish").disabled = true;
	// document.getElementById("district").disabled = true;
	// document.getElementById("province").disabled = true;
	// document.getElementById("telephoneNumber").disabled = true;
	// document.getElementById("fax").disabled = true;
	// document.getElementById("postCode").disabled = true;
	// document.getElementById("email").disabled = true;

	document.getElementById("initViewDetial").style.display = "none";
	document.getElementById("editDetail").style.display = "none";
	document.getElementById("saveExport").style.display = "none";
	document.getElementById("FF_add").style.display = "none";
	document.getElementById("E_add").style.display = "none";
	document.getElementById("D_add").style.display = "none";

	init(id);
	titleInHeader = "แสดงรายละเอียดการการจำหน่าย";
	document.getElementById("titleInHeader").innerHTML = titleInHeader;

	var i;
	i = parseInt(document.getElementById("numberOf_FF_committee").value);
	if(i > 0 ){
		for(j=0; j<i; j++){
			document.getElementById("FF_namePrefix"+j.toString()).disabled = true;
			document.getElementById("FF_firstName"+j.toString()).disabled = true;
			document.getElementById("FF_lastName"+j.toString()).disabled = true;
			document.getElementById("FF_position"+j.toString()).disabled = true;
			document.getElementById("FF_cType"+j.toString()).disabled = true;
			document.getElementById("FF_cPosition"+j.toString()).disabled = true;
			document.getElementById("FF_delete"+j.toString()).style.display = "none";
		}
	}
	i = parseInt(document.getElementById("numberOf_E_committee").value);
	if(i > 0 ){
		for(j=0; j<i; j++){
			document.getElementById("E_namePrefix"+j.toString()).disabled = true;
			document.getElementById("E_firstName"+j.toString()).disabled = true;
			document.getElementById("E_lastName"+j.toString()).disabled = true;
			document.getElementById("E_position"+j.toString()).disabled = true;
			document.getElementById("E_cType"+j.toString()).disabled = true;
			document.getElementById("E_cPosition"+j.toString()).disabled = true;
			document.getElementById("E_delete"+j.toString()).style.display = "none";
		}
	}
	i = parseInt(document.getElementById("numberOf_D_committee").value);
	if(i > 0 ){
		for(j=0; j<i; j++){
			document.getElementById("D_namePrefix"+j.toString()).disabled = true;
			document.getElementById("D_firstName"+j.toString()).disabled = true;
			document.getElementById("D_lastName"+j.toString()).disabled = true;
			document.getElementById("D_position"+j.toString()).disabled = true;
			document.getElementById("D_cType"+j.toString()).disabled = true;
			document.getElementById("D_cPosition"+j.toString()).disabled = true;
			document.getElementById("D_delete"+j.toString()).style.display = "none";
		}
	}
}

function changeToEdit(){
	isViewDetail = false;
	document.getElementById("title").disabled = false;
	document.getElementById("contractNo").disabled = false;
	document.getElementById("approveDate").disabled = false;
	document.getElementById("soldDestination").disabled = false;
	// document.getElementById("buildingNo").disabled = false;
	// document.getElementById("village").disabled = false;
	// document.getElementById("alley").disabled = false;
	// document.getElementById("road").disabled = false;
	// document.getElementById("parish").disabled = false;
	// document.getElementById("district").disabled = false;
	// document.getElementById("province").disabled = false;
	// document.getElementById("telephoneNumber").disabled = false;
	// document.getElementById("fax").disabled = false;
	// document.getElementById("postCode").disabled = false;
	// document.getElementById("email").disabled = false;

	document.getElementById("initViewDetial").style.display = "table-cell";
	document.getElementById("editDetail").style.display = "block";
	document.getElementById("saveExport").style.display = "block";
	document.getElementById("changeToEditButton").style.display = "none";
	document.getElementById("FF_add").style.display = "block";
	document.getElementById("E_add").style.display = "block";
	document.getElementById("D_add").style.display = "block";

	titleInHeader = "แก้ไขรายละเอียดการการจำหน่าย";
	document.getElementById("titleInHeader").innerHTML = titleInHeader;

	var i;
	i = parseInt(document.getElementById("numberOf_FF_committee").value);
	if(i > 0 ){
		for(j=0; j<i; j++){
			document.getElementById("FF_namePrefix"+j.toString()).disabled = false;
			document.getElementById("FF_firstName"+j.toString()).disabled = false;
			document.getElementById("FF_lastName"+j.toString()).disabled = false;
			document.getElementById("FF_position"+j.toString()).disabled = false;
			document.getElementById("FF_cType"+j.toString()).disabled = false;
			document.getElementById("FF_cPosition"+j.toString()).disabled = false;
			document.getElementById("FF_delete"+j.toString()).style.display = "block";
		}
	}
	i = parseInt(document.getElementById("numberOf_E_committee").value);
	if(i > 0 ){
		for(j=0; j<i; j++){
			document.getElementById("E_namePrefix"+j.toString()).disabled = false;
			document.getElementById("E_firstName"+j.toString()).disabled = false;
			document.getElementById("E_lastName"+j.toString()).disabled = false;
			document.getElementById("E_position"+j.toString()).disabled = false;
			document.getElementById("E_cType"+j.toString()).disabled = false;
			document.getElementById("E_cPosition"+j.toString()).disabled = false;
			document.getElementById("E_delete"+j.toString()).style.display = "block";
		}
	}
	i = parseInt(document.getElementById("numberOf_D_committee").value);
	if(i > 0 ){
		for(j=0; j<i; j++){
			document.getElementById("D_namePrefix"+j.toString()).disabled = false;
			document.getElementById("D_firstName"+j.toString()).disabled = false;
			document.getElementById("D_lastName"+j.toString()).disabled = false;
			document.getElementById("D_position"+j.toString()).disabled = false;
			document.getElementById("D_cType"+j.toString()).disabled = false;
			document.getElementById("D_cPosition"+j.toString()).disabled = false;
			document.getElementById("D_delete"+j.toString()).style.display = "block";
		}
	}
}