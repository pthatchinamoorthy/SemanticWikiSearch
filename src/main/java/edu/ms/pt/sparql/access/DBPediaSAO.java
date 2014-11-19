package edu.ms.pt.sparql.access;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.UriInfo;

import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.RDFNode;

import edu.ms.pt.model.Companies;
import edu.ms.pt.model.Company;

public class DBPediaSAO {

	public static final boolean DEPLOY_ENV_AMAZAON = false;
	private static final boolean DUMMY_DATA = true;
	
	private static final Logger LOGGER = Logger.getLogger(DBPediaSAO.class);
	
	/**
	 * @param searchKeyword
	 * @param uriInfo 
	 */
	public Companies searchCompanyByKeyword(String searchKeyword, String searchOption, UriInfo uriInfo) {
		LOGGER.log(Priority.INFO, "searchOption --------->" + searchOption);
		
		String sparqlQuery = "SELECT ?organization (STR(?name) as ?nameVar) (STR(?industry) as ?industryVar) (STR(?locationCountry) as ?locationCountryVar) (STR(?keyPeople) as ?keyPeopleVar) " +
							  "WHERE { " +
							  " ?organization <http://xmlns.com/foaf/0.1/name> ?name. ";
		
		if ("Exact".equals(searchOption)) {
			sparqlQuery += "FILTER (str(?name)='" + searchKeyword + "')";
			sparqlQuery += " OPTIONAL { ?organization <http://dbpedia.org/ontology/industry> ?industry. }" + 		
					   " OPTIONAL { ?organization <http://dbpedia.org/property/keyPeople> ?keyPeople. }" +
					   " OPTIONAL { ?organization <http://dbpedia.org/property/locationCountry> ?locationCountry. }";
		} else if ("Contains".equals(searchOption)) {
			sparqlQuery += "FILTER (regex(?name, '" + searchKeyword + "*'" + ", 'i'))";
			sparqlQuery += " OPTIONAL {?organization <http://dbpedia.org/ontology/industry> ?industry. }" + 		
					   " OPTIONAL { ?organization <http://dbpedia.org/property/keyPeople> ?keyPeople. }" +
					   " OPTIONAL { ?organization <http://dbpedia.org/property/locationCountry> ?locationCountry. }";
		} else {
			sparqlQuery += "FILTER (regex(?name, '^" + searchKeyword + "*'" + ", 'i'))";
			sparqlQuery += " ?organization <http://dbpedia.org/ontology/industry> ?industry. " + 		
					   " ?organization <http://dbpedia.org/property/keyPeople> ?keyPeople. " +
					   " ?organization <http://dbpedia.org/property/locationCountry> ?locationCountry. ";
		}
		
		sparqlQuery += " } LIMIT 20";
			
		QueryExecution queryExecution = null;
		ResultSet resultSet = null;
		List<Company> companyList = new ArrayList<Company>();
		try {
			LOGGER.log(Priority.INFO, sparqlQuery);
			queryExecution = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", sparqlQuery);
			resultSet = queryExecution.execSelect();
			while(resultSet.hasNext()) {
				QuerySolution result = resultSet.next();
				RDFNode organizationIdentifier = result.get("organization");
				RDFNode name = result.get("nameVar");
				RDFNode industry = result.get("industryVar");
				RDFNode country = result.get("locationCountryVar");
				RDFNode keyPeople = result.get("keyPeopleVar");
				
				Company company = new Company();
				company.setName(name.toString());
				company.setIndustry(industry != null ? industry.toString() : "-");
				company.setLocationCountry(country != null ? country.toString() : "-");
				company.setKeyPeople(keyPeople != null ? keyPeople.toString() : "-");
				String dataSourceUrl = organizationIdentifier !=  null ? organizationIdentifier.toString() : null;
				company.setDataSourceUrl(dataSourceUrl);
				company.setResourceIdentifier(dataSourceUrl.substring(28));
				
				companyList.add(company);
			}	
			return new Companies(companyList, queryExecution.getQuery().toString());
		} catch(Exception e) {
			LOGGER.error(e);
			if(DUMMY_DATA) {
				insertDummyCompanyRecords(companyList);
				return new Companies(companyList, "DBPEdia is down. " + sparqlQuery);
			}
			throw e;
		}
	}

