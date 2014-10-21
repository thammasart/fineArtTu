var i =1;
var j =1;
var k =1;
var aiLists = [];
var eoLists = [];
var supplyList=[];
var procumentDetailsTick = [];


$('document').ready(function(){
	showPage('1');
	createAICommittee();
	if(document.getElementById('eo_committee')!=null)createEOCommittee();
});

$(function () {
    $('#addDate').datetimepicker({
  	  language:'th',
  	  pickTime: false,
    })
    
    if($('#addDate').val()!='')$('#addDate').data("DateTimePicker").setDate(new Date());
    $('#checkDate').datetimepicker({
  	  language:'th',
  	  pickTime: false,
    })
    if($('#checkDate').val()!='')$('#checkDate').data("DateTimePicker").setDate(new Date());
});
function retriveProcurement(id,tab){
	var obj = {
			"id" : id,
			"tab" : tab
	};
	$.ajax({
		url:'/import/order/retriveProcurement',
	    type: 'post',
	    data: JSON.stringify(obj),
	    contentType: 'application/json',
	    dataType: 'json',
    	success: function(result){
    		if(result.ai.length > 1){
    			for(var i = 0; i< result.ai.length-1; i++){
    				createAICommittee();
    			}
    		}
    		for(var i = 0; i< result.ai.length; i++){
    			var aiDiv = $('#ai'+(i+1) + ' :input');
    			$.each(aiDiv, function(j, field) {
    				if(j<7){
    					$(field).val(result.ai[i][j]);
    				}
    				if(j == 0 || j == 1 || j == 2 || j == 3 || j==4){
    					$(field).prop("disabled",true);
    				}
    			});
    		}
    		if(tab == 1){
    			if(result.eo.length > 1){
    				for(var i = 0; i< result.eo.length-1; i++){
    					createEOCommittee();
    				}
    			}
    			for(var i = 0; i< result.eo.length; i++){
    				var eoDiv = $('#eo'+(i+1) + ' :input');
    				$.each(eoDiv, function(j, field) {
    					if(j<7){
    						$(field).val(result.eo[i][j]);
    					}
    					if(j == 0 || j == 1 || j == 2 || j == 3 || j==4){
        					$(field).prop("disabled",true);
        				}
    				});
    			}
    			loadOrderArticle(result);
    		}else{
    			loadOrderGood(result);
    		}
    	}
	});
}
function setDetail(id,tab,page){
	var obj = {
			"id" : id,
			"tab" : tab
	};
	page = page||2;
	showPage(page);
	
	$.ajax({
		url:'/import/order/retriveProcurementDetail',
	    type: 'post',
	    data: JSON.stringify(obj), 
	    contentType: 'application/json',
	    dataType: 'json',
    	success: function(result){
    		if(page == 2){
    			$('#procurementDetailId').val(result["id"]);
    			$('#description').val(result["description"]);
    			$('#code').val(result["code"]);
    			$('#price').val(result["price"]);
    			$('#priceNoVat').val(result["priceNoVat"]);
    			$('#quantity').val(result["quantity"]);
    			$('#llifeTime').val(result["llifeTime"]);
    			$('#alertTime').val(result["alertTime"]);
    			$('#seller').val(result["seller"]);
    			$('#phone').val(result["phone"]);
    			$('#brand').val(result["brand"]);
    			$('#serialNumber').val(result["serialNumber"]);
    			$('#code').val(result["code"]);
    		}else if(page == 3){
    			if(tab == 1){
    				for(var i = 0; i < result.subDetails.length; i++){
    					var subDetails = $('#sub'+(i+1)+' :input');
    					$.each(subDetails, function(j, field) {
        					if(j<8){
        						$(field).val(result.subDetails[i][j]);
        						console.log(result.subDetails[i][j]);
        					}
        				});
    				}
    			}else if(tab == 2){
    				for(var i = 0; i < result.subDetails.length; i++){
    					var subDetails = $('#sub'+(i+1)+' :input');
    					$.each(subDetails, function(j, field) {
        					if(j<8){
        						$(field).val(result.subDetails[i][j]);
        					}
        				});
    				}
    			}
    		}
    	}
	});
	
}
function cancelStatus(id,typeOfOrder){
	var data = {
			"id" : id,
			"typeOfOrder" : typeOfOrder
	}
	$.ajax({
		url:'/import/order/cancel',
	    type: 'post',
	    data: JSON.stringify(data),
	    contentType: 'application/json',
	    dataType: 'json',
    	success: function(result){
    		alert(result);
    	}
	});
}
function clearPage(){
	procumentDetailsTick = [];
	var fields2 = $('#page2 :input[type="text"]');
	var fields4 = $('#page2 :input[type="number"]');
	var fields6 = $('#page2 :input[type="radio"]');
	$.each(fields2, function(i, field) {
	    var dom = $(field);
	    dom.val("");
	});
	$.each(fields4, function(i, field) {
		var dom = $(field);
		dom.val(0);
	});
	$.each(fields6, function(i, field) {
		var dom = $(field);
		dom.prop('checked', false);
	});
	
}

