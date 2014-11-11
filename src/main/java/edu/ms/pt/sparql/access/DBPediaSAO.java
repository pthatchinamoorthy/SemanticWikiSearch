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
	
	private static final Logger LOGGER = Logger.getLogger(DBPediaSAO.class);
	
	/**
	 * @param searchKeyword
	 * @param uriInfo 
	 */
	public Companies searchCompanyByKeyword(String searchKeyword, String searchOption, UriInfo uriInfo) {
		LOGGER.log(Priority.INFO, "searchOption --------->" + searchOption);
		
		String sparqlQuery = "SELECT ?organization ?name ?locationCountry ?keyPeople " +
							  "WHERE { " +
							  "OPTIONAL { ?organization <http://xmlns.com/foaf/0.1/name> ?name. } ";
		
		if ("Exact".equals(searchOption)) 
			sparqlQuery += "FILTER (str(?name)='" + searchKeyword + "')";
		else if ("Contains".equals(searchOption))
			sparqlQuery += "FILTER (regex(?name, '" + searchKeyword + "*'" + ", 'i')) ";
		else
			sparqlQuery += "FILTER (regex(?name, '^" + searchKeyword + "*'" + ", 'i')) ";
		
		sparqlQuery += "OPTIONAL { ?organization <http://dbpedia.org/property/keyPeople> ?keyPeople. }" +
					   "OPTIONAL { ?organization <http://dbpedia.org/property/locationCountry> ?locationCountry. }" +
						"} " + "LIMIT 15";
		
		LOGGER.log(Priority.INFO, sparqlQuery);
		QueryExecution queryExecution = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", sparqlQuery);
		ResultSet resultSet = queryExecution.execSelect();
		LOGGER.log(Priority.INFO, resultSet.toString());
		
		List<Company> companyList = new ArrayList<Company>();
		
		while(resultSet.hasNext()) {
			QuerySolution result = resultSet.next();
			RDFNode organizationIdentifier = result.get("organization");
			RDFNode name = result.get("name");
			RDFNode country = result.get("locationCountry");
			RDFNode keyPeople = result.get("keyPeople");
			
			Company company = new Company();
			company.setName(name.toString());
			company.setLocationCountry(country != null ? country.toString() : "-");
			company.setKeyPeople(keyPeople != null ? keyPeople.toString() : "-");
			String dataSourceUrl = organizationIdentifier !=  null ? organizationIdentifier.toString() : null;
			company.setDataSourceUrl(dataSourceUrl);
			company.setResourceIdentifier(dataSourceUrl.substring(28));
			
			companyList.add(company);
		}	
		return new Companies(companyList);
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
		String sparqlQuery = "SELECT ?organization ?name ?locationCountry ?keyPeople " +
							 "WHERE { " +
							 "OPTIONAL { ?organization <http://xmlns.com/foaf/0.1/name> ?name. } ";
		
		
							 
		
		if (name != null)
			sparqlQuery +=  " FILTER (regex(?name, '" + name + "*', 'i')) ";
		
		if (industry != null) 
			sparqlQuery += "?organization <http://dbpedia.org/ontology/industry> ?industry. FILTER (regex(?industry, '" + industry + "*', 'i')) ";
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
		
		sparqlQuery += "OPTIONAL { ?organization <http://dbpedia.org/property/keyPeople> ?keyPeople. }";
		if (keyPeople != null)
			sparqlQuery += " FILTER (regex(?keyPeople, '" + keyPeople + "*', 'i')) ";
		 sparqlQuery += "OPTIONAL { ?organization <http://dbpedia.org/property/locationCountry> ?locationCountry. } ";
		if (locationCountry != null)
			sparqlQuery += " FILTER (regex(?locationCountry, '" + locationCountry + "*', 'i')) ";
		
		sparqlQuery += " } LIMIT 15";
		
		LOGGER.log(Priority.INFO, sparqlQuery);
		QueryExecution queryExecution = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", sparqlQuery);
		ResultSet resultSet = queryExecution.execSelect();
		LOGGER.log(Priority.INFO, resultSet.toString());
		
		List<Company> companyList = new ArrayList<Company>();
		
		while(resultSet.hasNext()) {
			QuerySolution result = resultSet.next();
			RDFNode organizationIdentifierNode = result.get("organization");
			RDFNode nameNode = result.get("name");
			RDFNode countryNode = result.get("locationCountry");
			RDFNode keyPeopleNode = result.get("keyPeople");
			
			Company company = new Company();
			company.setName(nameNode.toString());
			company.setLocationCountry(countryNode != null ? countryNode.toString() : "-");
			company.setKeyPeople(keyPeopleNode != null ? keyPeopleNode.toString() : "-");
			String dataSourceUrl = organizationIdentifierNode !=  null ? organizationIdentifierNode.toString() : null;
			company.setDataSourceUrl(dataSourceUrl);
			company.setResourceIdentifier(dataSourceUrl.substring(28));

			companyList.add(company);
		}	
		return new Companies(companyList);
	}


	public Company getCompanyInfo(String organisationIdentifier) {
		String rdfStore= DEPLOY_ENV_AMAZAON ? "file:///var/lib/tomcat7/webapps/ROOT/rdf/notes.rdf" : "file:///C:/Users/thatchinamoorthyp/git/SemanticWikiSearch/src/main/webapp/rdf/notes.rdf";
		String sparqlQuery = 
				"SELECT ?name ?notes ?isPrimaryTopicOf ?abstract ?foundedBy ?foundingDate ?locationCity ?locationCountry ?keyPeople ?symbol ?revenue ?netIncome ?numEmployees" +
							" FROM <" + "http://dbpedia.org/resource/" + organisationIdentifier  + ">" +
							" FROM NAMED <" + rdfStore + ">" +
							" WHERE" + 
							" {" +
								" OPTIONAL{ <" + "http://dbpedia.org/resource/" + organisationIdentifier + "> ?property ?value.}" +
								" OPTIONAL{ <" + "http://dbpedia.org/resource/" + organisationIdentifier + "> <http://xmlns.com/foaf/0.1/name> ?name.}" +
								" OPTIONAL{<" + "http://dbpedia.org/resource/" + organisationIdentifier + "> <http://xmlns.com/foaf/0.1/isPrimaryTopicOf> ?isPrimaryTopicOf.}" + 
								" OPTIONAL{ <" + "http://dbpedia.org/resource/" + organisationIdentifier + "> <http://dbpedia.org/ontology/abstract> ?abstract.}" +
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
		
		LOGGER.log(Priority.INFO, "SPARQL Query -->" + sparqlQuery);
		QueryExecution queryExecution = QueryExecutionFactory.create(sparqlQuery);
		ResultSet resultSet = queryExecution.execSelect();
		LOGGER.log(Priority.INFO, resultSet.toString());
		
		Company company = new Company();
		
		company.setResourceIdentifier(organisationIdentifier);
		company.setDataSourceUrl("http://dbpedia.org/resource/" + organisationIdentifier);
		
		while(resultSet.hasNext()) {
			QuerySolution result = resultSet.next();
			company.setName(result.get("name") !=null ? result.get("name").toString() : "-");
			company.setIsPrimaryTopicOf(result.get("isPrimaryTopicOf") != null ? result.get("isPrimaryTopicOf").toString() : "-");
			company.setAabstract(result.get("abstract") != null ? result.get("abstract").toString() : "-");
			company.setFoundedBy(result.get("foundedBy") != null ? result.get("foundedBy").toString() : "-");
			company.setFoundingDate(result.get("foundingDate") != null ? result.get("foundingDate").toString() : "-");
			company.setLocationCity(result.get("locationCity") != null ? result.get("locationCity").toString() : "-");
			company.setLocationCountry(result.get("locationCountry") != null ? result.get("locationCountry").toString() : "-");
			company.setKeyPeople(result.get("keyPeople") != null ? result.get("keyPeople").toString() : "-");
			company.setSymbol(result.get("symbol") != null ? result.get("symbol").toString() : "-");
			company.setRevenue(result.get("revenue") != null ? result.get("revenue").toString() : "-");
			company.setNetIncome(result.get("netIncome") != null ? result.get("netIncome").toString() : "-") ;
			company.setNumEmployees(result.get("numEmployees") != null ? result.get("numEmployees").toString() : "-") ;
			company.setNotes(result.get("notes") != null 
									? (company.getNotes().contains(result.get("notes").toString())  
											? company.getNotes() 
											: company.getNotes() + "." + result.get("notes").toString()) 
									: "No Saved Notes for this company");			
		}	
		return company;
	}
	
}