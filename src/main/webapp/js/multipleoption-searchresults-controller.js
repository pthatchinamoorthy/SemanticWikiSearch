semanticWikiSearchApp.controller('MultipleOptionSearchResultsController', function($scope, $http, $location, $routeParams) {
	$scope.error=false;
	$http.get("rest/" + "company/search/mutilple-option-search" + $routeParams.companyOptions).									
			success(function (data) {
						$scope.companies = data.companies;
						$scope.searchingComplete = true;
						
						$scope.keyword = undefined;
						$scope.notes = null;
			}).
			error(function(data, status, headers, config) {
				$scope.searchingComplete = true;
			    $scope.error=true;
			});
	
	$scope.getCompanyInfo = function(id){	
		$location.path("/company/id/" + id);
	}
});