function submitDetail(path){
	var data = {}
	var fields1 = $('#page2 :input');
	var fields2 = $('#page3 :input');
	$.each(fields1,function(i,field){
		var dom = $(field);
		data[dom.attr('name')] = dom.val();
	});
	$.each(fields2,function(i,field){
		var dom = $(field);
		data[dom.attr('name')] = dom.val();
	});
	
	$.ajax({
		url: path,
	    type: 'post',
	    data: JSON.stringify(data),
	    contentType: 'application/json',
	    dataType: 'json',
    	success: function(result){
    		if(result["type"] == "article"){
    			loadOrderArticle(result);
    		}else{
    			loadOrderGood(result);
    		}
    	}
	});
}

function getCommitteeTemplate(name){
	name == 'ai' ? aiLists.push(i):eoLists.push(j);
	var num = name == 'ai' ? i:j;
	console.log(name == 'ai' ? 'i':'j');
	var s = '<div id="'+name + num +'" style="margin-bottom:1%;display:inline-table">'+
	'				<div class="form-group" >'+
	'					<div class="input-group" >'+
	'					    <span class="input-group-addon">คำนำหน้าชื่อ</span>'+
	'					    <input name="'+name+'PrefixName'+ num +'"  type="text" class="form-control textAlignCenter  width100px"placeholder="ใส่ค่า">'+
	'					</div>'+
	'				</div>'+
	'				<div class="form-group" >'+
	'					<div class="input-group" >'+
	'					    <span class="input-group-addon">ชื่อ</span>'+
	'					    <input name="'+name+'FirstName'+ num +'" type="text" class="form-control textAlignCenter  width100px"placeholder="ใส่ค่า">'+
	'					</div>'+
	'				</div>'+
	'				<div class="form-group" >'+
	'					<div class="input-group" >'+
	'					    <span class="input-group-addon">สกุล</span>'+
	'					    <input name="'+name+'LastName'+ num +'" type="text" class="form-control textAlignCenter  width125px"placeholder="ใส่ค่า">'+
	'					</div>'+
	'				</div>'+
	'				<div class="form-group" >'+
	'					<div class="input-group" >'+
	'					    <span class="input-group-addon">หมายเลขบัตรประจำตัวประชาชน</span>'+
	'					    <input name="'+name+'PersonalID'+ num +'" type="text" class="form-control textAlignCenter  width100px"placeholder="ใส่ค่า">'+
	'					</div>'+
	'				</div>'+
	'				<div class="form-group" >'+
	'					<div class="input-group" >'+
	'					    <span class="input-group-addon" >ตำแหน่ง</span>'+
	'					    <input name="'+name+'Position'+ num +'" type="text" class="form-control textAlignCenter  width150px"placeholder="ตามข้อมูลuser">'+
	'					</div>'+
	'				</div>'+
	'				'+
	'				'+
	'				<div class="form-group" role="form">'+
	'				    <div class="input-group">'+
	'						<span class="input-group-addon">ประเภทกรรมการ</span>'+
	'						<select name="'+name+'CommitteeType'+ num +'" class="form-control textAlignCenter  width150px">'+
	'						  <option>---เลือก---</option>'+
	'						  <option>ข้าราชการ</option>'+
	'						  <option>พนักงานมหาลัย</option>'+
	'						</select>'+
	'				    </div>'+
	'				</div>'+
	'				'+
	'				<div class="form-group" role="form">'+
	'					<div class="input-group">'+
	'						<span class="input-group-addon">ตำแหน่งในคณะกรรมการ</span>'+
	'						<select name="'+name+'CommitteePosition'+ num +'" class="form-control textAlignCenter  width200px">'+
	'						  <option>---เลือก---</option>'+
	'						  <option>ประธานกรรมการ</option>'+
	'						  <option>กรรมการ</option>'+
	'						  <option>กรรมการและเลขานุการ</option>'+
	'						</select>'+
	'					</div>'+
	'				 </div>'+
	'				 <button type="button" onclick="removeDivCommittee(\''+name+'\','+ num +')">ลบ</button>'+
	'			 </div> <!-- id ='+ name + num +' -->'
	name == 'ai' ? i++ : j++;

	return s;
}

