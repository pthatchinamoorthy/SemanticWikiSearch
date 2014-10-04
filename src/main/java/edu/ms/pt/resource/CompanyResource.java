package edu.ms.pt.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import edu.ms.pt.sparql.access.Company;
import edu.ms.pt.sparql.access.DBPediaSAO;

@Path("/{name}")
public class CompanyResource {

	private static final Logger LOGGER = Logger.getLogger(CompanyResource.class);
	private DBPediaSAO dbPediaSAO;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCompanyInfo(@PathParam("name") String name) {
		LOGGER.info("The control is now CompanySearch Servlet");
		
		dbPediaSAO = new DBPediaSAO();
		Company company = dbPediaSAO.getCompanyInfo(name);
		LOGGER.info(company.toString());
		
		return Response.ok().entity(company).build();
	}
}
