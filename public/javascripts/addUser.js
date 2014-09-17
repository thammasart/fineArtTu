var submitStatus = true;
var re = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;

function submitButtonClick(){
    
    submitStatus = true;
    if(document.getElementById("username").value ==""){
        document.getElementById("usernameAlert").style.display = "table-row";
        submitStatus = false;
    }else  document.getElementById("usernameAlert").style.display= "none";

    if(document.getElementById("password").value ==""){
        document.getElementById("passwordAlert").style.display = "table-row";
        submitStatus = false;
    }else  document.getElementById("passwordAlert").style.display= "none";

    if(document.getElementById("rePassword").value ==""){
        document.getElementById("rePasswordAlert").style.display = "table-row";
        submitStatus = false;
    }else  document.getElementById("rePasswordAlert").style.display= "none";

    if(document.getElementById("departure").value ==""){
        document.getElementById("departureAlert").style.display = "table-row";
        submitStatus = false;
    }else  document.getElementById("departureAlert").style.display= "none";

    if(document.getElementById("position").value ==""){
        document.getElementById("positionAlert").style.display = "table-row";
        submitStatus = false;
    }else  document.getElementById("positionAlert").style.display= "none";

    if(document.getElementById("firstname").value ==""){
        document.getElementById("firstnameAlert").style.display = "table-row";
        submitStatus = false;
    }else  document.getElementById("firstnameAlert").style.display= "none";

    if(document.getElementById("lastname").value ==""){
        document.getElementById("lastnameAlert").style.display = "table-row";
        submitStatus = false;
    }else  document.getElementById("lastnameAlert").style.display= "none";

//    if($("#status option:selected").text() == "---เลือก---"){
//        document.getElementById("statusAlert").style.display = "table-row";
//        submitStatus = false;
//    alert("hello");
//    }else document.getElementById("statusAlert").style.display = "none";

    return submitStatus;
}
