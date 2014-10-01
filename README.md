# <b><em>AdvancedWikiSearch - A Semantic Web Application</em></b>

# <em>What it does</em>
This is a Semantic Web Application that allows you to search for any Company's profile. The detailed Use Cases are
  <ol>
    <li>Enable Company Profile search by using 2 ways
           1. Keyword search
           2. Multiple Options Search 
           </li>
    <li>Publish the Company Profile with Structured data by applying Semantic Markups</li>
    <li>Enable Client to extract/download the published Structured data</li>
  </ol>

# <em>What it is made of</em>
  <ol>
      <li>Data Access Layer
      a. SPARQL to query <a href="http://dbpedia.org/sparql">http://dbpedia.org/sparql</a>
      b. Apache Jena's ARQ library to build, fire and aggregate SPARQL queries</li>
      <li>Presentation Layer
      a. Spring Web MVC with ThemeLeaf templates
      b. <a href="http://schema.org/">http://schema.org/</a> ontology to markup the web pages
      c. RDFa serialization format
      </li>
  <li>RDFa Distiller (Yet to decided) to extract Structed data from the Web Page. </li>
  </ol>

  
      
  
