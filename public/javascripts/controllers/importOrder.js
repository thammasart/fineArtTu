var i =0;
var j =0;
function createAICommittee() {
    var s= document.getElementById("ai_committee").innerHTML;
    s+='              <div class="form-group" >'+
	'                    <div class="input-group" >'+
	'                        <span class="input-group-addon">คำนำหน้าชื่อ</span>'+
	'                        <input  type="text" class="form-control textAlignCenter  width100px"placeholder="ใส่ค่า">'+
	'                    </div>'+
	'              </div>'+
	'              <div class="form-group" >'+
	'                    <div class="input-group" >'+
	'                        <span class="input-group-addon">ชื่อ/สกุล</span>'+
	'                        <input type="text" class="form-control textAlignCenter  width225px"placeholder="ใส่ค่า">'+
	'                    </div>'+
	'              </div>'+
	'              <div class="form-group" >'+
	'                    <div class="input-group" >'+
	'                        <span class="input-group-addon" >ตำแหน่ง</span>'+
	'                        <input type="text" class="form-control textAlignCenter  width150px"placeholder="ตามข้อมูลuser">'+
	'                    </div>'+
	'              </div>'+
	''+
	''+
	'        <div class="form-group" role="form">'+
	'            <div class="input-group">'+
	'              <span class="input-group-addon">ประเภทกรรมการ</span>'+
	'                <select class="form-control textAlignCenter  width150px">'+
	'                  <option>---เลือก---</option>'+
	'                  <option>ข้าราชการ</option>'+
	'                  <option>พนักงานมหาลัย</option>'+
	'                </select>'+
	'            </div>'+
	'        </div>'+
	''+
	'         <div class="form-group" role="form">'+
	'            <div class="input-group">'+
	'              <span class="input-group-addon">ตำแหน่งในคณะกรรมการ</span>'+
	'                <select class="form-control textAlignCenter  width200px">'+
	'                  <option>---เลือก---</option>'+
	'                  <option>ประธารกรรมการ</option>'+
	'                  <option>กรรมการ</option>'+
	'                  <option>กรรมการและเรขานุการ</option>'+
	'                </select>'+
	'            </div>'+
	'          </div>'+
	'          <button>ลบ</button>';
    i++;
    document.getElementById("ai_committee").innerHTML=s;
}

function createEOCommittee(){
	var s= document.getElementById("eo_committee").innerHTML;
	s+='              <div class="form-group" >'+
	'                    <div class="input-group" >'+
	'                        <span class="input-group-addon">คำนำหน้าชื่อ</span>'+
	'                        <input  type="text" class="form-control textAlignCenter  width100px"placeholder="ใส่ค่า">'+
	'                    </div>'+
	'              </div>'+
	'              <div class="form-group" >'+
	'                    <div class="input-group" >'+
	'                        <span class="input-group-addon">ชื่อ/สกุล</span>'+
	'                        <input type="text" class="form-control textAlignCenter  width225px"placeholder="ใส่ค่า">'+
	'                    </div>'+
	'              </div>'+
	'              <div class="form-group" >'+
	'                    <div class="input-group" >'+
	'                        <span class="input-group-addon" >ตำแหน่ง</span>'+
	'                        <input type="text" class="form-control textAlignCenter  width150px"placeholder="ตามข้อมูลuser">'+
	'                    </div>'+
	'              </div>'+
	''+
	''+
	'        <div class="form-group" role="form">'+
	'            <div class="input-group">'+
	'              <span class="input-group-addon">ประเภทกรรมการ</span>'+
	'                <select class="form-control textAlignCenter  width150px">'+
	'                  <option>---เลือก---</option>'+
	'                  <option>ข้าราชการ</option>'+
	'                  <option>พนักงานมหาลัย</option>'+
	'                </select>'+
	'            </div>'+
	'        </div>'+
	''+
	'         <div class="form-group" role="form">'+
	'            <div class="input-group">'+
	'              <span class="input-group-addon">ตำแหน่งในคณะกรรมการ</span>'+
	'                <select class="form-control textAlignCenter  width200px">'+
	'                  <option>---เลือก---</option>'+
	'                  <option>ประธารกรรมการ</option>'+
	'                  <option>กรรมการ</option>'+
	'                  <option>กรรมการและเรขานุการ</option>'+
	'                </select>'+
	'            </div>'+
	'          </div>'+
	'          <button>ลบ</button>';
    j++;
    document.getElementById("eo_committee").innerHTML=s;
	
}