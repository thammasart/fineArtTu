google.load('visualization', '1.0', {
	'packages' : [ 'corechart' ]
});

// Set a callback to run when the Google Visualization API is loaded.
google.setOnLoadCallback(drawChart);

function drawChart() {
	// Create the data table.
	var data1 = new google.visualization.DataTable();
	data1.addColumn('string', 'ประเภท');
	data1.addColumn('number', 'จำนวนงบประมาณ');
	data1.addRows([ [ 'ครุภัณฑ์', 300000 ], [ 'วัสดุ', 10000 ] ]);

	var data2 = new google.visualization.DataTable();
	data2.addColumn('string', 'ประเภท');
	data2.addColumn('number', 'จำนวนงบประมาณ');
	data2.addRows([ [ 'ครุภัณฑ์', 3000 ], [ 'วัสดุ', 1000 ] ]);

	var data3 = new google.visualization.DataTable();
	data3.addColumn('string', 'ประเภท');
	data3.addColumn('number', 'จำนวนงบประมาณ');
	data3
			.addRows([ [ 'ครุภัณฑ์', 3000 ], [ 'วัสดุ', 1000 ],
					[ 'อื่นๆ', 100 ] ]);

	// Set chart options
	var options = {
		'title' : 'เปรียบเทียบการใช้งบประมาณรายปี',
	};

	// Instantiate and draw our chart, passing in some options.
	var chart1 = new google.visualization.PieChart(document
			.getElementById('graph-container'));
	var chart2 = new google.visualization.ColumnChart(document
			.getElementById('graph-container'));
	var chart3 = new google.visualization.ColumnChart(document
			.getElementById('graph-container'));
	google.visualization.events.addListener(chart1, 'select', function() {
		var object = chart1.getSelection();
		if (chart1.getSelection()[0]['row'] == 0) {
			chart2.draw(data2, options);
		} else {
			chart3.draw(data3, options);
		}
	});

	chart1.draw(data1, options);
}
