package edu.ms.pt.sparql.access;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.UriInfo;

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

public class DBPediaSAO {

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
			RDFNode organizationObject = result.get("organization");
			RDFNode name = result.get("name");
			RDFNode country = result.get("locationCountry");
			RDFNode keyPeople = result.get("keyPeople");
			
			Company company = new Company();
			company.setName(name.toString());
			company.setLocationCountry(country != null ? country.toString() : "-");
			company.setKeyPeople(keyPeople != null ? keyPeople.toString() : "-");
			
			if (DEPLOY_ENV_AMAZAON)
				company.setUrl(organizationObject.toString().replace(
												"http://dbpedia.org/resource/", 
												"http://" + uriInfo.getRequestUri().getHost() + "/company/"));
			else
				company.setUrl(organizationObject.toString().replace(
												"http://dbpedia.org/resource/", 
												"http://" + uriInfo.getRequestUri().getHost() + ":8080/SmartWikiSearch/company/"));
			companyList.add(company);
		}	
		return new Companies(companyList);
	}	
	
	public Company getCompanyInfo(String companyName, UriInfo uriInfo) {
		Triple triple = Triple.create(NodeFactory.createURI("http://dbpedia.org/resource/" + companyName), Var.alloc("property"), Var.alloc("value"));
		
		ElementTriplesBlock tripleBlock = new ElementTriplesBlock();
		tripleBlock.addTriple(triple);
		
		ElementGroup queryBody = new ElementGroup();
		queryBody.addElement(tripleBlock);
		
		Query query = QueryFactory.make();
		query.setQuerySelectType();
		query.addResultVar("property");
		query.addResultVar("value");
		query.setQueryPattern(queryBody);
		//query.setLimit(5);
		
		QueryExecution queryExecution = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", query);
		ResultSet resultSet = queryExecution.execSelect();
		Company company = new Company();
		
		while(resultSet.hasNext()) {
			QuerySolution result = resultSet.next();
			RDFNode property = result.get("property");
			RDFNode value = result.get("value");
			
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
		}	
		return company;
	}
	
}