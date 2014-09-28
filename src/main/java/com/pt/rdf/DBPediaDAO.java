package com.pt.rdf;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.VCARD;

public class DBPediaDAO {

	public Model createModel() {
		String personURI = "http://pt.com/PT";
		String fullName = "Prabhakaran Thatchinamoorthy";

		Model model = ModelFactory.createDefaultModel();
		Resource pt = model.createResource(personURI);
		pt.addLiteral(VCARD.FN, fullName);

		return model;
	}

}