//<![CDATA[
jQuery.noConflict();
jQuery(function () {
	 Highcharts.setOptions({
	     colors: ['#677E52', '#375D81', '#93A299', '#8C5935', '#05966D', '#419F2E', '#FF321D', '#E2AD3B', '#FF73BF', '#4EA9A0', '#C44C51', '#BD33A4', '#00FE7E', '#64E572', '#895959', '#FFF263', '#6AF9C4', '#D400FF', '#FF0000', '#006699','#677E52','#570906','#FFF168','#8FCF3C','#FC7F3C','#1D702D','#BDA44D','#FF483D','#D400FF','#B9121B','#6600FF','#C9A0DC','#F7230C','#F88E55','#EF9B0F','#FEC3AC','#BB0B0B','#677E52', '#375D81', '#93A299', '#8C5935', '#05966D', '#419F2E', '#FF321D', '#E2AD3B']
	    }); 
        var colors = Highcharts.getOptions().colors,
            categories = var1,
            data = var9;
                                                        
    
        // Build the data arrays
        var browserData = [];
        var versionsData = [];
        for (var i = 0; i < data.length; i++) {
    
            // add browser data
            browserData.push({
                name: categories[i],
                y: data[i].y,
                color: data[i].color
            });
    
            // add version data
            for (var j = 0; j < data[i].drilldown.data.length; j++) {
                var brightness = 0.2 - (j / data[i].drilldown.data.length) / 5 ;
                versionsData.push({
                    name: data[i].drilldown.categories[j],
                    y: data[i].drilldown.data[j],
                    color: Highcharts.Color(data[i].color).brighten(brightness).get()
                });
            }
        }
    
        // Create the chart
        jQuery('#container2').highcharts({
            chart: {
                type: 'pie'
            },
            title: {
                text: var8
            },
            yAxis: {
                title: {
                    text: ' '
                }
            },
            plotOptions: {
                pie: {
                	allowPointSelect: true,
                    cursor: 'pointer',
                    showInLegend: var10,
                    shadow: false,
                    center: ['50%', '50%']
                }
            },
            tooltip: {
        	    valueSuffix: '%'
            },
            series: [{
                name: 'Pourcentage',
                data: browserData,
                size: '60%',
                dataLabels: {
                    formatter: function() {
                        return this.y > 5 ? this.point.name : null;
                    },
                    color: 'white',
                    distance: -30
                }
            }, {
                name: 'Pourcentage',
                data: versionsData,
                size: '80%',
                innerSize: '60%',
                dataLabels: {
                    formatter: function() {
                        // display only if larger than 1
                        return this.y > 1 ? '<b>'+ this.point.name +':</b> '+ this.y +'%'  : null;
                    }
                }
            }]
        });
    });
//]]>