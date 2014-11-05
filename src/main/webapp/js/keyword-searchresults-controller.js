semanticWikiSearchApp.controller('KeywordSearchResultsController', function($scope, $http, $location, $routeParams) {	
	$scope.company=null;
	
	$scope.keyword=$routeParams.keyword;
	$http.get("rest/company/search/" + $routeParams.keyword).	 		//$http.get('json/company.json').
		success(function (data) {							
			$scope.companies = data.companies;
			$scope.searchingComplete = true;
			
			$scope.companyName = undefined ; 
			$scope.industryName = undefined;
			$scope.stockSymbol = undefined;
			$scope.keyPeople = undefined;
			$scope.greaterThanEmployeeSize = undefined;
			$scope.lesserThanEmployeeSize = undefined;
			$scope.foundingDate = undefined;
			$scope.foundedBy = undefined;
			$scope.income = undefined;
			$scope.revenue = undefined;
			$scope.city = undefined;
			$scope.country = undefined;
			$scope.notes = null;
			$scope.searching=false;
		}).
		error(function(data, status, headers, config) {
			    $scope.searchingComplete = undefined;
			    $scope.error=true;
		});	
	
	$scope.getCompanyInfo = function(id){	
		$location.path("/company/id/" + id);
	}
});