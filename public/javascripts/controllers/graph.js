// Load the Visualization API library and the piechart library.
google.load('visualization', '1.0', {
	'packages' : [ 'corechart' ]
});
// ... draw the chart...
google.setOnLoadCallback(drawChart);

var isSetChart = false;
var data;
var chart2;
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
		chart1 = new google.visualization.PieChart(document.getElementById('graph-container'));
		chart2 = new google.visualization.ColumnChart(document.getElementById('graph-container'));
		google.visualization.events.addListener(chart2, 'select', function() {

			console.log(chart2.getSelection()[0]['column'] + " "
					+ chart2.getSelection()[0]['row']);

			var object = chart2.getSelection()[0];
			//data = getData(object);
			//drawChart();
			if(object['column'] == 1 && state['mode'] != 'detail'){
				console.log('in1');
				data = null;
				/*data = new google.visualization.arrayToDataTable([['name','numberOfItem'],
						['สนง',Math.floor(Math.random() * 1000)],
						['ยานพหนะและขนส่ง',Math.floor(Math.random() * 1000)],
						['ไฟฟ้าและวิทยุ',Math.floor(Math.random() * 1000)],
						['คอมฯ',Math.floor(Math.random() * 1000)],
						['การศึกษา',Math.floor(Math.random() * 1000)],
						['งานบ้านงานครัว',Math.floor(Math.random() * 1000)],
						['ดนตรี',Math.floor(Math.random() * 1000)],
						['โฆษณาและเผยแพร่',Math.floor(Math.random() * 1000)],
						['ก่อสร้าง',Math.floor(Math.random() * 1000)]]);*/
				data = new google.visualization.arrayToDataTable([
                        ['durable','สนง','ยานพหนะและขนส่ง','ไฟฟ้าและวิทยุ','คอมฯ','การศึกษา','งานบ้านงานครัว','ดนตรี','โฆษณาและเผยแพร่','ก่อสร้าง'],
                        [Math.floor(Math.random() * 1000),
                         Math.floor(Math.random() * 1000),
                         Math.floor(Math.random() * 1000),
                         Math.floor(Math.random() * 1000),
                         Math.floor(Math.random() * 1000),
                         Math.floor(Math.random() * 1000),
                         Math.floor(Math.random() * 1000),
                         Math.floor(Math.random() * 1000),
                         Math.floor(Math.random() * 1000),
                         Math.floor(Math.random() * 1000)]]);
			}else if(object['column'] == 2 && state['mode'] != 'detail'){
				console.log('in2');
				data = null;
				data = new google.visualization.arrayToDataTable([['name','numberOfItem'],
						['สนง',Math.floor(Math.random() * 1000)],
						['คอม',Math.floor(Math.random() * 1000)],
						['งานบ้าน',Math.floor(Math.random() * 1000)],
						['ไฟฟ้า',Math.floor(Math.random() * 1000)],
						['บริโภค',Math.floor(Math.random() * 1000)]]);
				/*data = new google.visualization.arrayToDataTable([
                        ['consumable','สนง','คอม','งานบ้าน','ไฟฟ้า','บริโภค'],
                        [Math.floor(Math.random() * 1000),
                         Math.floor(Math.random() * 1000),
                         Math.floor(Math.random() * 1000),
                         Math.floor(Math.random() * 1000),
                         Math.floor(Math.random() * 1000),
                         Math.floor(Math.random() * 1000)]]);*/
			}else{
				$('#graph-tab a[href="#tracking"]').tab('show');
			}
			state['mode'] = 'detail';
			chart2.draw(data, options);
		});
	}
	

}

function getData(obj){
	
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
	myRandom();
	chart2.draw(data, options);
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

var setOption = function(obj) {
	for ( var k in obj) {
		options[k] = obj[k];
	}
	/*
	 * for ( var k in options) { console.log(k + " " + options[k]); }
	 */
}