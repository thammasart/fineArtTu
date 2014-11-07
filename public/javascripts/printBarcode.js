var url = "localhost/TSC-PHP.php";

function printBarcode(code){
	if(code instanceof Array){
		$.get(url, { 'colors[]' : code });
	}else{
		$.get(url, { 'colors[]' : [code] });
	}
}