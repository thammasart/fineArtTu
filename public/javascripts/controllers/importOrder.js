var i =1;
var j =1;
var aiLists = [];
var eoLists = [];
$('document').ready(function(){
	createAICommittee();
	createEOCommittee();
}); 
function getCommitteeTemplate(name){
	var s;
	if(name == 'ai'){
		aiLists.push(i);
		s = '<div id="ai' + i +'">'+
		'				<input type="text" hidden="true" name="aiLists">'+
		'				<div class="form-group" >'+
		'					<div class="input-group" >'+
		'					    <span class="input-group-addon">คำนำหน้าชื่อ</span>'+
		'					    <input name="aiPrefixName'+ i +'"  type="text" class="form-control textAlignCenter  width100px"placeholder="ใส่ค่า">'+
		'					</div>'+
		'				</div>'+
		'				<div class="form-group" >'+
		'					<div class="input-group" >'+
		'					    <span class="input-group-addon">ชื่อ/สกุล</span>'+
		'					    <input name="aiName'+ i +'" type="text" class="form-control textAlignCenter  width225px"placeholder="ใส่ค่า">'+
		'					</div>'+
		'				</div>'+
		'				<div class="form-group" >'+
		'					<div class="input-group" >'+
		'					    <span class="input-group-addon" >ตำแหน่ง</span>'+
		'					    <input name="aiPosition'+ i +'" type="text" class="form-control textAlignCenter  width150px"placeholder="ตามข้อมูลuser">'+
		'					</div>'+
		'				</div>'+
		'				'+
		'				'+
		'				<div class="form-group" role="form">'+
		'				    <div class="input-group">'+
		'						<span class="input-group-addon">ประเภทกรรมการ</span>'+
		'						<select name="aiCommitteeType'+ i +'" class="form-control textAlignCenter  width150px">'+
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
		'						<select name="aiCommitteePosition'+ i +'" class="form-control textAlignCenter  width200px">'+
		'						  <option>---เลือก---</option>'+
		'						  <option>ประธารกรรมการ</option>'+
		'						  <option>กรรมการ</option>'+
		'						  <option>กรรมการและเรขานุการ</option>'+
		'						</select>'+
		'					</div>'+
		'				 </div>'+
		'				 <button type="button" onclick="removeDivCommittee(\'ai\','+ i +')">ลบ</button>'+
		'			 </div> <!-- id = ai'+ i +' -->'
		i++;
	}else if(name == 'eo'){
		eoLists.push(j);
		s = '<div id="eo' + j +'">'+
		'				<div class="form-group" >'+
		'					<div class="input-group" >'+
		'					    <span class="input-group-addon">คำนำหน้าชื่อ</span>'+
		'					    <input name="eoPrefixName'+ j +'"  type="text" class="form-control textAlignCenter  width100px"placeholder="ใส่ค่า">'+
		'					</div>'+
		'				</div>'+
		'				<div class="form-group" >'+
		'					<div class="input-group" >'+
		'					    <span class="input-group-addon">ชื่อ/สกุล</span>'+
		'					    <input name="eoName'+ j +'" type="text" class="form-control textAlignCenter  width225px"placeholder="ใส่ค่า">'+
		'					</div>'+
		'				</div>'+
		'				<div class="form-group" >'+
		'					<div class="input-group" >'+
		'					    <span class="input-group-addon" >ตำแหน่ง</span>'+
		'					    <input name="eoPosition'+ j +'" type="text" class="form-control textAlignCenter  width150px"placeholder="ตามข้อมูลuser">'+
		'					</div>'+
		'				</div>'+
		'				'+
		'				'+
		'				<div class="form-group" role="form">'+
		'				    <div class="input-group">'+
		'						<span class="input-group-addon">ประเภทกรรมการ</span>'+
		'						<select name="eoCommitteeType'+ j +'" class="form-control textAlignCenter  width150px">'+
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
		'						<select name="eoCommitteePosition'+ j +'" class="form-control textAlignCenter  width200px">'+
		'						  <option>---เลือก---</option>'+
		'						  <option>ประธารกรรมการ</option>'+
		'						  <option>กรรมการ</option>'+
		'						  <option>กรรมการและเรขานุการ</option>'+
		'						</select>'+
		'					</div>'+
		'				 </div>'+
		'				 <button type="button" onclick="removeDivCommittee(\'eo\','+ j +')">ลบ</button>'+
		'			 </div> <!-- id = eo'+ j +' -->'
		j++;
	}
	return s;
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
		document.getElementById("ai_committee");
		document.getElementById("ai"+num).remove();
		document.getElementById("aiLists").value = aiLists.join();
		console.log(document.getElementById("aiLists").value);
	}else if(name == 'eo'){
		eoLists.remove(num);
		document.getElementById("ai_committee");
		document.getElementById("eo"+num).remove();
		document.getElementById("eoLists").value = eoLists.join();
	}
}