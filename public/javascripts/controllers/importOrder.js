var i =1;
var j =1;
var k =1;
var aiLists = [];
var eoLists = [];
var supplyList=[];
var procumentDetailsTick = [];
var procurementStatus = "null";


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
    			procumentStatus = result["status"];
    			if(procurementStatus == "UNCHANGE"){
    				$('#editBtn2').prop('disabled',true);	
    			}else{
    				$('#b2').text('แก้ไข ').append($('<span class="glyphicon glyphicon-ok"></span>'));
    				$('#editBtn2').show();
    			}
    			$('#b2').hide();
    			$('#page2 input').prop('disabled', true);
    			$('#isEditingOn').val('false');
    		}else if(page == 3){
    				
    			if(tab == 1){
    				for(var i = 0; i < result.subDetails.length; i++){
    					var subDetails = $('#sub'+(i+1)+' :input');
    					$.each(subDetails, function(j, field) {
        					if(j<8){
        						$(field).val(result.subDetails[i][j]);
        						//console.log(result.subDetails[i][j]);
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
    			var isEditing = $('#isEditingOn').val();
    			var isDisabled = true;
    			if(isEditing == 'true'){
    				isDisabled = false;
    			}
    			$('#spreadSupply input').prop('disabled', isDisabled);
    			$('#spreadSupply select').prop('disabled', isDisabled);
    		}
    	},
    	statusCode:{
	    	500: function(response){
	    		//console.log(response.responseText);
	    		var mywindow = window.open('', 'my div', 'height=400,width=600');
	            /*optional stylesheet*/ //mywindow.document.write('<link rel="stylesheet" href="main.css" type="text/css" />');
	            mywindow.document.write(response.responseText);
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
	    dom.val("").prop("disabled",false);
	});
	$.each(fields4, function(i, field) {
		var dom = $(field);
		dom.val(0).prop("disabled",false);
	});
	$.each(fields6, function(i, field) {
		var dom = $(field);
		dom.prop('checked', false).prop("disabled",false);
	});
	
	$('#editBtn2').hide();
	$('#b2').show();
	$('#b2').text('ยืนยัน ').append($('<span class="glyphicon glyphicon-ok"></span>'));
	$('#procurementDetailId').val("");
}

function submitDetail(path){
	var data = {};
	var fields1 = $('#page2 :input');
	var fields2 = $('#spreadSupply :input');
	var spreadLength = 9;
	var maxLength;
	if(document.getElementById('typeOfGoods') != null){
		spreadLength = 8
	}
	console.log(spreadLength);
	maxLength = spreadLength * 100;
	
	if(fields2.length > maxLength){
		for(var index = 0;index < fields2.length; index+=maxLength){
			data = {};
			if(index == 0){
				data['initFlag'] = "1";
			}else{
				data['initFlag'] = "0";
			}
			data['fracment'] = "true";
			data['index'] = index/spreadLength;
			$.each(fields1,function(i,field){
				var dom = $(field);
				data[dom.attr('name')] = dom.val();
			});
			for(var i=index; i< index+maxLength; i++){
				var dom = $(fields2.get(i));
				data[dom.attr('name')] = dom.val();
			}
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
					$('#procurementDetailId').val("");
				},
				async:   false,
				statusCode:{
					500: function(response){
						//console.log(response.responseText);
						
						var mywindow = window.open('', 'my div', 'height=400,width=600');
						/*optional stylesheet*/ //mywindow.document.write('<link rel="stylesheet" href="main.css" type="text/css" />');
						mywindow.document.write(response.responseText);
					}
				}
			});
		}
	}else{
		data['fracment'] = "false";
		data['initFlag'] = "-1";
		$.each(fields1,function(i,field){
			var dom = $(field);
			data[dom.attr('name')] = dom.val();
		});
		console.log(fields2.length);
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
				$('#procurementDetailId').val("");
			},
			statusCode:{
				500: function(response){
					//console.log(response.responseText);
					
					var mywindow = window.open('', 'my div', 'height=400,width=600');
					/*optional stylesheet*/ //mywindow.document.write('<link rel="stylesheet" href="main.css" type="text/css" />');
					mywindow.document.write(response.responseText);
				}
			}
		});
	}
}

