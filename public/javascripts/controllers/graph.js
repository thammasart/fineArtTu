// Load the Visualization API library and the piechart library.
google.load('visualization', '1.0', {
	'packages' : [ 'corechart' ]
});
// ... draw the chart...
google.setOnLoadCallback(drawChart);

var isSetChart = false;
var data;
var color = ['#7B9CDE','#EA8871','#FFC266','#70C074','#C266C2',
'#898BCD','#66C2DD','#EB8FAD','#A3CC66','#D48282',
'#83A1BF','#C28FC2','#7ACCC2','#CCCC70','#A385E0',
'#F0AB66','#B96A6A','#84BEA1','#99ACCA','#898BCD'];
var materialNames = ['สนง','ยานพหนะและขนส่ง','ไฟฟ้าและวิทยุ','คอมฯ','การศึกษา','งานบ้านงานครัว','ดนตรี','โฆษณาและเผยแพร่','ก่อสร้าง','ก่อสร้าง','ก่อสร้าง','ก่อสร้าง','ก่อสร้าง','ก่อสร้าง','ก่อสร้าง','ก่อสร้าง','ก่อสร้าง','ก่อสร้าง','ก่อสร้าง','ก่อสร้าง','ก่อสร้าง']
var defaultThead = '<tr><th>ลำดับที่<span class="glyphicon glyphicon-sort"></span></th><th>รายการ<span class="glyphicon glyphicon-sort"></span></th><th>จำนวน<span class="glyphicon glyphicon-sort"></span></th><th>รายละเอียด</span></th></tr>'
var balanceThead = '<tr><th>ลำดับที่<span class="glyphicon glyphicon-sort"></span></th><th>รายการ<span class="glyphicon glyphicon-sort"></span></th><th>งบประมาณที่ใช้<span class="glyphicon glyphicon-sort"></span></th><th>รายละเอียด</span></th></tr>';
var procurementThead = '<tr><th>ลำดับที่<span class="glyphicon glyphicon-sort"></span></th><th>รายการ<span class="glyphicon glyphicon-sort"></span></th><th>จำนวนที่นำเข้า<span class="glyphicon glyphicon-sort"></span></th><th>รายละเอียด</span></th></tr>';
var requisitionThead = '<tr><th>ลำดับที่<span class="glyphicon glyphicon-sort"></span></th><th>รายการ<span class="glyphicon glyphicon-sort"></span></th><th>จำนวนที่เบิกวัสดุ<span class="glyphicon glyphicon-sort"></span></th><th>รายละเอียด</span></th></tr>';
var transferThead = '<tr><th>ลำดับที่<span class="glyphicon glyphicon-sort"></span></th><th>รายการ<span class="glyphicon glyphicon-sort"></span></th><th>จำนวนที่โอนย้าย<span class="glyphicon glyphicon-sort"></span></th><th>รายละเอียด</span></th></tr>';
var repairingThead = '<tr><th>ลำดับที่<span class="glyphicon glyphicon-sort"></span></th><th>รายการ<span class="glyphicon glyphicon-sort"></span></th><th>จำนวนที่ส่งซ่อม<span class="glyphicon glyphicon-sort"></span></th><th>รายละเอียด</span></th></tr>';
var repairedThead = '<tr><th>ลำดับที่<span class="glyphicon glyphicon-sort"></span></th><th>รายการ<span class="glyphicon glyphicon-sort"></span></th><th>จำนวนที่ส่งซ่อม<span class="glyphicon glyphicon-sort"></span></th><th>ราคา<span class="glyphicon glyphicon-sort"></span></th><th>รายละเอียด</span></th></tr>';
var borrowThead = '<tr><th>ลำดับที่<span class="glyphicon glyphicon-sort"></span></th><th>รายการ<span class="glyphicon glyphicon-sort"></span></th><th>จำนวนที่ถูกยืม<span class="glyphicon glyphicon-sort"></span></th><th>รายละเอียด</span></th></tr>';
var remainThead = '<tr><th>ลำดับที่<span class="glyphicon glyphicon-sort"></span></th><th>รายการ<span class="glyphicon glyphicon-sort"></span></th><th>จำนวนที่คงเหลือ<span class="glyphicon glyphicon-sort"></span></th><th>รายละเอียด</span></th></tr>';
var options = {
		title : 'เปรียบเทียบการใช้งบประมาณรายเดือน',
		chartArea : {'left':'8%','width':'75%','height':'75%'},
		animation : {
			duration : 1000,
			easing : 'out',
		},
		colors: color,
};

