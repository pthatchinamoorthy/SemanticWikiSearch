package edu.ms.pt.sparql.access;

import java.util.List;

public class Companies {
	
	public Companies(List<Company> companies){
		this.setCompanies(companies);
	}
	
	private List<Company> companies;

	public List<Company> getCompanies() {
		return companies;
	}

	public void setCompanies(List<Company> companies) {
		this.companies = companies;
	}

}
