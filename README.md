# <em>AdvancedWikiSearch - A Semantic Web Application</em>

# <em>URL - </em>
<a href="http://prabhakarwikisearch.elasticbeanstalk.com">http://prabhakarwikisearch.elasticbeanstalk.com</a>

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
                <li>REST Web Service Framework - JAX-RS Jersey </li>
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
