semanticWikiSearchApp.controller('MainController', function($scope, $http, $location) {
	
	$scope.keywordSearch = function(){		
		$location.path("company/search/" + $scope.keyword);		
	}
	
	$scope.multipleOptionSearch = function(){	
		var multipleOptionPath = "company/search/mutilple-option-search";
		var queryParams = "?";
		if (typeof $scope.companyName !== "undefined")
			queryParams = queryParams + "companyName=" + $scope.companyName; 
		if (typeof $scope.industryName !== "undefined")
			queryParams = queryParams + "&industryName=" + $scope.industryName; 
		if (typeof $scope.stockSymbol !== "undefined")
			queryParams = queryParams + "&stockSymbol=" + $scope.stockSymbol;
		if (typeof $scope.keyPeople !== "undefined")
			queryParams = queryParams + "&keyPeople=" + $scope.keyPeople;
		if (typeof $scope.greaterThanEmployeeSize !== "undefined")
			queryParams = queryParams + "&greaterThanEmployeeSize=" + $scope.greaterThanEmployeeSize; 
		if (typeof $scope.lesserThanEmployeeSize !== "undefined")
			queryParams = queryParams + "&lesserThanEmployeeSize=" + $scope.lesserThanEmployeeSize;
		if (typeof $scope.foundingDate !== "undefined")
			queryParams = queryParams + "&foundingDate=" + $scope.foundingDate;
		if (typeof $scope.foundedBy !== "undefined")
			queryParams = queryParams + "&foundedBy=" + $scope.foundedBy;
		if (typeof $scope.income !== "undefined")
			queryParams = queryParams + "&income=" + $scope.income;
		if (typeof $scope.revenue !== "undefined")
			queryParams = queryParams + "&revenue=" + $scope.revenue; 
		if (typeof $scope.city !== "undefined")
			queryParams = queryParams + "&city=" + $scope.city;
		if (typeof $scope.country !== "undefined")
			queryParams = queryParams + "&country=" + $scope.country;
		
		$location.path("company/search/mutilple-option-search" + queryParams);
	}
});