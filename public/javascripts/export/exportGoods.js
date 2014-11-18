
var deleteList = [];

function addDeleteList(code){
    if(deleteList.indexOf(code) > -1){
        deleteList.remove(code);
        document.getElementById("row" + code).style.color = "";
        document.getElementById("checkbox" + code).checked = false;
    }
    else{
        deleteList.push(code);
        document.getElementById("row" + code).style.color = "#cc3300";
        document.getElementById("checkbox" + code).checked = true;
    }
}

function deleteOrderGoods(){
    var dataDetail = {};
    dataDetail.detail = deleteList;
    $.ajax({
        url:'/export/orderGoods/deletes',
        type: 'post',
        data: JSON.stringify(dataDetail),
        contentType: 'application/json',
        dataType: 'json',
        success: function(result){
            var status = result["status"];
            if(status == "SUCCESS"){
                var deleteList = [];
                location.reload();
            }
            else{
                alert('save detail error : ' + data["message"]);
            }
        }
    });
}