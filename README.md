# <em>AdvancedWikiSearch - A Semantic Web Application</em>

# <em>URL - </em>
<a href="http://prabhakarwikisearch.elasticbeanstalk.com">http://prabhakarwikisearch.elasticbeanstalk.com</a>

# <em>Problem 1: Data Integration</em>
  <ol>
    <li>Problem
        <ol>
          <li>Data is stored in different locations</li>
          <li>Different Containers - Database, Local files, Local Data Stores, Web Services</li>
        </ol>
    </li>
    <li>Solution
      <ol>RDF Data Model - Store the Data in RDF Data Model, irrespective of whether the container is a file or data store or webservice</ol>
      <ol>SPARQL - Query language that queries and integrates multiple data stores in fly</ol>
    </li>
  </ol>
  
# <em>Problem 2: Data Extraction</em>
  <ol>
    <li>Problem
        <ol>
          <li>Data is stored in millions of HTML web page, but there is no proper mechanishm to extract the data correctly and easily</li>
          <li>Search Engines renders in-accurate results</li>
        </ol>
    </li>
    <li>Solution
      <ol>RDFa Tags - Embed RDFa Tags to the HTML webpage</ol>
    </li>
  </ol>
  

# <em>What is it</em>
This is a Semantic Web Application that allows you to search for any Company's profile. 
The Use Cases are
  <ol>
    <li>Enable Company Profile search by using 2 ways
        <ol>
          <li>Simple Keyword search</li>
          <li>Advanced Options Search</li>
        </ol>
    </li>
    <li>Add Personalized Notes for the Company to local RDF stores</li>
    <li>Embed RDFa Tags to Company Profile Page</li>
  </ol>

# <em>What is it made of</em>
  The building blocks of this applications are
    <ol>
      <li>Back End Layer
            <ol>
                <li>External Company Data Store - DBPedia <a href="http://dbpedia.org/sparql/">http://dbpedia.org/sparql/</a></li>
                <li>Internal Company RDF Data Store<a href="prabhakarwikisearch.elasticbeanstalk.com/rdf/companies.rdf">prabhakarwikisearch.elasticbeanstalk.com/rdf/companies.rdf</a></li>
                <li>Internal Notes RDF Store <a href="prabhakarwikisearch.elasticbeanstalk.com/rdf/notes.rdf">prabhakarwikisearch.elasticbeanstalk.com/rdf/notes.rdf</a></li>
                <li>SPARQL Framework - Apache Jena's ARQ library to build, fire and aggregate SPARQL queries</li>
                <li>REST Web Service Framework - JAX-RS Jersey http://prabhakarwikisearch.elasticbeanstalk.com/rest/application.wadl</li>
            </ol>
      </li>
      <li>Front End
          <ol>
            <li>Java Script MVC Framework : AngularJS </li>
            <li>RDFa Tags - <a href="http://schema.org/">http://schema.org/</a> ontology to markup the web pages</li>
            <li>HTML/CSS</li>
          </ol>
      </li>
    </ol>
    
# <em>Component Diagram</em>
  The following figure depicts the technical components and the activity flow for all the 3 Use Cases
  <img src="https://s3-us-west-2.amazonaws.com/semanticwikisearch/images/ComponentDiagram.png"/>
  
# <em>How to build and deploy locally in machine</em>
  <ol>
  	<li>Install Java, Gradle</li>
  	<li>Clone the source code GIT URL https://github.com/prabhakar1983/SemanticWikiSearch.git</li>
  	<li>Run the build command “gradle”. The gradle script has a default task – “jettyRun”, which will build the war file “SemanticWikiSearch.war” and deploy it to the local Jetty web server</li> 
  	<li>Hit the Application URL: http://prabhakarwikisearch.elasticbeanstalk.com/ and start searching</li>
  </ol>
  
# <em>How to build and deploy in Amazon Elastic Bean Stalk</em>
  <ol>
  	<li>Install Java, Gradle</li>
  	<li>Clone the source code GIT URL https://github.com/prabhakar1983/SemanticWikiSearch.git</li>
  	<li>Run the build command “gradle build”, which will generate the Web Deployable - “./lib/SemanticWikiSearch.war”</li> 
  	<li>Create a Tomcat running instance in Amazon’s PAAS environment – Amazon Elastic Beanstalk. http://aws.amazon.com/elasticbeanstalk/ , deploy the “SemanticWikiSearch.war” to it. 
