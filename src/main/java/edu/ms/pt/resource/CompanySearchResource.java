package edu.ms.pt.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.ms.pt.model.Companies;

import edu.ms.pt.sparql.access.DBPediaSAO;

@Path("company/search/{company_keyword}")
public class CompanySearchResource extends Resource{
	
	private static final Logger LOGGER = Logger.getLogger(CompanySearchResource.class);

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response searchCompanyByKeyword(@PathParam("company_keyword") String companyKeyword) {
		Companies companies = new DBPediaSAO().searchCompanyByKeyword(companyKeyword, uriInfo);
		return Response.ok().entity(companies).build();
	}	
}