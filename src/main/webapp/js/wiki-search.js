var myApp = angular.module('SemanticWikiSearchApp',[]);
  
myApp.controller('CompanySearchController', ['$scope', '$http', function($scope, $http) {
						
	$scope.keywordSearch = function(){
		$scope.ajax=1;
		$scope.company=null;
		//$http.get('json/company.json').
		$http.get('rest/company/search/' + $scope.keyword).										
			success(function (data) {							
				$scope.companies = data.companies;
			}
		);
		$scope.ajax=null;
		$scope.notes = null;
	}
					
	$scope.multipleOptionSearch = function(){					
			var multipleOptionURL = "company/search/mutilple-option-search" + "?";
			if (typeof $scope.companyName !== "undefined")
				multipleOptionURL = multipleOptionURL + "companyName=" + $scope.companyName; 
			if (typeof $scope.industryName !== "undefined")
				multipleOptionURL = multipleOptionURL + "&industryName=" + $scope.industryName; 
			if (typeof $scope.stockSymbol !== "undefined")
				multipleOptionURL = multipleOptionURL + "&stockSymbol=" + $scope.stockSymbol;
			if (typeof $scope.keyPeople !== "undefined")
				multipleOptionURL = multipleOptionURL + "&keyPeople=" + $scope.keyPeople;
			if (typeof $scope.greaterThanEmployeeSize !== "undefined")
				multipleOptionURL = multipleOptionURL + "&greaterThanEmployeeSize=" + $scope.greaterThanEmployeeSize; 
			if (typeof $scope.lesserThanEmployeeSize !== "undefined")
				multipleOptionURL = multipleOptionURL + "&lesserThanEmployeeSize=" + $scope.lesserThanEmployeeSize;
			if (typeof $scope.foundingDate !== "undefined")
				multipleOptionURL = multipleOptionURL + "&foundingDate=" + $scope.foundingDate;
			if (typeof $scope.foundedBy !== "undefined")
				multipleOptionURL = multipleOptionURL + "&foundedBy=" + $scope.foundedBy;
			if (typeof $scope.income !== "undefined")
				multipleOptionURL = multipleOptionURL + "&income=" + $scope.income;
			if (typeof $scope.revenue !== "undefined")
				multipleOptionURL = multipleOptionURL + "&revenue=" + $scope.revenue; 
			if (typeof $scope.city !== "undefined")
				multipleOptionURL = multipleOptionURL + "&city=" + $scope.city;
			if (typeof $scope.country !== "undefined")
				multipleOptionURL = multipleOptionURL + "&country=" + $scope.country;
			
			$http.get(multipleOptionURL).									
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
				}
			);
	}
				
	$scope.getCompanyInfo = function(id){		
		$scope.companies = null;		
		$http.get('rest/company/id/' + id).									
			success(function (data) {
				$scope.company = data;
				$scope.isNotesUpdated = false;
				}
			);
	}
	
	$scope.updateNotes = function(organizationIdentifier){		
		$http.put('rest/company/notes/' + organizationIdentifier + "?notes=" + $scope.notes).
		success(function (data) {
			$scope.company = data;
			$scope.isNotesUpdated = true;			
			}
		);
	}
	
}]);