package edu.ms.pt.sparql.access;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class NotesWriter {
	
	private Model model; 	public Model getModel() {return model;}
	
	private static final boolean DEPLOY_ENV_AMAZAON = false;

	public NotesWriter(Model model) {
		super();
		this.model = model;
	}
	
	public NotesWriter() {
		super();
		this.model = ModelFactory.createDefaultModel();
		
		Path path =  null;
		if (DEPLOY_ENV_AMAZAON)
			path = Paths.get("webapps/ROOT/rdf", "notes.rdf"); 
		else
			path = Paths.get("src\\main\\webapp\\rdf", "notes.rdf");
		
		this.model.read(path.toUri().toString(), null, "RDF/XML");
	}
	
	public void createNotes(String organizationIdentifier, String notes) {
		Model newModel = ModelFactory.createDefaultModel();
		newModel.createResource("http://dbpedia.org/resource/" + organizationIdentifier).addProperty(model.createProperty("http://prabhakar.com/" , "notes"), model.createLiteral(notes));
		try {
			this.model.add(newModel);
			if(DEPLOY_ENV_AMAZAON)
				this.model.write(new FileWriter("webapps/ROOT/rdf/notes.rdf"), "RDF/XML");
			else
				this.model.write(new FileWriter("src\\main\\webapp\\rdf\\notes.rdf"), "RDF/XML");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	public static void main (String [] args) {
		new NotesWriter().createNotes("mjjjjjjjjjjoron", "-++++++++++++------");
		
	}
	
}