function getCommitteeTemplate(name){
	
	
	
	var num = name == 'ai' ? i:j;
        setI(num);
        console.log(name == 'ai' ? 'i':'j');
	var s ='				<th style ="text-align:center;">'+
	'					    <input id="'+name+'PrefixName'+ num +'" name="'+name+'PrefixName'+ num +'"  type="text"  class="form-control textAlignCenter width75px" placeholder="ใส่ค่า">'+
	'				</th>'+
	'				<th style ="text-align:center;">'+
	'					    <input id="'+name+'FirstName'+ num +'" name="'+name+'FirstName'+ num +'" type="text" class="form-control textAlignCenter width175px" placeholder="ใส่ค่า" onkeyup="mapInput(this.id)">'+
	'				</th>'+
	'				<th style ="text-align:center;">'+
	'					    <input id="'+name+'LastName'+ num +'" name="'+name+'LastName'+ num +'" type="text" class="form-control textAlignCenter width175px" placeholder="ใส่ค่า">'+
	'				</th>'+
	'				<th style ="text-align:center;">'+
	'					    <input id="'+name+'Position'+ num +'" name="'+name+'Position'+ num +'" type="text" class="form-control textAlignCenter width175px" placeholder="ตามข้อมูลuser">'+
	'				</th>'+
	'				'+
	'				<th style ="text-align:center;">'+
	'						<select name="'+name+'CommitteeType'+ num +'" class="form-control textAlignCenter width175px">'+
	'						  <option>---เลือก---</option>'+
	'						  <option>ข้าราชการ</option>'+
	'						  <option>พนักงานมหาลัย</option>'+
	'						</select>'+
	'				</th>'+
	'				'+
	'				<th style ="text-align:center;">'+
	'						<select name="'+name+'CommitteePosition'+ num +'" class="form-control textAlignCenter width175px">'+
	'						  <option>---เลือก---</option>'+
	'						  <option>ประธานกรรมการ</option>'+
	'						  <option>กรรมการ</option>'+
	'						  <option>กรรมการและเลขานุการ</option>'+
	'						</select>'+
	'				 </th>'+
	'				 <th>'+
	'				 <button type="button" class="btn btn btn-danger" onclick="removeDivCommittee(\''+name+'\','+ num +')">ลบ</button>'+
	'				 </th>';
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
		var v1;
		var v='<div id="sub'+k+'">'
		
		if(val.length>5)
		{
		v1='  <div class="form-inline marginBtm1" role="form" align="left" style="display:inline-table">'+
		'	        	<div class="form-group" >'+
		'	        		<div class="input-group"> '+
		'		                <span class="input-group-addon">สาขา</span>'+
		'		                    <select class="form-control textAlignCenter  width300px" name="'+name+'Department'+k+'" id="'+name+'Department'+k+'">'+
		'		                        <option>สาขาวิชาการละคอน</option>'+
		'		                        <option>สาขาวิชาศิลปะการออกแบบพัสตราภรณ์</option>'+
		'		                        <option>สาขาวิชาศิลปะการออกแบบอุตสาหกรรม</option>'+
		'		                        <option>สำนักงานเลขานุการ</option>'+
		'		                    </select>'+
		'		            </div>'+
		'	        	</div>'
		}
		else
		{
		v1='  <div class="form-inline marginBtm1" role="form" align="left" style="display:inline-table">'+
		'	        	<div class="form-group" >'+
		'	        		<div class="input-group"> '+
		'		                <span class="input-group-addon">สาขา</span>'+
		'		                    <select class="form-control textAlignCenter  width300px" name="'+name+'Department'+k+'" id="'+name+'Department'+k+'">'+
		'		                        <option>ห้องพัสดุ</option>'+
		'		                    </select>'+
		'		            </div>'+
		'	        	</div>'	
		}

		var v15='	        	<div class="form-group" >'+
		'	                <div class="input-group" >'+
		'	                    <span class="input-group-addon" >ห้อง</span>'+
		'	                    <input type="text" class="form-control textAlignCenter  width75px" name="'+name+'Room'+k+'" id="'+name+'Room'+k+(val.length==5? '" value="คลังพัสดุ">':'" value="">')+
		'	                </div>'+
		'	            </div>'+
		'	        	<div class="form-group" >'+
		'	                <div class="input-group" >'+
		'	                    <span class="input-group-addon" >ชั้น</span>'+
		'	                    <input type="text" class="form-control textAlignCenter  width50px" name="'+name+'Level'+k+'" id="'+name+'Level'+k+(val.length==5? '" value="2">':'" value="">')+
		'	                </div>'+
		'	            </div>';
if(val.length >5)
{
		var v15x='	        	<div class="form-group" >'+
		'	                <div class="input-group" >'+
		'	                    <span class="input-group-addon" >'+(name=='article'? 'รหัสFSN':'รหัสวัสดุ')+'</span>'+
		'	                    <input type="text" class="form-control textAlignCenter  '+
						(name=='article'? 'width225px"placeholder="ศก.พ.57-7400-100-0005(02/05)"':'width225px"placeholder="ศก.พ.57-01000(02/05)"')+
						' name="'+name+'FSNCode'+k+'" id="'+name+'FSNCode'+k+'" value="ศก.'+years+"-"+val+"("+(k>9?k:"0"+k)+"/"+(num>9?num:"0"+num)+")"+'">'+
		'	                </div>'+
		'	            </div>';
}
else
{
	var v15x='	        	<div class="form-group" >'+
	'	                <div class="input-group" >'+
	'	                    <span class="input-group-addon" >'+(name=='article'? 'รหัสFSN':'รหัสวัสดุ')+'</span>'+
	'	                    <input type="text" class="form-control textAlignCenter  '+
								(name=='article'? 'width225px"placeholder="ศก.พ.57-7400-100-0005(02/05)"':'width225px"placeholder="ศก.พ.57-01000(02/05)"')+
	' name="'+name+'FSNCode'+k+'" id="'+name+'FSNCode'+k+'" value="'+val+"("+(k>9?k:"0"+k)+"/"+(num>9?num:"0"+num)+")"+'">'+
	'	                </div>'+
	'	            </div>';
}
		
		var v16='	            <div class="form-group" >'+
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
	v=v+v1+v15+v15x+v16+v2+v3;
else
	v=v+v1+v15+v15x+v16+v3;

		
		
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
			document.getElementById("row"+name).style.color = "";
			document.getElementById("check"+name).checked = false;
		}else{
			procumentDetailsTick.push(procumentDetailName);
			document.getElementById("row"+name).style.color = "#cc3300";
			document.getElementById("check"+name).checked = true;
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
		divTable += '		 <tr id=row'+data['data'][i].id+'>'+
		'                    <th><input id="check'+ data['data'][i].id +'" type="checkbox" onclick="addTick('+ data['data'][i].id +')"></th>'+
		'                    <th onclick="addTick('+ data['data'][i].id +')">'+ data['data'][i].id +'</th>'+
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
		divTable += '		 <tr id=row'+data['data'][i].id+'>'+
		'                    <th><input id="check'+ data['data'][i].id +'" type="checkbox" onclick="addTick('+ data['data'][i].id +')"></th>'+
		'                    <th onclick="addTick('+ data['data'][i].id +')">'+ data['data'][i].code +'</th>'+
		'                    <th>'+ data['data'][i].description +'</th>'+
		'                    <th>'+ data['data'][i].quantity +'</th>'+
		'                    <th>'+ data['data'][i].classifier +'</th>'+
		'                    <th>'+ data['data'][i].price +'</th>'+
		'<th>';
		if(data['data'][i].fileType != null && data['data'][i].fileType.contains("image")){
			divTable+='<a href="/assets/'+data['data'][i].path+'"><img src="/assets/'+data['data'][i].path+'" alt="'+data['data'][i].fileName+'" style="width:40px;height:40px"></a>';
		}else{
			divTable+='<a href="'+data['data'][i].path+'" download>'+data['data'][i].fileName+'</a>';
		}
		
		
		divTable +='</th>'+
		'                    <th> <button type="button" class="btn btn-xs btn-info" onclick="setDetail('+data['data'][i].id + ',2)" > รายละเอียด</button></th>'+
		'                </tr>';
	}
	destroyTable();
	document.getElementById("goodList").innerHTML = divTable;
	updateTable();
}


