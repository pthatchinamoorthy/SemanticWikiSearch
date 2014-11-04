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
		          redirectTo: ''
		   });
}]);