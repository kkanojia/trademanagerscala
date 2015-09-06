function TransactionsIndexController($scope, TradeManagerService, $rootScope,  $modal,$timeout) {
	$scope.loading = true;

	$scope.transactionEntries = TradeManagerService.getAll();

	$scope.loading = false;

	$scope.deleteAllEntries = function() {
		$scope.loading = true;
		TradeManagerService.deleteAll();
		 $timeout(reloadTransactions, 3000);
		
	};
	
	function reloadTransactions() {
		$scope.transactionEntries = TradeManagerService.getAll();
		$scope.loading = false;
	}
	

	$scope.openInformation = function(){
		 var modalInstance = $modal.open({
		      animation: true,
		      templateUrl: 'partials/transactions/infoContent.htm',
		    });
	};

};

function CreateTransactionController($scope, $location, TradeManagerService, $rootScope) {
	$rootScope.infoMessage = "";
	$rootScope.error = "";
	$scope.minDate = new Date("August 01, 2015 00:00:00");
	$scope.maxDate = new Date("August 31, 2015 00:00:00");
	$scope.tradeEntry = new TradeManagerService();
	$scope.tradeEntry.tradeDate = $scope.minDate;

	$scope.buysell = {
		types : [ "Buy", "Sell" ]
	}

	// Ideally Get it from DB
	$scope.assetIds = [ {
		"id" : 1,
		"name" : "ABC"
	}, {
		"id" : 2,
		"name" : "DEF"
	}, {
		"id" : 3,
		"name" : "XYZ"
	}, ]

	$scope.save = function() {
		$rootScope.infoMessage = "";
		$rootScope.error = "";
		$scope.tradeEntry.assetId = $scope.tradeEntry.assetinfo.id;
		$scope.tradeEntry.tradeDate = $scope.format($scope.tradeEntry.tradeDate, 'dd-MMM-yyyy');

		$scope.tradeEntry.$save(function(responseEntry) {
			if (responseEntry == undefined || responseEntry.count < 1) {
				$rootScope.infoMessage = "Short selling is not allowed. Kunal was too sleepy to fix it. Hire him, he will fix it :)"
			}
			$location.path('/transactions/index');
		});
	};

	//fix for timezone
	$scope.format = function date2str(x, y) {
	    var z = {
	        M: x.getMonth() + 1,
	        d: x.getDate(),
	        h: x.getHours(),
	        m: x.getMinutes(),
	        s: x.getSeconds()
	    };
	    y = y.replace(/(M+|d+|h+|m+|s+)/g, function(v) {
	        return ((v.length > 1 ? "0" : "") + eval('z.' + v.slice(-1))).slice(-2)
	    });

	    return y.replace(/(y+)/g, function(v) {
	        return x.getFullYear().toString().slice(-v.length)
	    });
	};

};