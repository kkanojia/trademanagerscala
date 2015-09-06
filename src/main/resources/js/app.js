angular.module('exampleApp',
		[ 'ngRoute', 'ngCookies', 'smart-table', 'exampleApp.services', 'ui.bootstrap', 'ngSanitize', 'ui.select']).config(
		[ '$routeProvider', '$locationProvider', '$httpProvider', function($routeProvider, $locationProvider, $httpProvider) {

			$routeProvider.when('/transactions/index', {
				templateUrl : 'partials/transactions/index.htm',
				controller : TransactionsIndexController
			});

			$routeProvider.when('/transaction/create', {
				templateUrl : 'partials/transactions/create.htm',
				controller : CreateTransactionController
			});

			$routeProvider.when('/login', {
				templateUrl : 'partials/transactions/login.htm',
				controller : LoginController
			});

			$routeProvider.when('/assetprices', {
				templateUrl : 'partials/staticdata/assetprices.htm',
				controller : AssetPricesIndexController
			});

			$routeProvider.when('/fxrates', {
				templateUrl : 'partials/staticdata/fxrates.htm',
				controller : FxRatesIndexController
			});


			$routeProvider.otherwise({
				templateUrl : 'partials/transactions/index.htm',
				controller : TransactionsIndexController
			});

			$locationProvider.hashPrefix('!');

			$httpProvider.interceptors.push(function($q, $rootScope, $location) {
				return {
					'responseError' : function(rejection) {
						var status = rejection.status;
						var config = rejection.config;
						var method = config.method;
						var url = config.url;

						if (status == 401) {
							$rootScope.error = "Incorrect credentials. Hire Kunal, he knows how to fix it."
							delete $rootScope.user;
							delete $rootScope.authToken;
							$cookieStore.remove('authToken');
							$location.path("/login");
						} else {
							$rootScope.infoMessage = "";
							$rootScope.error = method + " on " + url + " failed with status " + status + ". Hire Kunal, he knows how to fix it.";
							delete $rootScope.user;
							delete $rootScope.authToken;
							$cookieStore.remove('authToken');
						}

						return $q.reject(rejection);
					}
				};
			});

			$httpProvider.interceptors.push(function($q, $rootScope, $location) {
				return {
					'request' : function(config) {
						var isRestCall = config.url.indexOf('rest') == 0;
						if (isRestCall && angular.isDefined($rootScope.authToken)) {
							var authToken = $rootScope.authToken;
							if (exampleAppConfig.useAuthTokenHeader) {
								config.headers['X-Auth-Token'] = authToken;
							} else {
								config.url = config.url + "?token=" + authToken;
							}
						}
						return config || $q.when(config);
					}
				};
			});

		} ]

).run(function($rootScope, $location, $cookieStore, UserService) {
	$rootScope.$on('$viewContentLoaded', function() {
		delete $rootScope.error;
	});

	$rootScope.hasRole = function(role) {
		if ($rootScope.user === undefined) {
			return false;
		}
		if ($rootScope.user.roles[role] === undefined) {
			return false;
		}
		return $rootScope.user.roles[role];
	};

	$rootScope.logout = function() {
		delete $rootScope.user;
		delete $rootScope.authToken;
		$cookieStore.remove('authToken');
		$location.path("/login");
	};

	$rootScope.openDatePickers = function($event, opened) {
		$event.preventDefault();
		$event.stopPropagation();
		$rootScope[opened] = true;
	};

	var originalPath = $location.path();
	$location.path("/login");
	var authToken = $cookieStore.get('authToken');
	if (authToken !== undefined) {
		$rootScope.authToken = authToken;
		UserService.get(function(user) {
			$rootScope.user = user;
			$location.path(originalPath);
		});
	}
	$rootScope.initialized = true;
});

function LoginController($scope, $rootScope, $location, $cookieStore, UserService) {
	$scope.loginTitle = "Trade manager Login"
	$scope.rememberMe = true;

	$scope.login = function() {
		$rootScope.error="";
		$rootScope.infoMessage = "Logging in...";
		UserService.authenticate($.param({
			username : $scope.username,
			password : $scope.password
		}), function(authenticationResult) {
			var authToken = authenticationResult.token;
			$rootScope.authToken = authToken;
			if ($scope.rememberMe) {
				$cookieStore.put('authToken', authToken);
			}
			$rootScope.infoMessage = "";
			UserService.get(function(user) {
				$rootScope.user = user;
				$location.path("/");
			});
		});
	};
};

angular.module('exampleApp').directive('datepickerPopup', function(dateFilter, datepickerPopupConfig) {
	return {
		restrict : 'A',
		priority : 1,
		require : 'ngModel',
		link : function(scope, element, attr, ngModel) {
			var dateFormat = attr.datepickerPopup || datepickerPopupConfig.datepickerPopup;
			ngModel.$formatters.push(function(value) {
				return dateFilter(value, dateFormat);
			});
		}
	};
});

