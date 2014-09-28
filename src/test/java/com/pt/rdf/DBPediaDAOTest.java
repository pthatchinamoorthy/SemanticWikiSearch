package com.pt.rdf;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.vocabulary.VCARD;


public class DBPediaDAOTest {
	
	DBPediaDAO dbPedia;
	public static final String personURI = "http://pt.com/PT";
	public static final String FULL_NAME = "Prabhakaran Thatchinamoorthy";
	
	@Before
	public void setup(){
		dbPedia = new DBPediaDAO();
	}
	
	
	@Test
	public void testModel(){
		Model model = dbPedia.createModel();
		Assert.assertEquals(FULL_NAME, model.getResource(personURI).getProperty(VCARD.FN).getLiteral().getValue());
	}

}