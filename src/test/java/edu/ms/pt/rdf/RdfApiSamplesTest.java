package edu.ms.pt.rdf;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.VCARD;

import edu.ms.pt.sparql.access.RdfApiSamples;


public class RdfApiSamplesTest {
	
	RdfApiSamples dbPedia;
	public static final String personURI = "http://pt.com/PT";
	public static final String FULL_NAME = "Prabhakaran Thatchinamoorthy";
	
	@Before
	public void setup(){
		dbPedia = new RdfApiSamples();
	}
	
	
	@Test
	public void test_createPT(){
		dbPedia.createPT();
		Resource ptResource = dbPedia.getModel().getResource(personURI);
		Assert.assertEquals(FULL_NAME, ptResource.getProperty(VCARD.FN).getLiteral().getValue());
		Assert.assertEquals(personURI, ptResource.getURI());
	}
	
	@Test
	public void test_listResources(){
		StmtIterator statements = dbPedia.getModel().listStatements();
		while (statements.hasNext()){
			Statement statement = statements.next();
			Resource subject = statement.getSubject();
			Property predicate = statement.getPredicate();
			RDFNode object = statement.getObject();
			
			System.out.println("Subject: " + subject);
			System.out.println("Predicate: "+ predicate);
			System.out.println("Object: " + object);
		}
	}
	
}
