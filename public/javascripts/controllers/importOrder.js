var i =1;
var j =1;
var k =1;
var aiLists = [];
var eoLists = [];
var supplyList=[];

$('document').ready(function(){
	showPage('1');
	createAICommittee();
	if(document.getElementById('eo_committee')!=null)createEOCommittee();
});
$(function () {
    $('#addDate').datetimepicker({
  	  language:'th'
    })
});
$(function () {
    $('#checkDate').datetimepicker({
  	  language:'th'
    })
});
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
	var s = '<div id="'+name + num +'">'+
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
	
	document.getElementById("fixNumber").value=num;
	
	var ss= document.getElementById("spreadSupply").innerHTML;
	ss = ""
	for(k=1;k<=num;k++)
	{
		var v='  <div class="form-inline marginBtm1" role="form" align="left">'+
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
						(name=='article'? 'width225px"placeholder="ศก.พ.57-7400-100-0005(02/05)"':'width150px"placeholder="ศก.พ.57-01000(02/05)"')+
						' name="'+name+'FSNCode'+k+'" id="'+name+'FSNCode'+k+'">'+
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
		'	                    <input type="text" class="form-control textAlignCenter  width100px"placeholder="ใส่ค่า" name="'+name+'FirstName'+k+'" id="'+name+'Name'+k+'">'+
		'	                </div>'+
		'	            </div>'+
		'	            <div class="form-group" >'+
		'	                <div class="input-group" >'+
		'	                    <span class="input-group-addon" >สกุล</span>'+
		'	                    <input type="text" class="form-control textAlignCenter  width125px"placeholder="ใส่ค่า" name="'+name+'LastName'+k+'" id="'+name+'Name'+k+'">'+
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
		'	        </div>  '
if(name=='article')
	v=v+v2+v3;
else
	v=v+v3;

		
		
		ss=ss+v;
	}
	document.getElementById("spreadSupply").innerHTML=ss;
}

function setValueBelow(name,num){
	for(var tmp=num+1; tmp<k; tmp++){
		document.getElementById(name+'Department'+tmp).value = document.getElementById(name+'Department'+num).value;
		document.getElementById(name+'Room'+tmp).value = document.getElementById(name+'Room'+num).value;
		document.getElementById(name+'Level'+tmp).value = document.getElementById(name+'Level'+num).value;
		document.getElementById(name+'PrefixName'+tmp).value = document.getElementById(name+'PrefixName'+num).value;
		document.getElementById(name+'Name'+tmp).value = document.getElementById(name+'Name'+num).value;
		document.getElementById(name+'Stock'+tmp).value = document.getElementById(name+'Stock'+num).value;
	}
}

function loadOrderArticle(data){
	var divTable = '';
	for(var i = 0; i<data["length"]; i++){
		divTable += '				<tr id='+i+'>'+
		'                    <th> <input type="checkbox"/> </th>'+
		'                    <th>'+ data['data'][i].id +'</th>'+
		'                    <th>'+ data['data'][i].fsn +'</th>'+
		'                    <th>'+ data['data'][i].description +'</th>'+
		'                    <th>'+ data['data'][i].quantity +'</th>'+
		'                    <th>'+ data['data'][i].classifier +'</th>'+
		'                    <th>'+ data['data'][i].price +'</th>'+
		'                    <th>'+ data['data'][i].lifeTime +'</th>'+
		'                    <th> <button class="btn btn-xs btn-info" ng-click="open()" > รายละเอียด</button></th>'+
		'                </tr>';
	}
	document.getElementById("durableArticleList").innerHTML = divTable;
}

function loadOrderGood(data){
	var divTable = '';
	for(var i = 0; i<data["length"]; i++){
		divTable += '				<tr id='+i+'>'+
		'                    <th> <input type="checkbox"/> </th>'+
		'                    <th>'+ data['data'][i].code +'</th>'+
		'                    <th>'+ data['data'][i].description +'</th>'+
		'                    <th>'+ data['data'][i].quantity +'</th>'+
		'                    <th>'+ data['data'][i].classifier +'</th>'+
		'                    <th>'+ data['data'][i].price +'</th>'+
		'                    <th> <button class="btn btn-xs btn-info" ng-click="open()" > รายละเอียด</button></th>'+
		'                </tr>';
	}
	document.getElementById("goodList").innerHTML = divTable;
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