function createAICommittee() {
    destroyTable();
    
    var table=document.getElementById("ai_committee");
    var tr = table.insertRow();
    
    name = 'ai'
    aiLists.push(i);
    var num =i;
    
    tr.id=name+num;
    
    console.log(tr.id);
    
    document.getElementById(tr.id).innerHTML += getCommitteeTemplate(name);
    
    updateTable();
    
    document.getElementById("aiLists").value = aiLists.join();
    
    if($('#orderStatus').val() == "SUCCESS" || $('#orderStatus').val() == "UNCHANGE"){
    	$('#ai_committee :input').prop('disabled', true);
    	$('#AiButton').prop('disabled', true);
		$('#ai_committee select').prop('disabled', true);
    }else{
    	$('#ai_committee input').prop('disabled', false);
    	$('#AiButton').prop('disabled', false);
		$('#ai_committee select').prop('disabled', false);
    }
    initAutoCompleteName();
}

function createEOCommittee(){
	/*
	var dv = document.createElement("tr")
	dv.innerHTML=getCommitteeTemplate('eo');
    document.getElementById("eo_committee").appendChild(dv);
    */
    destroyTable();
    
    var table=document.getElementById("eo_committee");
    var tr = table.insertRow();
    
    name = 'eo'
    eoLists.push(j);
    var num = j;
    
    tr.id=name+num;
    
    console.log(tr.id);
    
    document.getElementById(tr.id).innerHTML += getCommitteeTemplate(name);
    
    updateTable();
    
    document.getElementById("eoLists").value = eoLists.join();

    if($('#orderStatus').val() == "SUCCESS" || $('#orderStatus').val() == "UNCHANGE"){
    	$('#eo_committee :input').prop('disabled', true);
    	$('#EoButton').prop('disabled', true);
		$('#eo_committee select').prop('disabled', true);
    }else{
    	$('#eo_committee input').prop('disabled', false);
    	$('#EoButton').prop('disabled', false);
		$('#eo_committee select').prop('disabled', false);
    }
    
    initAutoCompleteNameEo();
    
}
function removeDivCommittee(name,num){
	destroyTable();
	if(name == 'ai'){
		aiLists.remove(num);
		document.getElementById("ai"+num).remove();
		document.getElementById("aiLists").value = aiLists.join();
	}else if(name == 'eo'){
		eoLists.remove(num);
		document.getElementById("eo"+num).remove();
		document.getElementById("eoLists").value = eoLists.join();
	}
	updateTable();
}
function submitToNext(){
    
    submitNext = true;

    if(document.getElementById("description").value ==""){
        document.getElementById("descriptionAlert").style.display = "table-row";
        submitNext = false;
    }else  document.getElementById("descriptionAlert").style.display= "none";

    if(document.getElementById("code").value==""){
        document.getElementById("codeAlert").style.display = "table-row";
        submitNext = false;
    }else  document.getElementById("codeAlert").style.display= "none";

    if(document.getElementById("price").value ==""){
        document.getElementById("priceAlert").style.display = "table-row";
        submitNext = false;
    }else  document.getElementById("priceAlert").style.display= "none";

    if(document.getElementById("priceNoVat").value ==""){
        document.getElementById("priceNoVatAlert").style.display = "table-row";
        submitNext = false;
    }else  document.getElementById("priceNoVatAlert").style.display= "none";

    if(document.getElementById("quantity").value==""){
        document.getElementById("quantityAlert").style.display = "table-row";
        submitNext = false;
    }else  document.getElementById("quantityAlert").style.display= "none";

//    if(document.getElementById("seller").value ==""){
//        document.getElementById("sellerAlert").style.display = "table-row";
//        submitNext = false;
//    }else  document.getElementById("sellerAlert").style.display= "none";
//
//    if(document.getElementById("phone").value ==""){
//        document.getElementById("phoneAlert").style.display = "table-row";
//        submitNext = false;
//    }else  document.getElementById("phoneAlert").style.display= "none";
//    
    if(submitNext == false){
        document.getElementById("description").focus();
    }
    else{
        showPage('3');
        preSpread('good');
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