The configuration details for this application is
 	Operating System: 64bit Amazon Linux 2014.03 v1.0.7
Web Server: Tomcat 7 Java 7
</li>
  	<li>Hit the Application URL: http://prabhakarwikisearch.elasticbeanstalk.com/ and start searching</li>
  </ol>

# <em>Sample SPARQL Query</em>
  <ol>Search Companies</ol>
  
  SELECT 
    ?organization (STR(?name) as ?nameVar) (STR(?industry) as ?industryVar) 
    (STR(?locationCountry) as ?locationCountryVar) (STR(?keyPeople) as ?keyPeopleVar) 
  WHERE 
  {  
    ?organization <http://xmlns.com/foaf/0.1/name> ?name. 
    FILTER (regex(?name, '^dell Inc*', 'i')) 
    ?organization <http://dbpedia.org/ontology/industry> ?industry.  
    ?organization <http://dbpedia.org/property/keyPeople> ?keyPeople.  
    ?organization <http://dbpedia.org/property/locationCountry> ?locationCountry.  
  } 
  LIMIT 20
  
  <ol>Retrive Company Information</ol>
  
SELECT 
    (STR(?name) as ?nameVar) (STR(?notes) as ?notesVar) (STR(?isPrimaryTopicOf) as ?isPrimaryTopicOfVar) 
    (STR(?abstract) as ?abstractVar) (STR(?foundedBy) as ?foundedByVar) 
    (STR(?foundingDate) as ?foundingDateVar) (STR(?locationCity) as ?locationCityVar) 
    (STR(?locationCountry) as ?locationCountryVar) (STR(?keyPeople) as ?keyPeopleVar) 
    (STR(?symbol) as ?symbolVar) (STR(?revenue) as ?revenueVar) (STR(?netIncome) as ?netIncomeVar) 
    (STR(?numEmployees) as ?numEmployeesVar) 
FROM 
    <http://dbpedia.org/resource/Dell> 
FROM 
    <file:///var/lib/tomcat7/webapps/ROOT/rdf/companies.rdf> 
FROM NAMED 
    <file:///var/lib/tomcat7/webapps/ROOT/rdf/notes.rdf> 
WHERE 
{ 
  OPTIONAL{ <http://dbpedia.org/resource/Dell> ?property ?value.} 
  OPTIONAL{ <http://dbpedia.org/resource/Dell> <http://xmlns.com/foaf/0.1/name> ?name.}
  OPTIONAL{<http://dbpedia.org/resource/Dell> <http://xmlns.com/foaf/0.1/isPrimaryTopicOf> ?isPrimaryTopicOf.} 
  OPTIONAL{ <http://dbpedia.org/resource/Dell> <http://dbpedia.org/ontology/abstract> ?abstract. 
              FILTER langMatches( lang(?abstract), "en" )} 
  OPTIONAL{ <http://dbpedia.org/resource/Dell> <http://dbpedia.org/ontology/foundedBy> ?foundedBy.} 
  OPTIONAL{ <http://dbpedia.org/resource/Dell> <http://dbpedia.org/ontology/foundingDate> ?foundingDate.} 
  OPTIONAL{ <http://dbpedia.org/resource/Dell> <http://dbpedia.org/property/locationCity> ?locationCity.}
  OPTIONAL{ <http://dbpedia.org/resource/Dell> <http://dbpedia.org/property/locationCountry> ?locationCountry.} 
  OPTIONAL{ <http://dbpedia.org/resource/Dell> <http://dbpedia.org/property/keyPeople> ?keyPeople.} 
  OPTIONAL{ <http://dbpedia.org/resource/Dell> <http://dbpedia.org/property/symbol> ?symbol.} 
  OPTIONAL{ <http://dbpedia.org/resource/Dell> <http://dbpedia.org/property/revenue> ?revenue.} 
  OPTIONAL{ <http://dbpedia.org/resource/Dell> <http://dbpedia.org/property/netIncome> ?netIncome.} 
  OPTIONAL{ <http://dbpedia.org/resource/Dell> <http://dbpedia.org/property/numEmployees> ?numEmployees.}
  OPTIONAL {
        GRAPH <file:///var/lib/tomcat7/webapps/ROOT/rdf/notes.rdf>  {
            <http://dbpedia.org/resource/Dell> <http://prabhakar.com/notes> ?notes.
        }
  }
} LIMIT 5
            
