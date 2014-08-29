// Load the Visualization API library and the piechart library.
google.load('visualization', '1.0', {
	'packages' : [ 'corechart' ]
});
// ... draw the chart...
google.setOnLoadCallback(drawChart);

var data;
var data1;
var data3;
var chart2;
var balanceData = [];
var options = {
	title : 'เปรียบเทียบการใช้งบประมาณรายเดือน',
	animation : {
		duration : 1000,
		easing : 'out',
	}
};

var months = [ 'Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep',
		'Oct', 'Nov', 'Dec' ];
var quarters = [ 'ไตรมาส1', 'ไตรมาส2', 'ไตรมาส3', 'ไตรมาส4' ];
var years = [ '2010', '2011', '2012', '2013', '2014' ];
var state = {
	'relation' : 'month',
	'mode' : 'balance',
	'item' : 'durableArticle'
};
var mode = {
	balance : 'เปรียบเทียบการใช้งบประมาณ',
	procurement : 'เปรียบเทียบจำนวนการนำเข้า',
	requisition : 'เปรียบเทียบจำนวนการเบิกจ่าย',
	repairing : 'เปรียบเทียบจำนวนการส่งซ่อม',
	remain : 'เปรียบเทียบจำนวนคงเหลือ',
	transfer : 'เปรียบเทียบจำนวนการโอนย้าย',
	remain : 'เปรียบเทียบจำนวนคงเหลือ'
};
var relation = {
	year : 'รายปี',
	quarter : 'รายไตรมาส',
	month : 'รายเดือน'
};

function load() {
	// Instantiate and draw our chart, passing in some options.
	chart1 = new google.visualization.PieChart(document
			.getElementById('graph-container'));
	chart2 = new google.visualization.ColumnChart(document
			.getElementById('graph-container'));
	google.visualization.events.addListener(chart2, 'select', function() {

		console.log(chart2.getSelection()[0]['column'] + " "
				+ chart2.getSelection()[0]['row']);

		var object = chart2.getSelection();
		if (state['relation'] == 'year') {

		} else if (state['relation'] == 'quarter') {

		} else if (state['relation'] == 'month') {

		} else if (state['relation'] == 'nYear') {

		}

	});

}

function myRandom(){
	data = null;
	var num1;
	var num2;
	data = new google.visualization.DataTable();
	data.addColumn('string', 'relation');
	data.addColumn('number', 'ครุภัณฑ์');
	data.addColumn('number', 'วัสดุ');
	if(state['relation'] == 'month'){
		for ( var i = 0; i < months.length; i++) {
			num1 = Math.floor(Math.random() * 1000);
			num2 = Math.floor(Math.random() * 1000);
			data.addRow([ months[i], num1, num2 ]);
		}
	}else if(state['relation'] == 'quarter') {
		for ( var i = 0; i < quarters.length; i++) {
			num1 = Math.floor(Math.random() * 1000);
			num2 = Math.floor(Math.random() * 1000);
			data.addRow([ quarters[i], num1, num2 ]);
		}
	}else if(state['relation'] == 'year'){
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
	myRandom();
	chart2.draw(data, options);
}

var setRelation = function(r) {
	state['relation'] = r;
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

var setMode = function(m) {
	switch (m) {
	case 'balance':
	case 'procurement':
	case 'requisition':
	case 'repairing':
	case 'transfer':
	case 'remain':
		break;
	}
	setOption({
		"title" : mode[m] + relation[state['relation']]
	});
	drawChart();
}

var setOption = function(o) {
	for ( var k in o) {
		options[k] = o[k];
	}
	/*
	 * for ( var k in options) { console.log(k + " " + options[k]); }
	 */
}