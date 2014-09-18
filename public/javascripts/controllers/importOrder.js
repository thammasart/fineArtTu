var template1;
$('document').ready(function(){
	template1= document.getElementById("ai_committee").innerHTML;
})

function createCommittee() {
    var s= document.getElementById("ai_committee").innerHTML;
    s+=template1;
    document.getElementById("ai_committee").innerHTML=s;
}