var months = [ 'Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep',
		'Oct', 'Nov', 'Dec' ];
var quarters = [ 'ไตรมาส1', 'ไตรมาส2', 'ไตรมาส3', 'ไตรมาส4' ];
var years = [ '2010', '2011', '2012', '2013', '2014' ];
var state = {
	'relation' : 'year',
	'mode' : 'balance',
	'request' : 'default',
	'page' : 0,
	'clickedItem' : {'row':-1,'column':-1},
	'selectedName' : 'default',
	'lastSelected' : {'row':-1,'column':-1},
}
var modeBtn = null;
var mode = {
	balance : 'เปรียบเทียบการใช้งบประมาณ',
	procurement : 'เปรียบเทียบจำนวนการนำเข้า',
	requisition : 'เปรียบเทียบจำนวนการเบิกจ่าย',
	repairing : 'เปรียบเทียบจำนวนการส่งซ่อม',
	remain : 'เปรียบเทียบจำนวนคงเหลือ',
	transfer : 'เปรียบเทียบจำนวนการโอนย้าย',
	remain : 'เปรียบเทียบจำนวนคงเหลือ',
	borrow : 'เปรียบเทียบจำนวนการยืม'
};
var relationBtn = null;
var relation = {
	year : 'รายปี',
	quarter : 'รายไตรมาส',
	month : 'รายเดือน'
};

function load() {
	// Instantiate and draw our chart, passing in some options.
	if(!isSetChart){
		isSetChart = true;
		//chart1 = new google.visualization.PieChart(document.getElementById('graph-container'));
		chart2 = new google.visualization.ColumnChart(document.getElementById('graph-container'));
		chart3 = new google.visualization.LineChart(document.getElementById('graph-container'));
		//google.visualization.events.addListener(chart1, 'select', selectionHandler);
		google.visualization.events.addListener(chart2, 'select', selectionHandler);
		google.visualization.events.addListener(chart3, 'select', selectionHandler);
	}
}

function selectionHandler(){
	var object = chart2.getSelection()[0]||chart3.getSelection()[0];//||chart1.getSelection()[0];
	if(chart2.getSelection()[0] == undefined){
		chart3.setSelection(chart3.getSelection()[0]);
	}else{
		chart2.setSelection(chart2.getSelection()[0]);
	}
	if(object.row != null){
		state['lastSelected'] = state['clickedItem'];
		state['clickedItem'] = object;
		if(state['page'] == 0){
			state['page'] = 1;
			getData('column');
		}else if(state['page']==1){
			var col = 3;
			if(state['mode'] == 'transfer' || state['mode'] == 'balance' || state['mode'] == 'procurement'){
				col = 0;
			}
			state['page'] = 2;
			state['selectedName'] = data.getFormattedValue(object.row, col);
			getData('table');
		}
	}
}

function clearChart(){
//	chart1.clearChart();
	chart2.clearChart();
	chart3.clearChart();
}

function getData(chart){
	$.ajax({
		url:'/graph',
	    type: 'post',
	    data: JSON.stringify(state),
	    contentType: 'application/json',
	    dataType: 'json',
	    success: function(result) {
    		setData(result , chart);
	    },
	    statusCode:{
	    	500: function(response){
	    		//console.log(response.responseText);
	    		var mywindow = window.open('', 'my div', 'height=400,width=600');
	            /*optional stylesheet*/ //mywindow.document.write('<link rel="stylesheet" href="main.css" type="text/css" />');
	            mywindow.document.write(response.responseText);
	 	    }
	    }
	});
}

