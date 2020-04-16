package testing;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.io.FileInputStream;


import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.request.QueryRequest;


public class SolrQuerying {
	
	
	public static void main(String[] args) {
		Properties prop=new Properties();
		String propFilePath = "resources/config.properties";
		
		FileInputStream inputStream = new FileInputStream(propFilePath);
		 
		if (inputStream != null) {
			prop.load(inputStream);
		} else {
			throw new FileNotFoundException("property file '" + propFilePath + "' not found in the classpath");
		}
		
		String solrURL = "https://solrdev.dhlab.epfl.ch/solr/impresso_dev";
		HttpSolrClient client = new HttpSolrClient.Builder(solrURL).build();
		SolrQuery solrQuery = new SolrQuery();
		solrQuery.setQuery("*:*");
		solrQuery.set("fl","*");
		solrQuery.setRows(10);
		
		try {
		    QueryRequest queryRequest = new QueryRequest(solrQuery);
		    queryRequest.setBasicAuthCredentials("guest_reader","password");
		    QueryResponse solrResponse = queryRequest.process(client);
		    System.out.println(solrResponse);
		    System.out.println("Total Documents : "+solrResponse.getResults().getNumFound());
		} catch (SolrServerException e) {
		    e.printStackTrace();
		} catch (IOException e) {
		    e.printStackTrace();
		}
		
	}
}