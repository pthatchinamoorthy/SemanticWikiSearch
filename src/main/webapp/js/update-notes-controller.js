semanticWikiSearchApp.controller('CompanyInformationNotesUpdateController', function($scope, $http, $location, $routeParams) {

	$http.put('rest/company/notes/' + $routeParams.identifierAndNotes).
	success(function (data) {
			$scope.company = data;
			$scope.isNotesUpdated = true;
			$scope.searchingComplete = true;
		}
	);
	
	$scope.updateNotes = function(organizationIdentifier){		
		$location.path("/company/notes/" + organizationIdentifier + "?notes=" + $scope.notes);
		
	}
});