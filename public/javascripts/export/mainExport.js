
function searchFSN(){
	var fsnCode = document.getElementById("fsnCode").value;
	var des = document.getElementById("fsnDescription").value;
	$.ajax({
		type: "GET",
		url: "/export/searchFSN",
		data: {'code': fsnCode, 'description' : des},
		success: function(data){
		   	//alert(JSON.stringify(data));
		    if(data["status"] == "SUCCESS"){
			   	var allArticles = data["result"];
			   	var s = "";
			   	destroyTable();
				for (var i = 0; i < allArticles.length; i++) {
					if((oldDetail.indexOf(allArticles[i].id) < 0)){
						s += '<tr id="fsn' + allArticles[i].id + '" >';
						s += '	<th onclick="addNewDetai(' + allArticles[i].id + ')"> ';
						if(newDetail.indexOf(allArticles[i].id) > -1){
							s += '<input type="checkbox" id="addDetail' + allArticles[i].id + '" checked';
						}
						else{
							s += '<input type="checkbox" id="addDetail' + allArticles[i].id + '"';
						}
						s += '> </th>';
						s += ' <th onclick="addNewDetai(' + allArticles[i].id + ')">'+ allArticles[i].code +'</th>';
						if(allArticles[i].detail){
							s += '	<th>'+ allArticles[i].detail.fsn.descriptionDescription +'</th>';
							s += '	<th>'+ allArticles[i].detail.price + ' / ' + allArticles[i].remainingPriceToString +'</th>';
							s += '	<th>'+ allArticles[i].detail.llifeTime + ' ปี / ' + allArticles[i].remainLifetimeToString +'</th>';
							s += '	<th>'+ allArticles[i].department + '</th>';
						}
						else{
							s += '	<th>'+ 'ไม่มี' +'</th>';
							s += '	<th>'+ 'ไม่มี' +'</th>';
							s += '	<th>'+ 'ไม่มี' +'</th>';
							s += '	<th>'+ allArticles[i].id + ' : ' + 'ไม่มี' +'</th>';
						}
						s += '</tr>';
					}
			   	}
			   	document.getElementById("searchResultTable").innerHTML = s;
			   	updateTable();
		    }
		    else{
		    	alert("find FSN error : " + data["message"]);
		    }
		}
	});

}