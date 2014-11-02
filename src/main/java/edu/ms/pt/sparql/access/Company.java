package edu.ms.pt.sparql.access;

//@JsonSerialize(include=Inclusion.NON_NULL)
public class Company {

	// @JsonProperty("foaf:name")
	private String name = null;

	// @JsonProperty("foaf:url")
	private String url = null;
	
	private String dataSourceUrl = null;

	// @JsonProperty("dbpedia-owl:isPrimaryTopicOf")
	private String isPrimaryTopicOf = null;
	
	private String notes =  "";

	// @JsonProperty("foaf:abstract")
	private String aabstract = null;

	private String symbol = null;

	private String locationCity = null;

	private String locationCountry = null;

	private String revenue = null;
	
	private String netIncome = null;

	private String keyPeople = null;

	private String numEmployees = null;
	
	private String foundedBy = null;
	
	private String foundingDate = null;
	
	private String sameAs = null;

	public String getFoundingDate() {
		return foundingDate;
	}

	public void setFoundingDate(String foundingDate) {
		this.foundingDate = foundingDate;
	}

	public String getFoundedBy() {
		return foundedBy;
	}

	public void setFoundedBy(String foundedBy) {
		this.foundedBy = foundedBy;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getIsPrimaryTopicOf() {
		return isPrimaryTopicOf;
	}

	public void setIsPrimaryTopicOf(String isPrimaryTopicOf) {
		this.isPrimaryTopicOf = isPrimaryTopicOf;
	}

	public String getAabstract() {
		return aabstract;
	}

	public void setAabstract(String aabstract) {
		this.aabstract = aabstract;
	}

	
	public String getRevenue() {
		return revenue;
	}

	public void setRevenue(String revenue) {
		this.revenue = revenue;
	}

	public String getKeyPeople() {
		return keyPeople;
	}

	public void setKeyPeople(String keyPeople) {
		this.keyPeople = keyPeople;
	}

	public String getNumEmployees() {
		return numEmployees;
	}

	public void setNumEmployees(String numEmployees) {
		this.numEmployees = numEmployees;
	}

	public String getLocationCity() {
		return locationCity;
	}

	public void setLocationCity(String locationCity) {
		this.locationCity = locationCity;
	}

	public String getLocationCountry() {
		return locationCountry;
	}

	public void setLocationCountry(String locationCountry) {
		this.locationCountry = locationCountry;
	}
	
	public String getNetIncome() {
		return netIncome;
	}

	public void setNetIncome(String netIncome) {
		this.netIncome = netIncome;
	}
	
	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	
	public String getSameAs() {
		return sameAs;
	}

	public void setSameAs(String sameAs) {
		this.sameAs = sameAs;
	}
	
	public String getDataSourceUrl() {
		return dataSourceUrl;
	}

	public void setDataSourceUrl(String dataSourceUrl) {
		this.dataSourceUrl = dataSourceUrl;
	}
	
	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

}