function preSpread(name){
	var num = document.getElementById("quantity").value;
        var val = document.getElementById("code").value;
        var years = document.getElementById("years").value;
	document.getElementById("fixNumber").value=num;
	
	var ss = "";
	for(k=1;k<=num;k++)
	{
		var v='<div id="sub'+k+'">'+
		'  <div class="form-inline marginBtm1" role="form" align="left" style="display:inline-table">'+
		''+
		'	        	<div class="form-group" >'+
		'	        		<div class="input-group"> '+
		'		                <span class="input-group-addon">สาขา</span>'+
		'		                    <select class="form-control textAlignCenter  width300px" name="'+name+'Department'+k+'" id="'+name+'Department'+k+'">'+
		'		                        <option>สาขาวิชาการละคอน</option>'+
		'		                        <option>สาขาวิชาศิลปะการออกแบบพัสตราภรณ์</option>'+
		'		                        <option>สาขาวิชาศิลปะการออกแบบอุตสาหกรรม</option>'+
		'		                        <option>สำนักงานเลขานุการ</option>'+
		'		                        <option>ห้องพัสดุ</option>'+
		'		                    </select>'+
		'		            </div>'+
		'	        	</div>'+
		''+
		'	        	<div class="form-group" >'+
		'	                <div class="input-group" >'+
		'	                    <span class="input-group-addon" >ห้อง</span>'+
		'	                    <input type="text" class="form-control textAlignCenter  width75px" name="'+name+'Room'+k+'" id="'+name+'Room'+k+'">'+
		'	                </div>'+
		'	            </div>'+
		'	        	<div class="form-group" >'+
		'	                <div class="input-group" >'+
		'	                    <span class="input-group-addon" >ชั้น</span>'+
		'	                    <input type="text" class="form-control textAlignCenter  width50px" name="'+name+'Level'+k+'" id="'+name+'Level'+k+'">'+
		'	                </div>'+
		'	            </div>'+
		'	        	<div class="form-group" >'+
		'	                <div class="input-group" >'+
		'	                    <span class="input-group-addon" >'+(name=='article'? 'รหัสFSN':'รหัสวัสดุ')+'</span>'+
		'	                    <input type="text" class="form-control textAlignCenter  '+
						(name=='article'? 'width225px"placeholder="ศก.พ.57-7400-100-0005(02/05)"':'width225px"placeholder="ศก.พ.57-01000(02/05)"')+
						' name="'+name+'FSNCode'+k+'" id="'+name+'FSNCode'+k+'" value="ศก.'+years+"-"+val+"("+(k>9?k:"0"+k)+"/"+(num>9?num:"0"+num)+")"+'">'+
		'	                </div>'+
		'	            </div>'+
		'	            <div class="form-group" >'+
		'	                <div class="input-group" >'+
		'	                    <span class="input-group-addon" >คำนำหน้าชื่อ</span>'+
		'	                    <input type="text" class="form-control textAlignCenter  width100px"placeholder="ใส่ค่า" name="'+name+'PrefixName'+k+'" id="'+name+'PrefixName'+k+'">'+
		'	                </div>'+
		'	            </div>'+
		'	            <div class="form-group" >'+
		'	                <div class="input-group" >'+
		'	                    <span class="input-group-addon" >ชื่อ</span>'+
		'	                    <input type="text" class="form-control textAlignCenter  width100px"placeholder="ใส่ค่า" name="'+name+'FirstName'+k+'" id="'+name+'FirstName'+k+'">'+
		'	                </div>'+
		'	            </div>'+
		'	            <div class="form-group" >'+
		'	                <div class="input-group" >'+
		'	                    <span class="input-group-addon" >สกุล</span>'+
		'	                    <input type="text" class="form-control textAlignCenter  width125px"placeholder="ใส่ค่า" name="'+name+'LastName'+k+'" id="'+name+'LastName'+k+'">'+
		'	                </div>'+
		'	            </div>'
	
if(name=='article')
{
var v2=	'		        <div class="form-group" >'+
		'			        <div class="input-group" >'+
		'			            <span class="input-group-addon" >รหัสจากคลัง</span>'+
		'			            <input type="text" class="form-control textAlignCenter  width100px" placeholder="ใส่ค่า" name="'+name+'Stock'+k+'" id="'+name+'Stock'+k+'">'+
		'			        </div>'+
		'		        </div>'
}
		
var v3 ='	            <button onclick="setValueBelow(\''+name+'\','+ k +')">ตกลง</button>'+
		'	        </div>  '+
		'		</div>'
if(name=='article')
	v=v+v2+v3;
else
	v=v+v3;

		
		
		ss=ss+v;
	}
	document.getElementById("spreadSupply").innerHTML=ss;
	var id = $('#procurementDetailId').val();
	if(id != ""){
		if(name == 'article'){
			setDetail(id, 1, 3);
		}else{
			setDetail(id, 2, 3);
		}
	}
}

