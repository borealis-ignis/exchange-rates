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
}

function updateChartArea(currencyId) {
	//TODO: send request to get rates (use flot to display it)
	console.log("currencyId: " + currencyId);
}