function setData(obj,chart){
	data = new google.visualization.arrayToDataTable(obj);
	clearChart();
	if(chart == "line"){
		chart3.draw(data, options);
		state['dataType'] = 'default';
		state['page'] = 0;
	}else if(chart == "column"){
		var newOption = {
				chartArea : {'width':'80%','height':'80%'},
				animation : {
					duration : 1000,
					easing : 'out',
				},
				legend: { position: "none" },
				hAxis: {
					//textPosition : 'none',
					textStyle :{fontSize: 10},
				},
				annotations : {alwaysOutside : true},
				colors: color,
				isStacked: true,
			};
		if(state['mode'] == 'balance'){
			newOption.legend = { position: "top" };
		}
		chart2.draw(data, newOption);
	}else if(chart == "table"){
		clearTable(0);
		if(state['request'] == 'search'){
			setDataTableColumn("trackingTable", defaultThead).rows.add(obj).draw();
		}else if(state['mode'] == 'balance'){
			setDataTableColumn("trackingTable", balanceThead).rows.add(obj).draw();
		}else if(state['mode'] == 'procurement'){
			setDataTableColumn("trackingTable", procurementThead).rows.add(obj).draw();
		}else if(state['mode'] == 'requisition'){
			setDataTableColumn("trackingTable", requisitionThead).rows.add(obj).draw();
		}else if(state['mode'] == 'transfer'){
			setDataTableColumn("trackingTable", transferThead).rows.add(obj).draw();
		}else if(state['mode'] == 'repairing'){
			setDataTableColumn("trackingTable", repairingThead).rows.add(obj).draw();
			/*if(state['clickedItem'].column == 1){
				setDataTableColumn("trackingTable", repairedThead).rows.add(obj).draw();
			}else{
				setDataTableColumn("trackingTable", repairingThead).rows.add(obj).draw();
			}*/
		}else if(state['mode'] == 'borrow'){
			setDataTableColumn("trackingTable", borrowThead).rows.add(obj).draw();
		}else if(state['mode'] == 'remain'){
			setDataTableColumn("trackingTable", remainThead).rows.add(obj).draw();
		}
		setSearchBox("search",0);
		
		$('#graph-tab a[href="#tracking"]').tab('show');
	}
	//document.getElementById('printDiv').innerHTML = document.getElementById('graph-container').innerHTML;
}

function myRandom() {
	data = null;
	var num1;
	var num2;
	data = new google.visualization.DataTable();
	data.addColumn('string', 'relation');
	data.addColumn('number', 'ครุภัณฑ์');
	data.addColumn('number', 'วัสดุ');
	if (state['relation'] == 'month') {
		for ( var i = 0; i < months.length; i++) {
			num1 = Math.floor(Math.random() * 1000);
			num2 = Math.floor(Math.random() * 1000);
			data.addRow([ months[i], num1, num2 ]);
		}
	} else if (state['relation'] == 'quarter') {
		for ( var i = 0; i < quarters.length; i++) {
			num1 = Math.floor(Math.random() * 1000);
			num2 = Math.floor(Math.random() * 1000);
			data.addRow([ quarters[i], num1, num2 ]);
		}
	} else if (state['relation'] == 'year') {
		for ( var i = 0; i < years.length; i++) {
			num1 = Math.floor(Math.random() * 1000);
			num2 = Math.floor(Math.random() * 1000);
			data.addRow([ years[i], num1, num2 ]);
		}
	}
}

function drawChart() {
	// Create the data table.
	load();
	//myRandom();
	if(state['mode'] == 'requisition' || state['mode'] == 'remain'){
		getData('column');
	}else{
		getData('line');
	}
}

var setRelation = function(r, element) {
	$(element).addClass('active');
    if(relationBtn == null){
    	var x = $( "button:contains('รายปี')" )[0];
    	$(x).removeClass('active');
    	relationBtn = element;
    }else{
    	$(relationBtn).removeClass('active');
    	relationBtn = element;
    }
	state['relation'] = r;
	state['clickedItem'] = {'row':-1 , 'column':-1};
	state['lastSelected'] = state['clickedItem'];
	state['page'] = 0;
	switch (r) {
	case 'month': 
	case 'quarter':
	case 'year':
	}
	setOption({
		"title" : mode[state['mode']] + relation[r]
	});
	drawChart();
}

var setMode = function(m, element) {
	$(element).addClass('active');
    if(modeBtn == null){
    	var x = $( "button:contains('การใช้งบประมาณ')" )[0];
    	$(x).removeClass('active');
    	modeBtn = element;
    }else{
    	$(modeBtn).removeClass('active');
    	modeBtn = element;
    }
	state['mode'] = m;
	state['clickedItem'] = {'row' : -1,'column' : -1};
	state['lastSelected'] = state['clickedItem'];
	state['page'] = 0;
	setOption({
		"title" : mode[m] + relation[state['relation']]
	});
	drawChart();
}

var setOption = function(obj) {
	for ( var k in obj) {
	}
		options[k] = obj[k];
}

