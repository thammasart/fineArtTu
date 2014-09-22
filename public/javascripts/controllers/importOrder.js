var i =1;
var j =1;
var aiLists = [];
var eoLists = [];

var supplyList=[];
var k;

$('document').ready(function(){
	showPage('1');
	createAICommittee();
	createEOCommittee();
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
	var fields3 = $('#page3 :input[type="text"]');
	var fields4 = $('#page2 :input[type="number"]');
	var fields5 = $('#page3 :input[type="number"]');
	var fields6 = $('#page2 :input[type="radio"]');
	$.each(fields2, function(i, field) {
	    var dom = $(field);
	    dom.val("");
	});
	$.each(fields3, function(i, field) {
	    var dom = $(field);
	    dom.val("");
	});
	$.each(fields4, function(i, field) {
		var dom = $(field);
		dom.val(0);
	});
	$.each(fields5, function(i, field) {
		var dom = $(field);
		dom.val(0);
	});
	$.each(fields6, function(i, field) {
		var dom = $(field);
		dom.prop('checked', false);
	});
	
}
function getCommitteeTemplate(name){
	name == 'ai' ? aiLists.push(i):eoLists.push(j);
	var num = name == 'ai' ? i:j;
	console.log(name == 'ai' ? 'i':'j');
	var s = '<div id="'+name + num +'">'+
	'				<input type="text" hidden="true" name="aiLists">'+
	'				<div class="form-group" >'+
	'					<div class="input-group" >'+
	'					    <span class="input-group-addon">คำนำหน้าชื่อ</span>'+
	'					    <input name="'+name+'PrefixName'+ num +'"  type="text" class="form-control textAlignCenter  width100px"placeholder="ใส่ค่า">'+
	'					</div>'+
	'				</div>'+
	'				<div class="form-group" >'+
	'					<div class="input-group" >'+
	'					    <span class="input-group-addon">ชื่อ/สกุล</span>'+
	'					    <input name="'+name+'Name'+ num +'" type="text" class="form-control textAlignCenter  width225px"placeholder="ใส่ค่า">'+
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
	'						  <option>ประธารกรรมการ</option>'+
	'						  <option>กรรมการ</option>'+
	'						  <option>กรรมการและเรขานุการ</option>'+
	'						</select>'+
	'					</div>'+
	'				 </div>'+
	'				 <button type="button" onclick="removeDivCommittee(\''+name+'\','+ num +')">ลบ</button>'+
	'			 </div> <!-- id ='+ name + num +' -->'
	name == 'ai' ? i++ : j++;

	return s;
}

function preSpread(){
	var num = document.getElementById("number").value;
	var ss= document.getElementById("spreadSupply").innerHTML;
	for(var i=1;i<=num;i++)
	{
		supplyList.push(i);
		var v='  <div class="form-inline marginBtm1" role="form" align="left">'+
		''+
		'	        	<div class="form-group" >'+
		'	        		<div class="input-group"> '+
		'		                <span class="input-group-addon">สาขา</span>'+
		'		                    <select class="form-control textAlignCenter  width300px">'+
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
		'	                    <input type="text" class="form-control textAlignCenter  width75px">'+
		'	                </div>'+
		'	            </div>'+
		'	        	<div class="form-group" >'+
		'	                <div class="input-group" >'+
		'	                    <span class="input-group-addon" >ชั้น</span>'+
		'	                    <input type="text" class="form-control textAlignCenter  width50px">'+
		'	                </div>'+
		'	            </div>'+
		'	        	<div class="form-group" >'+
		'	                <div class="input-group" >'+
		'	                    <span class="input-group-addon" >รหัสFSN</span>'+
		'	                    <input type="text" class="form-control textAlignCenter  width225px"placeholder="ศก.พ.57-7400-100-0005(02/05)">'+
		'	                </div>'+
		'	            </div>'+
		'	            <div class="form-group" >'+
		'	                <div class="input-group" >'+
		'	                    <span class="input-group-addon" >คำนำหน้าชื่อ</span>'+
		'	                    <input type="text" class="form-control textAlignCenter  width100px"placeholder="ใส่ค่า">'+
		'	                </div>'+
		'	            </div>'+
		'	            <div class="form-group" >'+
		'	                <div class="input-group" >'+
		'	                    <span class="input-group-addon" >ชื่อ/สกุล</span>'+
		'	                    <input type="text" class="form-control textAlignCenter  width225px"placeholder="ใส่ค่า">'+
		'	                     <button>ตกลง</button>'+
		'	                </div>'+
		'	            </div>'+
		''+
		''+
		'	        </div>  '
		
		ss=ss+v;
	}
	document.getElementById("spreadSupply").innerHTML=ss;
	document.getElementById("supplyList").value = supplyList.join();
}

function createAICommittee() {
    var s= document.getElementById("ai_committee").innerHTML;
    s+=getCommitteeTemplate('ai');
    document.getElementById("ai_committee").innerHTML=s;
    document.getElementById("aiLists").value = aiLists.join();
}

function createEOCommittee(){
	var s= document.getElementById("eo_committee").innerHTML;
	s+=getCommitteeTemplate('eo');
    document.getElementById("eo_committee").innerHTML=s;
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
	if(num == '1'){
		clearPage();
	}
}
