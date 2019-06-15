$(document).ready(function() {
	$("div#banks_container ul.banks-items li.bank-item").click(clickBankEvent);
	$("div#topPanel div.rate-type-button").click(clickRateTypeEvent);
	$("div#topPanel div.currency-button").click(clickCurrencyEvent);
	emptyChart();
});

var clickBankEvent = function() {
	var $bankItem = $(this);
	
	if ($bankItem.hasClass(activeClass)) {
		$bankItem.removeClass(activeClass);
	} else {
		$bankItem.addClass(activeClass);
	}
	
	updateChartArea();
}

var clickRateTypeEvent = function() {
	var $rateTypeItem = $(this);
	$("div#topPanel div.rate-type-button").removeClass(activeClass);
	$rateTypeItem.addClass(activeClass);
	
	updateChartArea();
}

var clickCurrencyEvent = function() {
	var $currencyItem = $(this);
	$("div#topPanel div.currency-button").removeClass(activeClass);
	$currencyItem.addClass(activeClass);
	
	updateChartArea();
}


function updateChartArea() {
	var activeBanks = $("div#banks_container ul.banks-items li.bank-item.active").map(function() { return this.id; }).get();
	if (activeBanks.length == 0) {
		emptyChart();
		return;
	}
	
	var currencyId = $("div#topPanel div.currency-button.active").attr("id");
	var rateType = $("div#topPanel div.rate-type-button.active").attr("id");
	
	var ctx = $("input#ctx").val();
	$.ajax({
		dataType: "json",
		url: ctx + "exchangerates?currencyId=" + currencyId,
		success: function(data) {
			updateChart(data, activeBanks, rateType);
		}
	});
}

function updateChart(data, activeBanks, rateType) {
	
	var dataset = [];
	
	for (var i in activeBanks) {
		var activeBankCode = activeBanks[i];
		
		var rates = [];
		for (var j in data) {
			var rateItem = data[j];
			if (rateItem.bank.code == activeBankCode) {
				var dateTime = new Date(rateItem.updateDate).getTime();
				if (rateType === "buy") {
					rates.push([dateTime, rateItem.buyRate]);
				} else if (rateType === "sell") {
					rates.push([dateTime, rateItem.sellRate]);
				}
			}
		}
		
		var chartItem = { label: activeBankCode, data: rates };
		dataset.push(chartItem);
	}
	
	var linesColors = ["#1fa403", "#0662c6", "#8d7802", "#be2d05", "#b405be", "#dae003", "#000000", "#06e2dc", "#25fa21", "#51038e"];
	
	drawChart(dataset, linesColors);
}