function getDescription(className, ids, path){
	if(path == '/graphDescription'){
		obj = {"className" : className, "ids" : ids};
	}else{
		obj = {"className" : className, "query" : ids};
	}
	$.ajax({
		url: path,
	    type: 'post',
	    data: JSON.stringify(obj),
	    contentType: 'application/json',
	    dataType: 'json',
	    statusCode:{
	    	200: function(response){
	    		document.getElementById('description').innerHTML = response.responseText;
	    		$('#tablePane').hide();
	    		$('#description').show();
	    		
	    		$('.myBtn').on('click', function(e) {

    			    $header = $(this);
    			    //getting the next element
    			    $content = $header.next();
    			    if($content.is(":hidden")){
    			    	// change to expand arrow
    			    	$header.find("span").remove();
    			    	$header.append($('<span class="glyphicon glyphicon-chevron-down"></span>'));
    			    	
    			    }else{
    			    	// change to collapse arrow
    			    	//console.log($header.text());
    			    	$header.find("span").remove();
    			    	$header.append($('<span class="glyphicon glyphicon-chevron-right"></span>'));
    			    }
    			    //open up the content needed - toggle the slide- if visible, slide up, if not slidedown.
    			    $content.slideToggle(500);

	    		});
	    		
	    		//console.log(response.responseText);
	    		/*var mywindow = window.open('', 'my div', 'height=400,width=600');
	            optional stylesheet //mywindow.document.write('<link rel="stylesheet" href="main.css" type="text/css" />');
	            mywindow.document.write(response.responseText);*/
	 	    },
	    	500: function(response){
	    		//console.log(response.responseText);
	    		var mywindow = window.open('', 'my div', 'height=400,width=600');
	            /*optional stylesheet*/ //mywindow.document.write('<link rel="stylesheet" href="main.css" type="text/css" />');
	            mywindow.document.write(response.responseText);
	 	    }
	    }
	});
	state['page'] = 3;
}

function backToTable(){
	$('#description').hide();
	$('#description').html("");
	$('#tablePane').show();
	state['page'] == 2;
}

$(document).on('shown.bs.tab', 'a[href="#relation"]', function (e) {
	backToTable();
	state['request'] = "default";
	state['clickedItem'] = {'row':-1 , 'column':-1};
	state['lastSelected'] = state['clickedItem'];
	state['page'] = 0;
	state['selectedName'] = 'default';
	drawChart();
});

$(document).on('shown.bs.tab', 'a[href="#tracking"]', function (e) {
	if(state['page']!=2){
		state['request'] = "search";
		clearTable(0);
		setDataTableColumn("trackingTable",defaultThead);
	}
});

function search(event){
    if(event.keyCode == 13){
    	state['request'] = "search";
		state['query'] = $('#search').val();
		backToTable();
		getData('table');
		//$('#search').val("");
    }
};

function printPage(){
	destroyTable();
	if(state['request'] == "search"){
		document.getElementById('printDiv').innerHTML = document.getElementById('tablePane').innerHTML;
    	var $header = $('#printDiv');
    	$header.find("span").remove();
	}else if(state['page'] == 0 || state['page'] == 1){
    	document.getElementById('printDiv').innerHTML = document.getElementById('graph-container').innerHTML;
    }else if(state['page'] == 2){
    	document.getElementById('printDiv').innerHTML = document.getElementById('tablePane').innerHTML;
    	var $header = $('#printDiv');
    	$header.find("span").remove();
    }else if(state['page'] == 3){
    	document.getElementById('printDiv').innerHTML = document.getElementById('description').innerHTML;
    	var $header = $('#printDiv');
    	$header.find("button").remove();
    	$header.find("span").remove();
    	$header.find(".collapse").css('display','block');
    }
    $('#printDiv').show();
    window.print();
    $('#printDiv').text("");
	updateTable();
	$('#printDiv').hide();
}

$( window ).resize(function() {
	drawChart();
});


/*
'<tr>
<th>ลำดับที่<span class="glyphicon glyphicon-sort"></span></th>
<th>ลำดับที่<span class="glyphicon glyphicon-sort"></span></th>
<th>ลำดับที่<span class="glyphicon glyphicon-sort"></span></th>
<th>ลำดับที่<span class="glyphicon glyphicon-sort"></span></th>
</tr>'
*/
