package edu.ms.pt.sparql.access;

import java.util.List;

public class Companies {
	
	private List<Company> companies;
	
	public Companies(List<Company> companyList) {
		this.companies = companyList;
	}

	public List<Company> getCompanies() {
		return companies;
	}

	public void setCompanies(List<Company> companies) {
		this.companies = companies;
	}	

}
