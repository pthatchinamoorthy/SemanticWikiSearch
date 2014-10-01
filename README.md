# <b>AdvancedWikiSearch - A Semantic Web Application</b>

# <em>What it does</em>
This is a Semantic Web Application that allows you to search for any Company's profile. The detailed Use Cases are
  1. Enable Company Profile search by using 2 ways
    a. Keyword search
    b. Multiple Options Search 
  2. Publish the Company Profile with Structured data by applying Semantic Markups
  3. Enable Client to extract/download the published Structured data

# <em>What it is made of</em>
  1. Data Access Layer
      a. SPARQL to query <a href="http://dbpedia.org/sparql"/>
      b. Apache Jena's ARQ library to build, fire and aggregate SPARQL queries
  2. Presentation Layer
      a. Spring Web MVC with ThemeLeaf templates
      b. <a href="http://schema.org/"/> ontology to markup the web pages
      c. RDFa serialization format
  3. RDFa Distiller (Yet to decided) to extract Structed data from the Web Page. 

  
      
  
