# <em>AdvancedWikiSearch - A Semantic Web Application</em>

# <em>What is it</em>
This is a Semantic Web Application that allows you to search for any Company's profile. 
The Use Cases are
  <ol>
    <li>Enable Company Profile search by using 2 ways
        <ol>
          <li>Keyword search</li>
          <li>Multiple Options Search</li>
        </ol>
    </li>
    <li>Publish the Company Profile with Structured data by applying Semantic Markups</li>
    <li>Enable Client to extract/download the published Structured data</li>
  </ol>

# <em>What is it made of</em>
  The building blocks of this applications are
    <ol>
      <li>Data Access Layer
            <ol>
                <li>SPARQL to query <a href="http://dbpedia.org/sparql/">http://dbpedia.org/sparql/</a></li>
                <li>Apache Jena's ARQ library to build, fire and aggregate SPARQL queries</li>
            </ol>
      </li>
      <li>Presentation Layer
          <ol>
            <li>Spring Web MVC with ThemeLeaf templates</li>
            <li><a href="http://schema.org/">http://schema.org/</a> ontology to markup the web pages</li>
            <li>RDFa serialization format</li>
          </ol>
      </li>
      <li>RDFa Distiller (Yet to decided) to extract Structed data from the Web Page.</li>
    </ol>
    
# <em>Component Diagram</em>
  The following figure depicts the technical components and the activity flow for all the 3 Use Cases
  <img src="https://s3-us-west-2.amazonaws.com/semanticwikisearch/images/ComponentDiagram.png"/>
  
# <em>How to build</em>
  <ol>
  	<li>Install Java, Gradle</li>
  	<li>Run the command "gradle" which will call the default task "jettyRun"</li>
  	<li>http://localhost:8080/SmartWikiSearch/company/search/{company_keyword}</li> 
  	<li>Add the Chrome Browser extension - "JSON Formatter" to view the JSON clearly in the browser</>
  </ol>