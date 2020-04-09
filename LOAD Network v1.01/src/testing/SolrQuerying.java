package testing;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.request.QueryRequest;


public class SolrQuerying {
	
	public static void main(String[] args) {
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