	/**
	 * @param searchKeyword
	 * @param uriInfo 
	 */
	public Companies searchCompanyByOption(String name, String industry,
										   String symbol, String keyPeople, 
										   String greatThanNumEmployees, String lesserThanNumEmployees,  
										   String foundedBy, String foundingDate, 
										   String locationCity, String locationCountry, 
										   String revenue, String netIncome
										   , UriInfo uriInfo) {	
		String sparqlQuery = "SELECT ?organization (STR(?name) as ?nameVar) (STR(?industry) as ?industryVar) (STR(?locationCountry) as ?locationCountryVar) (STR(?keyPeople) as ?keyPeopleVar) " +
							 "WHERE { " +
							 " ?organization <http://xmlns.com/foaf/0.1/name> ?name. ";
		if (name != null)
			sparqlQuery +=  " FILTER (regex(?name, '" + name + "*', 'i')) ";
		
		if (symbol != null)
			sparqlQuery += "?organization <http://dbpedia.org/property/symbol> ?symbol. FILTER (str(?symbol)='" + symbol + "')";
		if (greatThanNumEmployees != null)
			sparqlQuery += "?organization <http://dbpedia.org/property/numEmployees> ?numEmployees. FILTER (?numEmployees > " + greatThanNumEmployees + ")";
		if (lesserThanNumEmployees != null)
			sparqlQuery += "?organization <http://dbpedia.org/property/numEmployees> ?numEmployees. FILTER (?numEmployees < " + lesserThanNumEmployees + ")";
		if (foundedBy != null)
			sparqlQuery += "?organization <http://dbpedia.org/ontology/foundedBy> ?foundedBy. FILTER (regex(?foundedBy, '" + foundedBy + "*', 'i')) ";
		if (foundingDate != null)
			sparqlQuery += "?organization <http://dbpedia.org/ontology/foundingDate> ?foundingDate. FILTER (str(?foundingDate)='" + foundingDate + "')";
		if (locationCity != null)
			sparqlQuery += "?organization <http://dbpedia.org/property/locationCity> ?locationCity. FILTER (regex(?locationCity, '" + locationCity + "*', 'i')) ";
		if (revenue != null)
			sparqlQuery += "?organization <http://dbpedia.org/property/revenue> ?revenue. FILTER (?revenue > " + revenue + ")";
		if (netIncome != null)
			sparqlQuery += "?organization <http://dbpedia.org/property/netIncome> ?netIncome. FILTER (?netIncome > " + netIncome + ")";
		
		sparqlQuery += " OPTIONAL { ?organization <http://dbpedia.org/ontology/industry> ?industry. }";
		if (industry != null) 
			sparqlQuery += " FILTER (regex(?industry, '" + industry + "*', 'i')) ";
		
		sparqlQuery += "OPTIONAL { ?organization <http://dbpedia.org/property/keyPeople> ?keyPeople. }";
		if (keyPeople != null)
			sparqlQuery += " FILTER (regex(?keyPeople, '" + keyPeople + "*', 'i')) ";
		
		sparqlQuery += "OPTIONAL { ?organization <http://dbpedia.org/property/locationCountry> ?locationCountry. } ";
		if (locationCountry != null)
			sparqlQuery += " FILTER (regex(?locationCountry, '" + locationCountry + "*', 'i')) ";
		
		sparqlQuery += " } LIMIT 20";
		
		QueryExecution queryExecution = null;
		ResultSet resultSet = null;
		List<Company> companyList = new ArrayList<Company>();
		try {
			LOGGER.log(Priority.INFO, sparqlQuery);
			queryExecution = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", sparqlQuery);
			resultSet = queryExecution.execSelect();
			while(resultSet.hasNext()) {
				QuerySolution result = resultSet.next();
				RDFNode organizationIdentifierNode = result.get("organization");
				RDFNode nameNode = result.get("nameVar");
				RDFNode countryNode = result.get("locationCountryVar");
				RDFNode keyPeopleNode = result.get("keyPeopleVar");
				RDFNode industryNode = result.get("industryVar");
				
				Company company = new Company();
				company.setName(nameNode.toString());
				company.setLocationCountry(countryNode != null ? countryNode.toString() : "-");
				company.setKeyPeople(keyPeopleNode != null ? keyPeopleNode.toString() : "-");
				company.setIndustry(industryNode != null ? industryNode.toString() : "-");
				String dataSourceUrl = organizationIdentifierNode !=  null ? organizationIdentifierNode.toString() : null;
				company.setDataSourceUrl(dataSourceUrl);
				company.setResourceIdentifier(dataSourceUrl.substring(28));

				companyList.add(company);
			}	
			return new Companies(companyList, queryExecution.getQuery().toString());
		} catch(Exception e) {
			LOGGER.error(e);
			if(DUMMY_DATA) {
				insertDummyCompanyRecords(companyList);
				return new Companies(companyList, "DPPedia is down. " + sparqlQuery);
			}
			throw e;
		}
	}


