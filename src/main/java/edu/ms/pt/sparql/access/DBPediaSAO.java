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
import com.hp.hpl.jena.sparql.expr.E_Regex;
import com.hp.hpl.jena.sparql.expr.Expr;
import com.hp.hpl.jena.sparql.expr.ExprVar;
import com.hp.hpl.jena.sparql.expr.nodevalue.NodeValueString;
import com.hp.hpl.jena.sparql.syntax.ElementFilter;
import com.hp.hpl.jena.sparql.syntax.ElementGroup;
import com.hp.hpl.jena.sparql.syntax.ElementTriplesBlock;

import edu.ms.pt.resource.CompanySearchResource;

public class DBPediaSAO {
	private static final Logger LOGGER = Logger.getLogger(DBPediaSAO.class);
	private static final String DBPEDIA_ONT_NS = "http://dbpedia.org/ontology/";
	private static final String DBPEDIA_PROP_NS = "http://dbpedia.org/property/";
	private static final String FOAF_NS = "http://xmlns.com/foaf/0.1/";
	
	private static final int RECORD_LIMIT = 10;
	private static final boolean DEPLOY_ENV_AMAZAON = false;

	/**
	 * @param searchKeyword
	 * @param uriInfo 
	 */
	public Companies searchCompanyInfo(String searchKeyword, UriInfo uriInfo) {	
		
		
		ElementTriplesBlock tripleBlock = new ElementTriplesBlock();
		Triple nameTriple = Triple.create(Var.alloc("organization"), NodeFactory.createURI(FOAF_NS + "name") , Var.alloc("name"));
		tripleBlock.addTriple(nameTriple);				
		Triple locationCountryTriple = Triple.create(Var.alloc("organization"), NodeFactory.createURI(DBPEDIA_PROP_NS + "locationCountry") , Var.alloc("locationCountry"));
		tripleBlock.addTriple(locationCountryTriple);		
		Triple keyPeopleTriple = Triple.create(Var.alloc("organization"), NodeFactory.createURI(DBPEDIA_PROP_NS + "keyPeople") , Var.alloc("keyPeople"));
		tripleBlock.addTriple(keyPeopleTriple);
		
		Expr e = new E_Regex(new ExprVar("name"), new NodeValueString("^" + searchKeyword + "*"), new NodeValueString("i"));
		ElementFilter filter = new ElementFilter(e);
		
		ElementGroup queryBody = new ElementGroup();
		queryBody.addElement(tripleBlock);
		queryBody.addElementFilter(filter);
		
		Query query = QueryFactory.make();
		query.setQuerySelectType();
		query.addResultVar("organization");
		query.addResultVar("name");
		query.addResultVar("locationCountry");
		query.addResultVar("keyPeople");
		query.setQueryPattern(queryBody);
		query.setLimit(RECORD_LIMIT);
		
		QueryExecution queryExecution = QueryExecutionFactory.sparqlService("" + "http://dbpedia.org/sparql", query);
		ResultSet resultSet = queryExecution.execSelect();
		
		
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
			company.setDataSourceUrl(organizationIdentifier !=  null ? organizationIdentifier.toString() : null);
			
			if (DEPLOY_ENV_AMAZAON)
				company.setUrl(organizationIdentifier.toString().replace(
												"http://dbpedia.org/resource/", 
												"http://" + uriInfo.getRequestUri().getHost() + "/company/"));
			else
				company.setUrl(organizationIdentifier.toString().replace(
												"http://dbpedia.org/resource/", 
												"http://" + uriInfo.getRequestUri().getHost() + ":8080/SmartWikiSearch/company/"));
			companyList.add(company);
		}	
		return new Companies(companyList);
	}	
	
	public Company getCompanyInfo(String organisationIdentifier) {
		String organization = organisationIdentifier.contains("http") ? organisationIdentifier : "http://dbpedia.org/resource/" + organisationIdentifier;
		String sparqlQuery = 
				"SELECT ?name ?notes ?isPrimaryTopicOf ?abstract ?foundedBy ?foundingDate ?locationCity ?locationCountry ?keyPeople ?symbol ?revenue ?netIncome ?numEmployees" +
					" FROM <" + organization  + ">" +
							" FROM NAMED <file:///C:/Users/thatchinamoorthyp/git/SemanticWikiSearch/src/main/webapp/rdf/notes.rdf>" +
							" WHERE" + 
							" {" +
								" OPTIONAL{ <" + organization + "> ?property ?value.}" +
								" OPTIONAL{ <" + organization + "> <http://xmlns.com/foaf/0.1/name> ?name.}" +
								" OPTIONAL{<" + organization + "> <http://xmlns.com/foaf/0.1/isPrimaryTopicOf> ?isPrimaryTopicOf.}" + 
								" OPTIONAL{ <" + organization + "> <http://dbpedia.org/ontology/abstract> ?abstract.}" +
								" OPTIONAL{ <" + organization + "> <http://dbpedia.org/ontology/foundedBy> ?foundedBy.}" +
								" OPTIONAL{ <" + organization + "> <http://dbpedia.org/ontology/foundingDate> ?foundingDate.}" +
								" OPTIONAL{ <" + organization + "> <http://dbpedia.org/property/locationCity> ?locationCity.}" +
								" OPTIONAL{ <" + organization + "> <http://dbpedia.org/property/locationCountry> ?locationCountry.}" +
								" OPTIONAL{ <" + organization + "> <http://dbpedia.org/property/keyPeople> ?keyPeople.}" +
								" OPTIONAL{ <" + organization + "> <http://dbpedia.org/property/symbol> ?symbol.}" +
								" OPTIONAL{ <" + organization + "> <http://dbpedia.org/property/revenue> ?revenue.}" +
								" OPTIONAL{ <" + organization + "> <http://dbpedia.org/property/netIncome> ?netIncome.}" +
								" OPTIONAL{ <" + organization + "> <http://dbpedia.org/property/numEmployees> ?numEmployees.}" +
								" OPTIONAL {" +
									"GRAPH <file:///C:/Users/thatchinamoorthyp/git/SemanticWikiSearch/src/main/webapp/rdf/notes.rdf> " +
										" {<" + organization + "> <http://prabhakar.com/notes> ?notes.}" +
								"}" +
							"}" +
							" LIMIT 10";
		LOGGER.log(Priority.INFO, sparqlQuery);
		
		QueryExecution queryExecution = QueryExecutionFactory.create(sparqlQuery);
		ResultSet resultSet = queryExecution.execSelect();
		Company company = new Company();
		
		company.setDataSourceUrl("http://dbpedia.org/resource/" + organisationIdentifier);
		while(resultSet.hasNext()) {
			QuerySolution result = resultSet.next();
			company.setName(result.get("name") !=null ? result.get("name").toString() : "-");
			company.setIsPrimaryTopicOf(result.get("isPrimaryTopicOf") != null ? result.get("isPrimaryTopicOf").toString() : "-");
			company.setFoundedBy(result.get("foundedBy") != null ? result.get("foundedBy").toString() : "-");
			company.setFoundingDate(result.get("foundingDate") != null ? result.get("foundingDate").toString() : "-");
			company.setLocationCity(result.get("locationCity") != null ? result.get("locationCity").toString() : "-");
			company.setLocationCountry(result.get("locationCountry") != null ? result.get("locationCountry").toString() : "-");
			company.setKeyPeople(result.get("keyPeople") != null ? result.get("keyPeople").toString() : "-");
			company.setSymbol(result.get("symbol") != null ? result.get("symbol").toString() : "-");
			company.setRevenue(result.get("revenue") != null ? result.get("revenue").toString() : "-");
			company.setNetIncome(result.get("netIncome") != null ? result.get("netIncome").toString() : "-") ;
			company.setNotes(result.get("notes") != null ? company.getNotes() +". " + result.get("notes").toString() : "No Saved Notes for this company");			
		}	
		return company;
	}
	
	/*public Company getCompanyInfo(String organisationIdentifier, UriInfo uriInfo) {
		Triple triple = Triple.create(NodeFactory.createURI("http://dbpedia.org/resource/" + organisationIdentifier), Var.alloc("property"), Var.alloc("value"));
		Triple graphTriple = Triple.create(NodeFactory.createURI("http://dbpedia.org/resource/" + organisationIdentifier), NodeFactory.createURI("http://prabhakar.com/notes"), Var.alloc("notes"));
		
		ElementTriplesBlock tripleBlock = new ElementTriplesBlock();
		tripleBlock.addTriple(triple);
		
		
		ElementGroup queryBody = new ElementGroup();
		queryBody.addElement(tripleBlock);
		
		
		Query query = QueryFactory.make();
		query.setQuerySelectType();
		query.addResultVar("property");
		query.addResultVar("value");
		query.addGraphURI("http://dbpedia.org/resource/" + organisationIdentifier);
		query.addNamedGraphURI("file:///C:/Users/thatchinamoorthyp/git/SemanticWikiSearch/src/main/webapp/rdf/notes.rdf");
		query.setQueryPattern(queryBody);
		
		QueryExecution queryExecution = QueryExecutionFactory.create(query);
		ResultSet resultSet = queryExecution.execSelect();
		Company company = new Company();
		
		while(resultSet.hasNext()) {
			QuerySolution result = resultSet.next();
			RDFNode property = result.get("property");
			RDFNode value = result.get("value");
			RDFNode notes = result.get("notes");
			
			if (property.toString().equals(FOAF_NS + "name")) company.setName(value.toString());
			if (property.toString().equals(FOAF_NS + "isPrimaryTopicOf")) company.setIsPrimaryTopicOf(value.toString());
			if (property.toString().equals(DBPEDIA_ONT_NS + "abstract")) company.setAabstract(value.toString());
			if (property.toString().equals(DBPEDIA_PROP_NS + "locationCity")) company.setLocationCity(value.toString());
			if (property.toString().equals(DBPEDIA_PROP_NS + "locationCountry")) company.setLocationCountry(value.toString());
			if (property.toString().equals(DBPEDIA_PROP_NS + "keyPeople")) company.setKeyPeople(value.toString());
			if (property.toString().equals(DBPEDIA_PROP_NS + "symbol")) company.setSymbol(value.toString());
			if (property.toString().equals(DBPEDIA_PROP_NS + "revenue")) company.setRevenue(value.toString());
			if (property.toString().equals(DBPEDIA_PROP_NS + "netIncome")) company.setNetIncome(value.toString());
			
			if (property.toString().equals(DBPEDIA_ONT_NS + "foundedBy")) company.setFoundedBy(value.toString());
			if (property.toString().equals(DBPEDIA_ONT_NS + "foundingDate")) company.setFoundingDate(value.toString());
			if (property.toString().equals(DBPEDIA_PROP_NS + "numEmployees")) company.setNumEmployees(value.toString());
			
			company.setDataSourceUrl("http://dbpedia.org/resource/" + organisationIdentifier);
			company.setNotes(notes != null ? notes.toString() : "No Saved Notes for this company");
		}	
		return company;
	}*/
	
}