$(document).ready(function() {
	$("div.top-container div.top-buttons div.top-button").click(clickTopButtonEvent);
});


var activeClass = "active";


var clickTopButtonEvent = function() {
	var $topButton = $(this);
	if ($topButton.hasClass(activeClass)) {
		return;
	}
	
	var pageId = $topButton.attr("id");
	
	var ctx = $("input#ctx").val();
	document.location.href = ctx + pageId;
}


function emptyChart() {
	drawChart([], []);
}

function drawChart(dataset, linesColors) {
	
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
		colors: linesColors
	};
	
	var chartContainer = "div#charts_container div#chart";
	$.plot(chartContainer, dataset, options);
	$(chartContainer).UseTooltip();
	$(chartContainer).UseClickTooltip();
}


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