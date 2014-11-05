semanticWikiSearchApp.controller('MultipleOptionSearchResultsController', function($scope, $http, $location, $routeParams) {
	$http.get("rest/" + "company/search/mutilple-option-search" + $routeParams.companyOptions).									
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
			}).
			error(function(data, status, headers, config) {
			    $scope.error=true;
			});
	
	$scope.getCompanyInfo = function(id){	
		$location.path("/company/id/" + id);
	}
});
