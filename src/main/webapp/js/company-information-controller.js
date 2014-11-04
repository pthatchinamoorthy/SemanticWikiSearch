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