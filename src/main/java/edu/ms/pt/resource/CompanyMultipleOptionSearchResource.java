package edu.ms.pt.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

import com.ms.pt.model.Companies;

import edu.ms.pt.sparql.access.DBPediaSAO;

@Path("search/mutilple-option-search")
public class CompanyMultipleOptionSearchResource extends Resource{
	
	private static final Logger LOGGER = Logger.getLogger(CompanyMultipleOptionSearchResource.class);

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response searchCompanyMultipleOption(
			@QueryParam("companyName") String name, @QueryParam("industryName") String industry,
			@QueryParam("stockSymbol") String symbol, @QueryParam("keyPeople") String keyPeople, 
			@QueryParam("greaterThanEmployeeSize") String greatThanNumEmployees, @QueryParam("lesserThanEmployeeSize") String lesserThanNumEmployees,  
			@QueryParam("foundedBy") String foundedBy, @QueryParam("foundingDate") String foundingDate, 
			@QueryParam("city") String locationCity, @QueryParam("country") String locationCountry, 
			@QueryParam("revenue") String revenue, @QueryParam("income") String netIncome) {
		
		LOGGER.log(Priority.INFO, "multiple option search -------->");
		Companies companies = new DBPediaSAO().searchCompanyByOption( name,  industry,
				   symbol,  keyPeople, 
				    greatThanNumEmployees,  lesserThanNumEmployees,  
				    foundedBy,  foundingDate, 
				    locationCity,  locationCountry, 
				    revenue,  netIncome,
				   uriInfo);
		return Response.ok().entity(companies).build();
	}
}