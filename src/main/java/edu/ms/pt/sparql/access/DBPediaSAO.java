package edu.ms.pt.sparql.access;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.UriInfo;

import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

import com.hp.hpl.jena.graph.NodeFactory;
import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.sparql.core.Var;
import com.hp.hpl.jena.sparql.expr.E_Equals;
import com.hp.hpl.jena.sparql.expr.E_GreaterThan;
import com.hp.hpl.jena.sparql.expr.E_LessThan;
import com.hp.hpl.jena.sparql.expr.E_Regex;
import com.hp.hpl.jena.sparql.expr.Expr;
import com.hp.hpl.jena.sparql.expr.ExprVar;
import com.hp.hpl.jena.sparql.expr.nodevalue.NodeValueDouble;
import com.hp.hpl.jena.sparql.expr.nodevalue.NodeValueInteger;
import com.hp.hpl.jena.sparql.expr.nodevalue.NodeValueString;
import com.hp.hpl.jena.sparql.syntax.ElementFilter;
import com.hp.hpl.jena.sparql.syntax.ElementGroup;
import com.hp.hpl.jena.sparql.syntax.ElementTriplesBlock;
import com.ms.pt.model.Companies;
import com.ms.pt.model.Company;

public class DBPediaSAO {

	public static final boolean DEPLOY_ENV_AMAZAON = false;
	
	private static final Logger LOGGER = Logger.getLogger(DBPediaSAO.class);
	private static final String DBPEDIA_ONT_NS = "http://dbpedia.org/ontology/";
	private static final String DBPEDIA_PROP_NS = "http://dbpedia.org/property/";
	private static final String FOAF_NS = "http://xmlns.com/foaf/0.1/";
	
	private static final int RECORD_LIMIT = 10;
	
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
							 "?organization <http://xmlns.com/foaf/0.1/name> ?name. " +
							 "?organization <http://dbpedia.org/property/keyPeople> ?keyPeople. " +
							 "?organization <http://dbpedia.org/property/locationCountry> ?locationCountry. ";
		
		if (name != null)
			sparqlQuery = sparqlQuery + " FILTER (str(?name)='" + name + "')";
		if (keyPeople != null)
			sparqlQuery =  sparqlQuery + " FILTER (?keyPeople=\"\")";
		if (locationCountry != null)
			sparqlQuery = sparqlQuery +  " FILTER (?locationCountry=\"\")";
		
		if (industry != null) 
			sparqlQuery = sparqlQuery + "?organization <http://dbpedia.org/ontology/industry> ?industry. FILTER (?industry=\"\")";
		if (symbol != null)
			sparqlQuery = sparqlQuery + "?organization <http://dbpedia.org/property/symbol> ?symbol. FILTER (?symbol=\"\")";
		if (greatThanNumEmployees != null)
			sparqlQuery = sparqlQuery + "?organization <http://dbpedia.org/property/numEmployees> ?numEmployees. FILTER (?numEmployees > \"\")";
		if (lesserThanNumEmployees != null)
			sparqlQuery = sparqlQuery + "?organization <http://dbpedia.org/property/numEmployees> ?numEmployees. FILTER (?numEmployees < \"\")";
		if (foundedBy != null)
			sparqlQuery = sparqlQuery + "?organization <http://dbpedia.org/ontology/foundedBy> ?foundedBy. FILTER (?foundedBy=\"\")";
		if (foundingDate != null)
			sparqlQuery = sparqlQuery + "?organization <http://dbpedia.org/ontology/foundingDate> ?foundingDate. FILTER (?foundingDate=\"\")";
		if (locationCity != null)
			sparqlQuery = sparqlQuery + "?organization <http://dbpedia.org/property/locationCity> ?locationCity. FILTER (?locationCity=\"\")";
		if (revenue != null)
			sparqlQuery = sparqlQuery + "?organization <http://dbpedia.org/property/revenue> ?revenue. FILTER (?revenue=\"\")";
		if (netIncome != null)
			sparqlQuery = sparqlQuery + "?organization <http://dbpedia.org/property/netIncome> ?netIncome. FILTER (?netIncome=\"\")";
		sparqlQuery = sparqlQuery + " }";
		sparqlQuery = sparqlQuery + " LIMIT 10";
		
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