	public Company getCompanyInfo(String organisationIdentifier) {
		String rdfStore= DEPLOY_ENV_AMAZAON ? "file:///var/lib/tomcat7/webapps/ROOT/rdf/notes.rdf" : "file:///C:/Users/thatchinamoorthyp/git/SemanticWikiSearch/src/main/webapp/rdf/notes.rdf";
		String sparqlQuery = 
				"SELECT (STR(?name) as ?nameVar) (STR(?notes) as ?notesVar) (STR(?isPrimaryTopicOf) as ?isPrimaryTopicOfVar) (STR(?abstract) as ?abstractVar) (STR(?foundedBy) as ?foundedByVar) (STR(?foundingDate) as ?foundedDateVar) (STR(?locationCity) as ?locationCityVar) (STR(?locationCountry) as ?locationCountryVar) (STR(?keyPeople) as ?keyPeopleVar) (STR(?symbol) as ?symbolVar) (STR(?revenue) as ?revenueVar) (STR(?netIncome) as ?netIncomeVar) (STR(?numEmployees) as ?numEmployeesVar)" +
							" FROM <" + "http://dbpedia.org/resource/" + organisationIdentifier  + ">" +
							" FROM NAMED <" + rdfStore + ">" +
							" WHERE" + 
							" {" +
								" OPTIONAL{ <" + "http://dbpedia.org/resource/" + organisationIdentifier + "> ?property ?value.}" +
								" OPTIONAL{ <" + "http://dbpedia.org/resource/" + organisationIdentifier + "> <http://xmlns.com/foaf/0.1/name> ?name.}" +
								" OPTIONAL{<" + "http://dbpedia.org/resource/" + organisationIdentifier + "> <http://xmlns.com/foaf/0.1/isPrimaryTopicOf> ?isPrimaryTopicOf.}" + 
								" OPTIONAL{ <" + "http://dbpedia.org/resource/" + organisationIdentifier + "> <http://dbpedia.org/ontology/abstract> ?abstract. FILTER langMatches( lang(?abstract), \"en\" )}" +
								" OPTIONAL{ <" + "http://dbpedia.org/resource/" + organisationIdentifier + "> <http://dbpedia.org/ontology/foundedBy> ?foundedBy.}" +
								" OPTIONAL{ <" + "http://dbpedia.org/resource/" + organisationIdentifier + "> <http://dbpedia.org/ontology/foundingDate> ?foundingDate.}" +
								" OPTIONAL{ <" + "http://dbpedia.org/resource/" + organisationIdentifier + "> <http://dbpedia.org/property/locationCity> ?locationCity.}" +
								" OPTIONAL{ <" + "http://dbpedia.org/resource/" + organisationIdentifier + "> <http://dbpedia.org/property/locationCountry> ?locationCountry.}" +
								" OPTIONAL{ <" + "http://dbpedia.org/resource/" + organisationIdentifier + "> <http://dbpedia.org/property/keyPeople> ?keyPeople.}" +
								" OPTIONAL{ <" + "http://dbpedia.org/resource/" + organisationIdentifier + "> <http://dbpedia.org/property/symbol> ?symbol.}" +
								" OPTIONAL{ <" + "http://dbpedia.org/resource/" + organisationIdentifier + "> <http://dbpedia.org/property/revenue> ?revenue.}" +
								" OPTIONAL{ <" + "http://dbpedia.org/resource/" + organisationIdentifier + "> <http://dbpedia.org/property/netIncome> ?netIncome.}" +
								" OPTIONAL{ <" + "http://dbpedia.org/resource/" + organisationIdentifier + "> <http://dbpedia.org/property/numEmployees> ?numEmployees.}" +
								" OPTIONAL {" +
									"GRAPH <" + rdfStore + "> " +
										" {<" + "http://dbpedia.org/resource/" + organisationIdentifier + "> <http://prabhakar.com/notes> ?notes.}" +
								"}" +
							"}" +
							" LIMIT 5";
		
		QueryExecution queryExecution = null;
		ResultSet resultSet = null;
		try {
			LOGGER.log(Priority.INFO, sparqlQuery);
			queryExecution = QueryExecutionFactory.create(sparqlQuery);
			resultSet = queryExecution.execSelect();
			Company company = new Company();
			company.setResourceIdentifier(organisationIdentifier);
			company.setDataSourceUrl("http://dbpedia.org/resource/" + organisationIdentifier);
			
			while(resultSet.hasNext()) {
				QuerySolution result = resultSet.next();
				company.setName(result.get("nameVar") !=null ? result.get("nameVar").toString() : "-");
				company.setIsPrimaryTopicOf(result.get("isPrimaryTopicOfVar") != null ? result.get("isPrimaryTopicOfVar").toString() : "-");
				company.setAabstract(result.get("abstractVar") != null ? result.get("abstractVar").toString() : "-");
				company.setFoundedBy(result.get("foundedByVar") != null ? result.get("foundedByVar").toString() : "-");
				company.setFoundingDate(result.get("foundingDateVar") != null ? result.get("foundingDateVar").toString() : "-");
				company.setLocationCity(result.get("locationCityVar") != null ? result.get("locationCityVar").toString() : "-");
				company.setLocationCountry(result.get("locationCountryVar") != null ? result.get("locationCountryVar").toString() : "-");
				company.setKeyPeople(result.get("keyPeopleVar") != null ? result.get("keyPeopleVar").toString() : "-");
				company.setSymbol(result.get("symbolVar") != null ? result.get("symbolVar").toString() : "-");
				company.setRevenue(result.get("revenueVar") != null ? result.get("revenueVar").toString() : "-");
				company.setNetIncome(result.get("netIncomeVar") != null ? result.get("netIncomeVar").toString() : "-") ;
				company.setNumEmployees(result.get("numEmployeesVar") != null ? result.get("numEmployeesVar").toString() : "-") ;
				company.setNotes(result.get("notesVar") != null 
										? (company.getNotes().contains(result.get("notesVar").toString())  
												? company.getNotes() 
												: company.getNotes() + "." + result.get("notesVar").toString()) 
										: "No Saved Notes for this company");
				company.setSparqlQuery(queryExecution.getQuery().toString());
			}	
			return company;
		} catch(Exception e) {
			LOGGER.error(e);
			if(DUMMY_DATA) {
				return ReturnCompanyWithDummyData(sparqlQuery);
			}
			throw e;
		}
	}

