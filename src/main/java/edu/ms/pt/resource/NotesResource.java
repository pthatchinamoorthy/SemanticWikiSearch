package edu.ms.pt.resource;

import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;


import edu.ms.pt.model.Company;
import edu.ms.pt.sparql.access.DBPediaSAO;
import edu.ms.pt.sparql.access.NotesWriter;

@Path("company/notes/{organizationIdenfier}")
public class NotesResource extends Resource{
	
	private static final Logger LOGGER = Logger.getLogger(NotesResource.class);

	@PUT
	public Response updateCompanyInfo(@PathParam("organizationIdenfier") String organizationIdenfier, 
									@QueryParam("notes") String notes) {
		NotesWriter notesWriter = new NotesWriter();
		notesWriter.createNotes(organizationIdenfier, notes);
		
		Company updatedCompany = new DBPediaSAO().getCompanyInfo(organizationIdenfier);
		return Response.ok().entity(updatedCompany).build();
	}
	
}