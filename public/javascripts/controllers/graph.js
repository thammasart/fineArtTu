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

var options = {
		title : 'เปรียบเทียบการใช้งบประมาณรายเดือน',
		chartArea : {'left':'8%','width':'75%','height':'75%'},
		animation : {
			duration : 1000,
			easing : 'out',
		},
		colors: color
};

var months = [ 'Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep',
		'Oct', 'Nov', 'Dec' ];
var quarters = [ 'ไตรมาส1', 'ไตรมาส2', 'ไตรมาส3', 'ไตรมาส4' ];
var years = [ '2010', '2011', '2012', '2013', '2014' ];
var state = {
	'relation' : 'year',
	'mode' : 'balance',
	'previousMode' : 'balance',
	'page' : 0,
	'clickedItem' : {'row':-1,'column':-1},
	'dataType' : 'default' 
}
var modeBtn = null;
var mode = {
	balance : 'เปรียบเทียบการใช้งบประมาณ',
	procurement : 'เปรียบเทียบจำนวนการนำเข้า',
	requisition : 'เปรียบเทียบจำนวนการเบิกจ่าย',
	repairing : 'เปรียบเทียบจำนวนการส่งซ่อม',
	remain : 'เปรียบเทียบจำนวนคงเหลือ',
	transfer : 'เปรียบเทียบจำนวนการโอนย้าย',
	remain : 'เปรียบเทียบจำนวนคงเหลือ'
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
		state['clickedItem'] = object;
		if(state['page'] == 0){
			getData('column');
		}else if(state['page']==1){
			//getData('table');
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
	    statusCode: function(response){
	    	$(document).html(response);
	    }
	});
}

function setData(obj,chart){
	console.log(obj);
	data = new google.visualization.arrayToDataTable(obj);
	clearChart();
	if(chart == "line"){
		chart3.draw(data, options);
		state['dataType'] = 'default';
		state['page'] = 0;
	}else if(chart == "column"){
		state['page'] = 1;
		chart2.draw(data, {
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
		});
	}else if(chart == "table"){
		
	}
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
$(document).on('shown.bs.tab', 'a[href="#relation"]', function (e) {
	drawChart();
	console.log('event fired');
})

function drawChart() {
	// Create the data table.
	load();
	//myRandom();
	getData('line');
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
	state['dataType'] = 'default';
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
	state['dataType'] = 'default';
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