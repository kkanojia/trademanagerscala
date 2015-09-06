function AssetPricesIndexController($scope, TradeManagerService) {
	$scope.assetPrices = TradeManagerService.queryAssetPrices();
};

function FxRatesIndexController($scope, TradeManagerService) {
	$scope.fxRateEntries = TradeManagerService.queryFxRates();
};
