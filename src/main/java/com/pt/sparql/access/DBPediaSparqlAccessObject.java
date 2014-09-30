package com.pt.sparql.access;

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

public class DBPediaSparqlAccessObject {

	/**
	 * @param companyName
	 */
	public void searchCompanyInfo(String companyName) {		
		Triple triple = Triple.create(Var.alloc("organization"), NodeFactory.createURI("http://xmlns.com/foaf/0.1/name") , Var.alloc("name"));
		Expr e = new E_Regex(new ExprVar("name"), new NodeValueString("^" + companyName + "*"), new NodeValueString("i"));
		
		ElementTriplesBlock tripleBlock = new ElementTriplesBlock();
		tripleBlock.addTriple(triple);
		ElementFilter filter = new ElementFilter(e);
		
		ElementGroup queryBody = new ElementGroup();
		queryBody.addElement(tripleBlock);
		queryBody.addElementFilter(filter);
		
		
		Query query = QueryFactory.make();
		query.setQuerySelectType();
		query.addResultVar("organization");
		query.addResultVar("name");
		query.setQueryPattern(queryBody);
		query.setLimit(5);
		
		QueryExecution queryExecution = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", query);
		ResultSet resultSet = queryExecution.execSelect();
		while(resultSet.hasNext()) {
			QuerySolution result = resultSet.next();
			RDFNode organization = result.get("organization");
			RDFNode name = result.get("name");
			
			System.out.println("---------> " + organization.toString() + "----------------->" + name);
			getCompanyInfo(organization.toString());
			
		}	
		
	}	
	
	public void getCompanyInfo(String companyURL) {		
		Triple triple = Triple.create(NodeFactory.createURI("http://dbpedia.org/resource/Microsoft_Live_Labs_Listas"), Var.alloc("property"), Var.alloc("value"));
		
		ElementTriplesBlock tripleBlock = new ElementTriplesBlock();
		tripleBlock.addTriple(triple);
		
		ElementGroup queryBody = new ElementGroup();
		queryBody.addElement(tripleBlock);
		
		Query query = QueryFactory.make();
		query.setQuerySelectType();
		query.addResultVar("property");
		query.addResultVar("value");
		query.setQueryPattern(queryBody);
		query.setLimit(5);
		
		QueryExecution queryExecution = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", query);
		ResultSet resultSet = queryExecution.execSelect();
		while(resultSet.hasNext()) {
			QuerySolution result = resultSet.next();
			RDFNode property = result.get("property");
			RDFNode value = result.get("value");
			System.out.println("========> " + property + "===========>" + value);
		}	
		
	}	
	
	public static void main(String[] args) {
		new DBPediaSparqlAccessObject().searchCompanyInfo("microsoft");
	}

}