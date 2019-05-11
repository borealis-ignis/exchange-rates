$(document).ready(function() {
	$("div#topPanel div.currency-button").click(clickCurrencyEvent);
	
	var currencyId = $("div#topPanel div.currency-button.active").attr("id");
	updateChartArea(currencyId);
});

var clickCurrencyEvent = function() {
	var $currencyButton = $(this);
	var activeClass = "active";
	
	if ($currencyButton.hasClass(activeClass)) {
		return;
	}
	
	$("div#topPanel div.currency-button").removeClass(activeClass);
	$currencyButton.addClass(activeClass);
	
	updateChartArea($currencyButton.attr("id"));
};

function updateChartArea(currencyId) {
	var ctx = $("input#ctx").val();
	
	$.ajax({
		dataType: "json",
		url: ctx + "exchangerates?currencyId=" + currencyId,
		success: chartUpdateEvent
	});
}

var chartUpdateEvent = function(data) {
	var buyRates = [];
	var sellRates = [];
	
	for (var i in data) {
		var item = data[i];
		
		var dateTime = new Date(item.updateDate).getTime();
		
		buyRates.push([dateTime, item.buyRate]);
		sellRates.push([dateTime, item.sellRate]);
	}
	
	
	var dataset = [
		{ label: "Sell Rate", data: sellRates },
		{ label: "Buy Rate", data: buyRates }
	];
	
	var options = {
		series: {
			lines: {
				show: true
			},
			points: {
				radius: 5,
				fill: true,
				show: true
			}
		},
		xaxis: {
			mode: "time",
			//tickSize: [1, "day"],
			timezone: "browser",
			axisLabelUseCanvas: true,
			axisLabelFontSizePixels: 12,
			axisLabelFontFamily: 'Verdana, Arial',
			axisLabelPadding: 10
		},
		yaxes: {
			axisLabelUseCanvas: true,
			axisLabelFontSizePixels: 12,
			axisLabelFontFamily: 'Verdana, Arial',
			axisLabelPadding: 10
		},
		legend: {
			container:$("#legendContainer"),
			noColumns: 0,
		},
		grid: {
			hoverable: true,
			clickable: true,
			borderWidth: 1,
			borderColor: "#d5fec6",
			backgroundColor: { colors: ["#ffffff", "#d5fec6"] }
		},
		colors: ["#1fa403", "#0662c6"]
	};
	
	var chartContainer = "div#charts_container div#chart";
	$.plot(chartContainer, dataset, options);
	$(chartContainer).UseTooltip();
	$(chartContainer).UseClickTooltip();
};


// Plugins:
var previousPoint = null;
$.fn.UseTooltip = function () {
	$(this).bind("plothover", toolTipEvent);
};

$.fn.UseClickTooltip = function () {
	$(this).bind("plotclick", toolTipEvent);
};

var toolTipEvent = function (event, pos, item) {
	if (item) {
		if (previousPoint != item.dataIndex) {
			previousPoint = item.dataIndex;
			
			$("#tooltip").remove();
			
			var date = item.datapoint[0];
			var rate = item.datapoint[1];
			showTooltip(item.pageX, item.pageY, "<strong>" + rate + "</strong><br/> " + dateFormat(new Date(date)));
		}
	} else {
		$("#tooltip").remove();
		previousPoint = null;
	}
};

function dateFormat(date) {
	var offsetSign = (date.getTimezoneOffset() < 0)? "-" : "+";
	var offset = Math.abs(date.getTimezoneOffset() / 60);
	
	return date.getFullYear() + "-" + ("0" + (date.getMonth() + 1)).slice(-2) + "-" + ("0" + date.getDate()).slice(-2) + " "
    	+ ("0" + date.getHours()).slice(-2) + ":" + ("0" + date.getMinutes()).slice(-2)
    	+ offsetSign + ("0" + offset).slice(-2) + "00";
}

function showTooltip(x, y, contents) {
	$('<div id="tooltip">' + contents + '</div>').css({
		top: y + 5,
		left: x + 20,
	}).appendTo("body").fadeIn(200);
}