var i =1;
var j =1;
var aiLists = [];
var eoLists = [];
$('document').ready(function(){
	createAICommittee();
	createEOCommittee();
}); 
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