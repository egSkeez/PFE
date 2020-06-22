jQuery.noConflict();
jQuery(document).ready(
		function() {
	jQuery('#container').highcharts({

		chart : {
			type : 'bar'

		},
		title : {
			text : 'Historiques visites'
		},
		subtitle : {
			text : 'Minist\351re de l\350ducation'
		},
		xAxis : {
			categories : [ '2010', '2011', '2012', '2013', '2014' ],
			title : {
				text : null
			}
		},
		yAxis : {
			min : 0,
			title : {
				text : 'Visites',
				align : 'high'
			},
			labels : {
				overflow : 'justify'
			}
		},
		tooltip : {
			valueSuffix : ' personnes'
		},
		plotOptions : {
			bar : {
				dataLabels : {
					enabled : true
				}
			}
		},
		legend : {
			layout : 'vertical',
			align : 'right',
			verticalAlign : 'top',
			x : -40,
			y : 100,
			floating : true,
			borderWidth : 1,
			backgroundColor : '#FFFFFF',
			shadow : true
		},
		credits : {
			enabled : false
		},
		series : [ {
			name : 'groupe',
			data : [ 10, 203, 6, 20, 2 ]
		}, {
			name : 'famille',
			data : [ 130, 30, 94, 40, 10 ]
		}, {
			name : 'particulier',
			data : [ 97, 91, 405, 73, 40 ]
		} ]
	});
});