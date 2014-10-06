package edu.ms.pt.resource;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

public class Resource {
	
	@Context
	UriInfo uriInfo;
	
	protected Map<String,String> getContext() {
		Map<String,String> context = new HashMap<String,String>();
		context = new HashMap<String,String>();
		context.put("foaf", "http://xmlns.com/foaf/0.1/");
		context.put("dbpedia-owl", "http://dbpedia.org/ontology/");
		return context;
	}

}
