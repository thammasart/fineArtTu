var submitStatus = true;

function submitButtonClick(){
    submitStatus = true;
    if(document.getElementById("description").value ==""){
        document.getElementById("descriptionAlert").style.display = "table-row";
        submitStatus = false;
    }else  document.getElementById("descriptionAlert").style.display= "none";

    if(document.getElementById("materialCode").value.length != 5 ){
        document.getElementById("materialCodeAlert").style.display = "table-row";
        submitStatus = false;
    }else  document.getElementById("materialCodeAlert").style.display= "none";

    return submitStatus;
}
