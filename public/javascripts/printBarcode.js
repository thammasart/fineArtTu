var url = "http://localhost:8080/TSC-PHP.php";

function printBarcode(code){
	if(code instanceof Array){
		$.get(url, { 'code[]' : code });
	}else{
		$.get(url, { 'code[]' : [code] });
	}
}
