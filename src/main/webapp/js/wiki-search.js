function CompanySearchController($scope, $http) {
						
	$scope.keywordSearch = function(){
		$scope.ajax=1;
		$scope.company=null;
		//$http.get('json/company.json').
		$http.get('company/search/' + $scope.keyword).										
			success(function (data) {							
				$scope.companies = data.companies;
			}
		);
		$scope.ajax=null;
		$scope.notes = null;
	}
					
	$scope.multipleOptionSearch = function(){					
			var multipleOptionURL = 'company/mutilple-option-search?' + "companyName=" + $scope.companyName + "&industryName=" + $scope.industryName + "&stockSymbol=" + $scope.stockSymbol + "&keyPeople=" + $scope.keyPeople + "&greaterThanEmployeeSize=" + $scope.greaterThanEmployeeSize + "&lesserThanEmployeeSize=" + $scope.lesserThanEmployeeSize + "&foundedBy=" + $scope.foundedBy + "&income=" + $scope.income + "&revenue=" + $scope.revenue + "&city=" + $scope.city + "&state=" + $scope.state + "&country=" + $scope.country + "&xxx=" + $scope.xxx;
			alert(multipleOptionURL);																  
			$http.get($multipleOptionURL).									
			success(function (data) {
				$scope.companies = data.companies;
				}
			);
	}
				
	$scope.getCompanyInfo = function(companyUrl){		
		$scope.companies = null;
		//var companyUrl = companyUrl.substring(38); 
		var companyUrl = companyUrl.substring(48);				 // AMAZON
		$http.get(companyUrl).									
			success(function (data) {
				$scope.company = data;
				}
			);
	}
	
	$scope.updateNotes = function(name){		
		$http.put('company/' + name + "?&notes=" + $scope.notes).
		success(function (data) {
			$scope.isNotesUpdated = true;			
			}
		);
	}
	
}