function setValueBelow(name,num){
	for(var tmp=num+1; tmp<k; tmp++){
		document.getElementById(name+'Department'+tmp).value = document.getElementById(name+'Department'+num).value;  //use id
		document.getElementById(name+'Room'+tmp).value = document.getElementById(name+'Room'+num).value;
		document.getElementById(name+'Level'+tmp).value = document.getElementById(name+'Level'+num).value;
		document.getElementById(name+'PrefixName'+tmp).value = document.getElementById(name+'PrefixName'+num).value;
		document.getElementById(name+'FirstName'+tmp).value = document.getElementById(name+'FirstName'+num).value;
		document.getElementById(name+'LastName'+tmp).value = document.getElementById(name+'LastName'+num).value;
		if(name=='article')
		document.getElementById(name+'Stock'+tmp).value = document.getElementById(name+'Stock'+num).value;
	}
}


function addTick(name){
	var procumentDetailName = name;
	console.log(procumentDetailsTick);
	if(procumentDetailsTick.indexOf(procumentDetailName) > -1){
		procumentDetailsTick.remove(procumentDetailName);
	}else{
		procumentDetailsTick.push(procumentDetailName);
	}
}

function removeProcurementDetail(path){
	console.log(procumentDetailsTick.toString());
	var parseData = {
			'parseData'	: procumentDetailsTick.toString() 	
	};
	console.log(parseData);
	
	$.ajax({
		url: path,
	    type: 'post',
	    data: JSON.stringify(parseData), ////
	    contentType: 'application/json',
	    dataType: 'json',
    	success: function(result){
    		if(result["type"] == "article"){
    			procumentDetailsTick = []
    			loadOrderArticle(result);
    		}else{
    			procumentDetailsTick = []
    			loadOrderGood(result);
    		}
    	}
	});
	
}