	private Company ReturnCompanyWithDummyData(String sparqlQuery) {
		Company company = new Company();
		company.setName("Dell Inc");
		company.setIsPrimaryTopicOf("");
		company.setAabstract("Dell Inc. is an American privately owned multinational computer technology company " +
				"based in Round Rock, Texas, United States, that develops, sells, repairs and supports computers and" +
				" related products and services. Bearing the name of its founder, Michael Dell, the company is one of " +
				"the largest technological corporations in the world, employing more than 103,300 people worldwide.Dell" +
				" sells personal computers, servers, data storage devices, network switches, software, computer " +
				"peripherals, HDTVs, cameras, printers, MP3 players and also electronics built by other manufacturers.");
		company.setFoundedBy("http://dbpedia.org/resource/Michael_Dell");
		company.setFoundingDate("");
		company.setLocationCity("http://dbpedia.org/resource/Round_Rock,_Texas");
		company.setLocationCountry("United States");
		company.setKeyPeople("http://dbpedia.org/resource/Michael_Dell");
		company.setSymbol("");
		company.setRevenue("US$ 56.94 billion");
		company.setNetIncome("US$ 2.37 billion") ;
		company.setNumEmployees("108800") ;
		company.setNotes("This is dummy notes, as DBPedia is down now.");
		company.setSparqlQuery("DBPedia is down" + sparqlQuery);
		return company;
	}
	
	private void insertDummyCompanyRecords(List<Company> companyList) {
		Company company = new Company();
		company.setName("Dell Inc.");
		company.setIndustry("Sofware Services");
		company.setLocationCountry("United States");
		company.setKeyPeople("Michael Dell");
		company.setResourceIdentifier("Dell_Inc");				
		companyList.add(company);
		company.setName("Microsoft Inc.");
		company.setIndustry("Computer Software");
		company.setLocationCountry("United States");
		company.setKeyPeople("Bill Gates");
		company.setResourceIdentifier("Microsoft");				
		companyList.add(company);
		company.setName("Apple Inc");
		company.setIndustry("Computer Hardware");
		company.setLocationCountry("United States");
		company.setKeyPeople("Tim Cook");
		company.setResourceIdentifier("Apple_Inc");				
		companyList.add(company);
	}
}