	/**
	 * @param searchKeyword
	 * @param uriInfo 
	 */
	public Companies searchCompanyByKeyword(String searchKeyword, UriInfo uriInfo) {	
		
		
		String sparqlQuery = "SELECT ?organization ?name ?locationCountry ?keyPeople " +
							  "WHERE { " +
							  "?organization <http://xmlns.com/foaf/0.1/name> ?name. " +
							  "?organization <http://dbpedia.org/property/keyPeople> ?keyPeople. " +
							  "?organization <http://dbpedia.org/property/locationCountry> ?locationCountry. " +
							  "FILTER (regex(?name, '^" + searchKeyword + "*'" + ", 'i')) " +
							  "} " +
							  "LIMIT 10";
		
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
	
	
	private Query buildMultipleOptionQueryViaARQ(String name, String industry,
			String symbol, String keyPeople, String greatThanNumEmployees,
			String lesserThanNumEmployees, String foundedBy,
			String foundingDate, String locationCity, String locationCountry,
			String revenue, String netIncome) {
		ElementGroup queryBody = new ElementGroup();
		ElementTriplesBlock tripleBlock = new ElementTriplesBlock();
		queryBody.addElement(tripleBlock);
		Query query = QueryFactory.make();
		query.setQuerySelectType();
		query.setQueryPattern(queryBody);
		query.addResultVar("organization");
		query.setLimit(RECORD_LIMIT);
		
		query.addResultVar("name");
		tripleBlock.addTriple(Triple.create(Var.alloc("organization"), NodeFactory.createURI(FOAF_NS + "name") , Var.alloc("name")));
		if(name!=null && !name.isEmpty())	{
			ElementFilter nameFilter = new ElementFilter(new E_Equals(new ExprVar("name"), new NodeValueString(name)));
			queryBody.addElementFilter(nameFilter);
		}
		
		if (industry!=null && !industry.isEmpty()) {
			tripleBlock.addTriple(Triple.create(Var.alloc("organization"), NodeFactory.createURI(DBPEDIA_ONT_NS + "industry") , Var.alloc("industry")));				
			ElementFilter industryFilter = new ElementFilter(new E_Equals(new ExprVar("industry"), new NodeValueString(industry)));
			queryBody.addElement(industryFilter);
		}
		
		if (symbol!=null && !symbol.isEmpty()) {
			tripleBlock.addTriple(Triple.create(Var.alloc("organization"), NodeFactory.createURI(DBPEDIA_PROP_NS + "symbol") , Var.alloc("symbol")));
			ElementFilter symbolFilter = new ElementFilter(new E_Equals(new ExprVar("symbol"), new NodeValueString(symbol)));
			queryBody.addElement(symbolFilter);
		}
		
		query.addResultVar("keyPeople");
		tripleBlock.addTriple(Triple.create(Var.alloc("organization"), NodeFactory.createURI(DBPEDIA_PROP_NS + "keyPeople") , Var.alloc("keyPeople")));
		if (keyPeople != null && !keyPeople.isEmpty()) {
			ElementFilter keyPeopleFilter = new ElementFilter(new E_Equals(new ExprVar("keyPeople"), new NodeValueString(keyPeople)));
			queryBody.addElement(keyPeopleFilter);
		}
		
		if (greatThanNumEmployees != null && !greatThanNumEmployees.isEmpty()) {
			tripleBlock.addTriple(Triple.create(Var.alloc("organization"), NodeFactory.createURI(DBPEDIA_ONT_NS + "numberOfEmployees") , Var.alloc("numberOfEmployees")));				
			ElementFilter greatThanNumEmployeesFilter = new ElementFilter(new E_GreaterThan(new ExprVar("numberOfEmployees"), new NodeValueInteger(Long.parseLong(greatThanNumEmployees))));
			queryBody.addElementFilter(greatThanNumEmployeesFilter);
		}
		
		if (lesserThanNumEmployees != null && !lesserThanNumEmployees.isEmpty()) {
			tripleBlock.addTriple(Triple.create(Var.alloc("organization"), NodeFactory.createURI(DBPEDIA_ONT_NS + "numberOfEmployees") , Var.alloc("numberOfEmployees")));				
			ElementFilter lesserThanNumEmployeesFilter = new ElementFilter(new E_LessThan(new ExprVar("numberOfEmployees"), new NodeValueInteger(Long.parseLong(lesserThanNumEmployees))));
			queryBody.addElementFilter(lesserThanNumEmployeesFilter);
		}
		
		if (foundedBy !=null && !foundedBy.isEmpty()) {			
			tripleBlock.addTriple(Triple.create(Var.alloc("organization"), NodeFactory.createURI(DBPEDIA_ONT_NS + "foundedBy") , Var.alloc("foundedBy")));				
			ElementFilter foundedByFilter = new ElementFilter(new E_Equals(new ExprVar("foundedBy"), new NodeValueString(foundedBy)));
			queryBody.addElementFilter(foundedByFilter);	
		}
		
		if (foundingDate != null && !foundingDate.isEmpty()) {
			query.addResultVar("foundingDate");
			tripleBlock.addTriple(Triple.create(Var.alloc("organization"), NodeFactory.createURI(DBPEDIA_ONT_NS + "foundingDate") , Var.alloc("foundingDate")));				
			ElementFilter foundingDateFilter = new ElementFilter(new E_Equals(new ExprVar("foundingDate"), new NodeValueString(foundingDate)));
			queryBody.addElementFilter(foundingDateFilter);
		}
		
		if (locationCity != null && locationCity.isEmpty()) {
			query.addResultVar("locationCity");
			tripleBlock.addTriple(Triple.create(Var.alloc("organization"), NodeFactory.createURI(DBPEDIA_PROP_NS + "locationCity") , Var.alloc("locationCity")));	
			ElementFilter locationCityFilter = new ElementFilter(new E_Equals(new ExprVar("locationCity"), new NodeValueString(locationCity)));
			queryBody.addElement(locationCityFilter);
		}
		
		query.addResultVar("locationCountry");
		tripleBlock.addTriple(Triple.create(Var.alloc("organization"), NodeFactory.createURI(DBPEDIA_PROP_NS + "locationCountry") , Var.alloc("locationCountry")));
		if (locationCountry != null && !locationCountry.isEmpty()) {
			ElementFilter locationCountryFilter = new ElementFilter(new E_Equals(new ExprVar("locationCountry"), new NodeValueString(locationCountry)));
			queryBody.addElement(locationCountryFilter);
		}
		
		if (revenue !=null && !revenue.isEmpty()) {
			query.addResultVar("revenue");
			tripleBlock.addTriple(Triple.create(Var.alloc("organization"), NodeFactory.createURI(DBPEDIA_PROP_NS + "revenue") , Var.alloc("revenue")));	
			ElementFilter revenueFilter = new ElementFilter(new E_GreaterThan(new ExprVar("revenue"), new NodeValueDouble(Double.parseDouble(revenue))));
			queryBody.addElement(revenueFilter);
		}
		
		if (netIncome!= null && !netIncome.isEmpty()) {
			query.addResultVar("netIncome");
			tripleBlock.addTriple(Triple.create(Var.alloc("organization"), NodeFactory.createURI(DBPEDIA_PROP_NS + "netIncome") , Var.alloc("netIncome")));	
			ElementFilter netIncomeFilter = new ElementFilter(new E_GreaterThan(new ExprVar("netIncome"), new NodeValueDouble(Double.parseDouble(netIncome))));
			queryBody.addElement(netIncomeFilter);
		}
		return query;
	}
	
}