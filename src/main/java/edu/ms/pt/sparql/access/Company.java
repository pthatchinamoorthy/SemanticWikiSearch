package edu.ms.pt.sparql.access;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

@JsonSerialize(include=Inclusion.NON_NULL)
public class Company {
	
	@JsonProperty("foaf:name")
	private String companyName = null;
	
	@JsonProperty("foaf:url")
	private String url = null;
	
	@JsonProperty("dbpedia-owl:isPrimaryTopicOf")
	private String dataSourceUrl = null;
	
	@JsonProperty("foaf:abstract")
	private String description = null;
	
	public String getDataSourceUrl() {
		return dataSourceUrl;
	}

	public void setDataSourceUrl(String dataSourceUrl) {
		this.dataSourceUrl = dataSourceUrl;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}