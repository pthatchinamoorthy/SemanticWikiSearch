var semanticWikiSearchApp = angular.module('SemanticWikiSearchApp',['ngRoute']);

semanticWikiSearchApp.config(['$routeProvider', function($routeProvider) {
	 $routeProvider.
	       when('/company/search/:keyword', {
	         templateUrl: 'html/company_search_results.html',
	         controller: 'KeywordSearchResultsController'
	       }).
	       when('/company/search/mutilple-option-search:companyOptions', {
		         templateUrl: 'html/company_search_results.html',
		         controller: 'MultipleOptionSearchResultsController'
		   }).
	       when('/company/id/:id', {
	         templateUrl: 'html/company_information.html',
	         controller: 'CompanyInformationController'
	       }).
	       when('/company/notes/:identifierAndNotes', {
		         templateUrl: 'html/company_information.html',
		         controller: 'CompanyInformationNotesUpdateController'
		   }).
		   otherwise({
		          redirectTo: 'html/companySearch.html'
		   });
}]);


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

semanticWikiSearchApp.controller('KeywordSearchResultsController', function($scope, $http, $location, $routeParams) {
	$scope.company=null;
	$scope.keyword=$routeParams.keyword;
	$http.get("rest/company/search/" + $routeParams.keyword).	 		//$http.get('json/company.json').
		success(function (data) {							
			$scope.companies = data.companies;
			$scope.notes = null;
			}
		);
	
	$scope.getCompanyInfo = function(id){	
		$location.path("/company/id/" + id);
	}
});

semanticWikiSearchApp.controller('MultipleOptionSearchResultsController', function($scope, $http, $location, $routeParams) {
	$http.get("rest/" + "company/search/mutilple-option-search" + $routeParams.companyOptions).									
			success(function (data) {
						$scope.companies = data.companies;
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
			         }		
			);
	
	$scope.getCompanyInfo = function(id){	
		$location.path("/company/id/" + id);
	}
});



	
semanticWikiSearchApp.controller('CompanyInformationController', function($scope, $http, $location, $routeParams) {
	$scope.companies = null;		
	$http.get('rest/company/id/' + $routeParams.id).									
		success(function (data) {
				$scope.company = data;
				$scope.isNotesUpdated = false;
			}
		);
	
	$scope.updateNotes = function(organizationIdentifier){		
		$location.path("/company/notes/" + organizationIdentifier + "?notes=" + $scope.notes);
		
	}
});

semanticWikiSearchApp.controller('CompanyInformationNotesUpdateController', function($scope, $http, $location, $routeParams) {

	$http.put('rest/company/notes/' + $routeParams.identifierAndNotes).
	success(function (data) {
			$scope.company = data;
			$scope.isNotesUpdated = true;
		}
	);
	
	$scope.updateNotes = function(organizationIdentifier){		
		$location.path("/company/notes/" + organizationIdentifier + "?notes=" + $scope.notes);
		
	}
});

	
