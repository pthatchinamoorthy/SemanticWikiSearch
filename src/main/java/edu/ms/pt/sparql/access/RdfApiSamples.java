package edu.ms.pt.sparql.access;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.SimpleSelector;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.VCARD;

public class RdfApiSamples {
	
	private Model model; 	public Model getModel() {return model;}

	public RdfApiSamples(Model model) {
		super();
		this.model = model;
	}
	
	public RdfApiSamples() {
		super();
		this.model = ModelFactory.createDefaultModel();
	}
	
	public Model createPT() {
		Resource nameResource = model.createResource()
								.addProperty(VCARD.Given, model.createLiteral("Prabhakaran", "en"))
								.addProperty(VCARD.Family, model.createLiteral("Thatchinamoorthy", "fr"));
		model.createResource("http://pt.com/PT").
								addProperty(VCARD.FN, "Prabhakaran Thatchinamoorthy").
								addProperty(VCARD.N, nameResource);	
		return model;
	}
	
	public void listResources (){
		SimpleSelector selector = new SimpleSelector(null, null, (RDFNode)null){
			public boolean selects(Statement s){
				return s.getObject().toString().endsWith("y");
			}
		};
		StmtIterator statements = model.listStatements(selector);
		while (statements.hasNext()){
			Statement statement = statements.next();
			Resource subject = statement.getSubject();
			Property predicate = statement.getPredicate();
			RDFNode object = statement.getObject();
			
			System.out.println("Subject: " + subject);
			System.out.println("Predicate: "+ predicate);
			System.out.println("Object: " + object.toString());
		}
		
	}
	
	public void writeRDF(){
		File notesFile = new File("C:\\Users\\thatchinamoorthyp\\git\\SemanticWikiSearch\\src\\main\\webapp\\rdf\\notes.rdf");
		
		try {
			model.write(new FileWriter(notesFile), "Turtle");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	/**
	 * @param args
	 */
	public static void main (String [] args){
		RdfApiSamples dbpedia = new RdfApiSamples();
		dbpedia.createPT();
		dbpedia.listResources();
		dbpedia.writeRDF();
	}
}