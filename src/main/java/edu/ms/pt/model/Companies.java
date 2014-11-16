package edu.ms.pt.model;

import java.util.List;

public class Companies {

	public Companies(List<Company> companyList, String sparqlQuery) {
		this.companies = companyList;
		this.sparqlQuery = sparqlQuery;
	}
	
	private List<Company> companies;
	
	private String sparqlQuery;
	
	public String getSparqlQuery() {
		return sparqlQuery;
	}

	public void setSparqlQuery(String sparqlQuery) {
		this.sparqlQuery = sparqlQuery;
	}
	
	public List<Company> getCompanies() {
		return companies;
	}

	public void setCompanies(List<Company> companies) {
		this.companies = companies;
	}	

}
