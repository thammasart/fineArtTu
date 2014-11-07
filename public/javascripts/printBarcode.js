var url = "http://localhost/TSC-PHP.php";

function printBarcode(code){
	if(code instanceof Array){
		$.get(url, { 'code[]' : code });
	}else{
		$.get(url, { 'code[]' : [code] });
	}
}