var services = angular.module('exampleApp.services', ['ngResource']);
services.factory('UserService', function($resource) {
	return $resource('rest/user/:action', {},{
				authenticate: {method: 'POST',params: {'action' : 'authenticate'},headers : {'Content-Type': 'application/x-www-form-urlencoded'}},
				});
	});

services.factory('TradeManagerService', function($resource) {
	return $resource('rest/transaction/:id', {id: '@id'},
			{
        getAll: { method: 'GET',isArray:true, params: {}, url: 'rest/transaction/all'},
        deleteAll: { method: 'DELETE',isArray:true, params: {}, url: 'rest/transaction/deleteAll'},
        queryAssetPrices: { method: 'GET',isArray:true, params: {}, url: 'rest/staticdata/assetprices'},
		queryFxRates: { method: 'GET',isArray:true, params: {}, url: 'rest/staticdata/fxrates'}
	});
});

services.directive('ngReallyClick', [function() {
    return {
        restrict: 'A',
        link: function(scope, element, attrs) {
            element.bind('click', function() {
                var message = attrs.ngReallyMessage;
                if (message && confirm(message)) {
                    scope.$apply(attrs.ngReallyClick);
                }
            });
        }
    }
}]);