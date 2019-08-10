$(document).ready(function() {
	$("div#banks_container ul.banks-items li.bank-item").click(clickBankEvent);
	$("div#topPanel div.currency-button").click(clickCurrencyEvent);
	
	var currencyId = $("div#topPanel div.currency-button.active").attr("id");
	var month = Number($("div.months-dropdown span.chosen").attr("month"));
	updateChartArea(currencyId, month);
});

var clickBankEvent = function() {
	var $bankItem = $(this);
	
	if ($bankItem.hasClass(activeClass)) {
		return;
	}
	
	updateActivity($("div#banks_container ul.banks-items li.bank-item"), $bankItem);
	
	var bankCode = $bankItem.attr("id");
	var month = Number($("div.months-dropdown span.chosen").attr("month"));
	updateEratesPage();
}

function updateEratesPage() {
	var months = Number($("div.months-dropdown span.chosen").attr("month"));
	if (typeof months != 'number') {
		months = 1;
	}
	
	var bankCode = $("ul.banks-items li.bank-item.active").attr("id");
	
	var ctx = $("input#ctx").val();
	document.location.href = ctx + "erates?bankCode=" + bankCode + "&months=" + months;
}

var clickCurrencyEvent = function() {
	var $currencyButton = $(this);
	
	if ($currencyButton.hasClass(activeClass)) {
		return;
	}
	
	updateActivity($("div#topPanel div.currency-button"), $currencyButton);
	
	var currency = $currencyButton.attr("id");
	var month = Number($("div.months-dropdown span.chosen").attr("month"));
	
	updateChartArea(currency, month);
};

function updateActivity($items, $activeItem) {
	$items.removeClass(activeClass);
	$activeItem.addClass(activeClass);
}

function updateChartArea(currencyId, months) {
	var ctx = $("input#ctx").val();
	$.ajax({
		dataType: "json",
		url: ctx + "exchangerates?currencyId=" + currencyId + "&months=" + months,
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