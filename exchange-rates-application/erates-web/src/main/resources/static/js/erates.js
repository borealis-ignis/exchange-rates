$(document).ready(function() {
	$("div#banks_container ul.banks-items li.bank-item").click(clickBankEvent);
	$("div#topPanel div.currency-button").click(clickCurrencyEvent);
	
	var currencyId = $("div#topPanel div.currency-button.active").attr("id");
	updateChartArea(currencyId);
});

var clickBankEvent = function() {
	var $bankItem = $(this);
	
	if ($bankItem.hasClass(activeClass)) {
		return;
	}
	
	updateActivity($("div#banks_container ul.banks-items li.bank-item"), $bankItem);
	
	var bankCode = $bankItem.attr("id");
	
	var ctx = $("input#ctx").val();
	document.location.href = ctx + "erates?bankCode=" + bankCode;
}

var clickCurrencyEvent = function() {
	var $currencyButton = $(this);
	
	if ($currencyButton.hasClass(activeClass)) {
		return;
	}
	
	updateActivity($("div#topPanel div.currency-button"), $currencyButton);
	
	updateChartArea($currencyButton.attr("id"));
};

function updateActivity($items, $activeItem) {
	$items.removeClass(activeClass);
	$activeItem.addClass(activeClass);
}

function updateChartArea(currencyId) {
	var ctx = $("input#ctx").val();
	$.ajax({
		dataType: "json",
		url: ctx + "exchangerates?currencyId=" + currencyId,
		success: chartUpdateEvent
	});
}

var chartUpdateEvent = function(data) {
	var activeBankCode = $("div#banks_container ul.banks-items li.bank-item.active").attr("id");
	
	var buyRates = [];
	var sellRates = [];
	
	for (var i in data) {
		var item = data[i];
		if (item.bank.code == activeBankCode) {
			var dateTime = new Date(item.updateDate).getTime();
			
			buyRates.push([dateTime, item.buyRate]);
			sellRates.push([dateTime, item.sellRate]);
		}
	}
	
	
	var dataset = [
		{ label: "Sell Rate", data: sellRates },
		{ label: "Buy Rate", data: buyRates }
	];
	
	
	var linesColors = ["#1fa403", "#0662c6"];
	
	drawChart(dataset, linesColors);
}