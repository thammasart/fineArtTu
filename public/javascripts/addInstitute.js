var submitStatus = true;

function submitButtonClick(){
    
    submitStatus = true;
    if(document.getElementById("nameEntrepreneur").value ==""){
        document.getElementById("nameEntrepreneurAlert").style.display = "table-row";
        submitStatus = false;
    }else  document.getElementById("nameEntrepreneurAlert").style.display= "none";

    if(document.getElementById("nameDealer").value ==""){
        document.getElementById("nameDealerAlert").style.display = "table-row";
        submitStatus = false;
    }else  document.getElementById("nameDealerAlert").style.display= "none";

    if(document.getElementById("payPeriod2").value =="0"){
        document.getElementById("payPeriod2Alert").style.display = "table-row";
        submitStatus = false;
    }else  document.getElementById("payPeriod2Alert").style.display= "none";

    if(document.getElementById("sendPeriod2").value =="0"){
        document.getElementById("sendPeriod2Alert").style.display = "table-row";
        submitStatus = false;
    }else  document.getElementById("sendPeriod2Alert").style.display= "none";


    return submitStatus;
}


 function optionElement(name) { 
    var ni = document.getElementsByName("niti");
    var pe = document.getElementsByName("personal");
    var i;
    var j;
    if(name == 1){ 
        for(i = 0; i < ni.length;i++){ 
            ni[i].style.display = "block";
        } 
        for(j = 0; j < ni.length;j++){ 
            pe[j].style.display = "none";
        } 
    }else if(name == 2){ 
        for(i = 0; i < ni.length;i++){ 
            ni[i].style.display = "none";
        } 
        for(j = 0; j < ni.length;j++){ 
            pe[j].style.display = "block";
        } 
    }
 } 