function loadOrderArticle(data){
	var divTable = '';
	for(var i = 0; i<data["data"].length; i++){
		divTable += '				<tr id='+i+'>'+
		'                    <th><input id="'+ data['data'][i].id +'" type="checkbox" onclick="addTick('+ data['data'][i].id +')"></th>'+
		'                    <th>'+ data['data'][i].id +'</th>'+
		'                    <th>'+ data['data'][i].fsn +'</th>'+
		'                    <th>'+ data['data'][i].description +'</th>'+
		'                    <th>'+ data['data'][i].quantity +'</th>'+
		'                    <th>'+ data['data'][i].classifier +'</th>'+
		'                    <th>'+ data['data'][i].price +'</th>'+
		'                    <th>'+ data['data'][i].lifeTime +'</th>'+
		'<th>';
		
		if(data['data'][i].fileType != null && data['data'][i].fileType.contains("image")){
			divTable+='<a href="/assets/'+data['data'][i].path+'"><img src="/assets/'+data['data'][i].path+'" alt="'+data['data'][i].fileName+'" style="width:40px;height:40px"></a>';
		}else{
			divTable+='<a href="'+data['data'][i].path+'" download>'+data['data'][i].fileName+'</a>';
		}
		
		
		divTable +='</th>'+
		'                    <th> <button type="button" class="btn btn-xs btn-info" onclick="setDetail('+data['data'][i].id + ',1)" > รายละเอียด</button></th>'+
		'                </tr>';
	}
	destroyTable();
	document.getElementById("durableArticleList").innerHTML = divTable;
	updateTable();
}

function loadOrderGood(data){
	var divTable = '';
	for(var i = 0; i<data["data"].length; i++){
		divTable += '				<tr id='+i+'>'+
		'                    <th><input id="'+ data['data'][i].id +'" type="checkbox" onclick="addTick('+ data['data'][i].id +')"></th>'+
		'                    <th>'+ data['data'][i].code +'</th>'+
		'                    <th>'+ data['data'][i].description +'</th>'+
		'                    <th>'+ data['data'][i].quantity +'</th>'+
		'                    <th>'+ data['data'][i].classifier +'</th>'+
		'                    <th>'+ data['data'][i].price +'</th>'+
		'                    <th> <button type="button" class="btn btn-xs btn-info" onclick="setDetail('+data['data'][i].id + ',2)" > รายละเอียด</button></th>'+
		'                </tr>';
	}
	destroyTable();
	document.getElementById("goodList").innerHTML = divTable;
	updateTable();
}


function createAICommittee() {
	var dv = document.createElement("div")
	dv.innerHTML=getCommitteeTemplate('ai');
    document.getElementById("ai_committee").appendChild(dv);
    document.getElementById("aiLists").value = aiLists.join();
}

function createEOCommittee(){
	var dv = document.createElement("div")
	dv.innerHTML=getCommitteeTemplate('eo');
    document.getElementById("eo_committee").appendChild(dv);
    document.getElementById("eoLists").value = eoLists.join();
}
function removeDivCommittee(name,num){
	if(name == 'ai'){
		aiLists.remove(num);
		document.getElementById("ai"+num).remove();
		document.getElementById("aiLists").value = aiLists.join();
	}else if(name == 'eo'){
		eoLists.remove(num);
		document.getElementById("eo"+num).remove();
		document.getElementById("eoLists").value = eoLists.join();
	}
}

function showPage(num){
	document.getElementById("page1").style.display = num == '1' ?  "block" : "none";
	document.getElementById("page2").style.display = num == '2' ?  "block" : "none";
	document.getElementById("page3").style.display = num == '3' ?  "block" : "none";
}
function submitButtonClickAr(){
    
    submitStatus = true;
    if(document.getElementById("title").value==""){
        document.getElementById("titleAlert").style.display = "table-row";
        submitStatus = false;
        document.saveOrderArticle.title.focus();
    }else  document.getElementById("titleAlert").style.display= "none";

    if(document.getElementById("contractNo").value ==""){
        document.getElementById("contractNoAlert").style.display = "table-row";
        submitStatus = false;
        document.saveOrderArticle.contractNo.focus();
    }else  document.getElementById("contractNoAlert").style.display= "none";

    if(document.getElementById("addDate_p").value ==""){
        document.getElementById("addDate_pAlert").style.display = "table-row";
        submitStatus = false;
    }else  document.getElementById("addDate_pAlert").style.display= "none";

    if(document.getElementById("checkDate_p").value==""){
        document.getElementById("checkDate_pAlert").style.display = "table-row";
        submitStatus = false;
    }else  document.getElementById("checkDate_pAlert").style.display= "none";

    if(document.getElementById("years").value ==""){
        document.getElementById("yearsAlert").style.display = "table-row";
        submitStatus = false;
    }else  document.getElementById("yearsAlert").style.display= "none";

    return submitStatus;
}
