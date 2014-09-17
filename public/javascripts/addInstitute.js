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
