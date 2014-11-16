semanticWikiSearchApp.controller('MainController', function($scope, $http, $location) {
	$scope.searchOption="StartsWith";
	
	$scope.keywordSearch = function(){
		var params = $scope.keyword
		if (typeof $scope.searchOption !== "undefined")
			params = params + "?&search_option=" + $scope.searchOption;
		$location.path("company/search/" + params);		
	}
	
	$scope.multipleOptionSearch = function(){	
		var multipleOptionPath = "company/search/mutilple-option-search";
		var queryParams = "?";
		if (typeof $scope.companyName !== "undefined" && $scope.companyName !== "")
			queryParams = queryParams + "companyName=" + $scope.companyName; 
		if (typeof $scope.industryName !== "undefined" && $scope.industryName !== "")
			queryParams = queryParams + "&industryName=" + $scope.industryName; 
		if (typeof $scope.stockSymbol !== "undefined" && $scope.stockSymbol !== "")
			queryParams = queryParams + "&stockSymbol=" + $scope.stockSymbol;
		if (typeof $scope.keyPeople !== "undefined" && $scope.keyPeople !== "")
			queryParams = queryParams + "&keyPeople=" + $scope.keyPeople;
		if (typeof $scope.greaterThanEmployeeSize !== "undefined" && $scope.greaterThanEmployeeSize !== "")
			queryParams = queryParams + "&greaterThanEmployeeSize=" + $scope.greaterThanEmployeeSize; 
		if (typeof $scope.lesserThanEmployeeSize !== "undefined" && $scope.lesserThanEmployeeSize !== "")
			queryParams = queryParams + "&lesserThanEmployeeSize=" + $scope.lesserThanEmployeeSize;
		if (typeof $scope.foundingDate !== "undefined" && $scope.foundingDate !== "")
			queryParams = queryParams + "&foundingDate=" + $scope.foundingDate;
		if (typeof $scope.foundedBy !== "undefined" && $scope.foundedBy !== "")
			queryParams = queryParams + "&foundedBy=" + $scope.foundedBy;
		if (typeof $scope.income !== "undefined" && $scope.income !== "")
			queryParams = queryParams + "&income=" + $scope.income;
		if (typeof $scope.revenue !== "undefined" && $scope.revenue !== "")
			queryParams = queryParams + "&revenue=" + $scope.revenue; 
		if (typeof $scope.city !== "undefined" && $scope.city !== "")
			queryParams = queryParams + "&city=" + $scope.city;
		if (typeof $scope.country !== "undefined" && $scope.country !== "")
			queryParams = queryParams + "&country=" + $scope.country;
		
		$location.path("company/search/mutilple-option-search" + queryParams);
	}
});