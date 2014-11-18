$(function () {
    $('#dateP').datetimepicker({
  	  language:'th'
    })
});

var donation = {
	'id': 0,
	'title': "",
	'number': ""
};

var newDetail = [];
var oldDetail = [];
var checkedDetail = [];
var isViewDetail = false;

var titleInHeader = "เพิ่มรายการบริจาค";

function addDetailButton(){
	newDetail = [];
	destroyTable();
	document.getElementById("searchResultTable").innerHTML = "";
	updateTable();
	document.getElementById("addWindows").style.display = "none";
	document.getElementById("addDetailWindows").style.display = "block";
	document.getElementById("titleInHeader").innerHTML = "เพิ่มรายละเอียดการบริจาค";
	document.getElementById("fsnCode").focus();
}

function addDonateButton(){
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
		url: "/export/donate/loadDetail",
		data: {'id': donation.id},
		success: function(data){
		   	//alert(JSON.stringify(data));
		    if(data["status"] == "SUCCESS"){
			   	var details = data["details"];
			   	var detailLength = details.length;
			   	var s = "";
			   	destroyTable();
			   	oldDetail = [];
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
						s += '	<th>'+'ไม่มี'+'</th>';
					}
					s += '	<th>'+details[i].durableArticles.remainingPriceToString+'</th>';
					s += '	<th>'+details[i].durableArticles.department+'</th>';
					s += '	<th>'+details[i].durableArticles.detail.procurement.checkDate+'</th>';
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
	dataDetail.id = donation.id;
	dataDetail.detail = newDetail;
	$.ajax({
		url:'/export/donate/saveDetail',
	    type: 'post',
	    data: JSON.stringify(dataDetail),
	    contentType: 'application/json',
	    dataType: 'json',
    	success: function(result){
    		var status = result["status"];
		    if(status == "SUCCESS"){
	    		document.getElementById("fsnCode").value = "";
				document.getElementById("fsnDescription").value = "";
				addDonateButton();
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
	dataDetail.id = donation.id;
	dataDetail.detail = checkedDetail;
	$.ajax({
		url:'/export/donate/deleteDetail',
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
        initAutocomplete("FF",i);
	i++;
	document.getElementById("numberOf_FF_committee").value = i;
}

function delete_FF_committree(num){
	destroyTable();
	document.getElementById("FF"+num).remove();
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
        initAutocomplete("D",i);
	i++;
	document.getElementById("numberOf_D_committee").value = i;
}

function init(id){
	donation.id = id;
	getDetail();
	addDonateButton();

	var i;
	i = parseInt(document.getElementById("numberOf_FF_committee").value);
	if(i == 0 ){
		add_FF_committree();
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
	document.getElementById("donateDestination").disabled = true;

	document.getElementById("datepickerbutton").style.display = "none";
	document.getElementById("editDetail").style.display = "none";
	document.getElementById("saveExport").style.visibility = "hidden";
	document.getElementById("FF_add").style.display = "none";
	document.getElementById("D_add").style.display = "none";
	
	init(id);
	titleInHeader = "แสดงรายละเอียดการบริจาค";
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
	document.getElementById("donateDestination").disabled = false;

	document.getElementById("datepickerbutton").style.display = "table-cell";
	document.getElementById("editDetail").style.display = "block";
	document.getElementById("saveExport").style.visibility = "visible";
	document.getElementById("changeToEditButton").style.display = "none";
	document.getElementById("FF_add").style.display = "block";
	document.getElementById("D_add").style.display = "block";

	titleInHeader = "แก้ไขรายละเอียดการบริจาค";
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
