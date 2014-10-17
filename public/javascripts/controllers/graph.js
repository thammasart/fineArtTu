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
		chartArea : {'left':'5%','width':'75%','height':'75%'},
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
	'relation' : 'month',
	'mode' : 'balance',
	'item' : 'durableArticle',
	'previousMode' : 'balance'
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
		chart3 = new google.visualization.LineChart(document.getElementById('graph-container'));
		google.visualization.events.addListener(chart1, 'select', selectionHandler);
		google.visualization.events.addListener(chart2, 'select', selectionHandler);
		google.visualization.events.addListener(chart3, 'select', selectionHandler);
	}

}

function selectionHandler(){

		/*console.log(chart3.getSelection()[0]['column'] + " "
				+ chart3.getSelection()[0]['row']);
		 */
		var object = chart2.getSelection()[0]||chart3.getSelection()[0]||chart1.getSelection()[0];
		var isChart1 = chart1.getSelection()[0]!=undefined;
		if(isChart1){
			chart1.setSelection(chart1.getSelection()[0]);
		}
		console.log(state);
		console.log(object);
		//data = getData(object);
		//drawChart();
		var isIn2 = false;
		var element = [['name','value',{role:'style'},{role:'annotation'}]];
		for ( var i = 0; i < materialNames.length; i++) {
			element.push([materialNames[i],Math.floor(Math.random() * 1000),color[i],materialNames[i]]);
		}
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
			/*data = new google.visualization.arrayToDataTable([
                  ['durable','สนง','ยานพหนะและขนส่ง','ไฟฟ้าและวิทยุ','คอมฯ','การศึกษา','งานบ้านงานครัว','ดนตรี','โฆษณาและเผยแพร่','ก่อสร้าง','ก่อสร้าง','ก่อสร้าง','ก่อสร้าง','ก่อสร้าง','ก่อสร้าง','ก่อสร้าง','ก่อสร้าง','ก่อสร้าง','ก่อสร้าง','ก่อสร้าง','ก่อสร้าง','ก่อสร้าง'],
                  [Math.floor(Math.random() * 1000),
                   Math.floor(Math.random() * 1000),
                   Math.floor(Math.random() * 1000),
                   Math.floor(Math.random() * 1000),
                   Math.floor(Math.random() * 1000),
                   Math.floor(Math.random() * 1000),
                   Math.floor(Math.random() * 1000),
                   Math.floor(Math.random() * 1000),
                   Math.floor(Math.random() * 1000),
                   Math.floor(Math.random() * 1000),
                   Math.floor(Math.random() * 1000),
                   Math.floor(Math.random() * 1000),
                   Math.floor(Math.random() * 1000),
                   Math.floor(Math.random() * 1000),
                   Math.floor(Math.random() * 1000),
                   Math.floor(Math.random() * 1000),
                   Math.floor(Math.random() * 1000),
                   Math.floor(Math.random() * 1000),
                   Math.floor(Math.random() * 1000),
                   Math.floor(Math.random() * 1000),
                   Math.floor(Math.random() * 1000),
		           Math.floor(Math.random() * 1000)]]);*/
			data = new google.visualization.arrayToDataTable(element);
		}else if(object['column'] == 2 && state['mode'] != 'detail'){
			console.log('in2');
			data = null;
			/*data = new google.visualization.arrayToDataTable([['name','numberOfItem'],
					['สนง',Math.floor(Math.random() * 1000)],
					['คอม',Math.floor(Math.random() * 1000)],
					['งานบ้าน',Math.floor(Math.random() * 1000)],
					['ไฟฟ้า',Math.floor(Math.random() * 1000)],
					['บริโภค',Math.floor(Math.random() * 1000)]]);*/
			/*data = new google.visualization.arrayToDataTable([
                    ['consumable','สนง','คอม','งานบ้าน','ไฟฟ้า','บริโภค'],
                    [Math.floor(Math.random() * 1000),
                     Math.floor(Math.random() * 1000),
                     Math.floor(Math.random() * 1000),
                     Math.floor(Math.random() * 1000),
                     Math.floor(Math.random() * 1000),
                     Math.floor(Math.random() * 1000)]]);*/
			data = new google.visualization.arrayToDataTable(element);
			isIn2 = true;
		}else{
			$('#graph-tab a[href="#tracking"]').tab('show');
			myRandom();
		}
		if(state['mode'] != 'detail'){
			//state['mode'] = state['previousMode'];
			state['previousMode'] = state['mode'];
			state['mode'] = 'detail';
		}else{
			/*state['previousMode'] = state['mode'];
			state['mode'] = 'detail';*/
			state['mode'] = state['previousMode'];
		}
		//chart2.draw(data,options);
		if(!isIn2&&!isChart1){
			chart2.draw(data, {
				title : 'เปรียบเทียบการใช้งบประมาณรายเดือน',
				chartArea : {'width':'80%','height':'80%'},
				animation : {
					duration : 1000,
					easing : 'out',
				},
				//legend: { position: "none" },
				hAxis: {
					//textPosition : 'none',
					textStyle :{fontSize: 10},
				},
			});
		}else if(!isChart1){
			chart1.draw(data,options);
		}
		isChart1=false;
}

function clearChart(){
	chart1.clearChart();
	chart2.clearChart();
	chart3.clearChart();
}

function getData(obj){
	$.ajax({
		url:'/graph',
	    type: 'post',
	    data: JSON.stringify(obj),
	    contentType: 'application/json',
	    dataType: 'json',
    	success: function(result){
    		alert(result);
    	}
	});
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
	myRandom();
	clearChart();
	chart3.draw(data, options);
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