package construction;

import java.io.IOException;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;
import java.io.FileInputStream;


import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.request.QueryRequest;
import org.apache.solr.common.SolrDocumentList;


public class SolrReader {

	public SolrReader() {
		
	}
	
	public Set<String> getContentItemIDs(String newspaperID) {
		Properties prop=new Properties();
		String propFilePath = "../resources/config.properties";
		
		FileInputStream inputStream;
		try {
			inputStream = new FileInputStream(propFilePath);
			prop.load(inputStream);
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		 
		HttpSolrClient client = new HttpSolrClient.Builder(prop.getProperty("solrDBName")).build();
		SolrQuery solrQuery = new SolrQuery();
		solrQuery.setQuery("meta_journal_s:"+newspaperID);
		solrQuery.set("fl","id");
		solrQuery.setRows(100);
		
		Set<String> solrIds = null;
		try {
		    QueryRequest queryRequest = new QueryRequest(solrQuery);
		    queryRequest.setBasicAuthCredentials(prop.getProperty("solrUserName"),System.getenv("solrPassword"));
		    QueryResponse solrResponse = queryRequest.process(client);
		    solrIds = solrResponse.getResults()
		    		  .stream().map(x -> (String) x.get("id"))
		    		  .collect(Collectors.toSet());
		} catch (SolrServerException e) {
		    e.printStackTrace();
		} catch (IOException e) {
		    e.printStackTrace();
		}
		
		return solrIds;

	}
	
	public ImpressoContentItem getContentItem(String solrId) {
		Properties prop=new Properties();
		String propFilePath = "../resources/config.properties";
		
		FileInputStream inputStream;
		try {
			inputStream = new FileInputStream(propFilePath);
			prop.load(inputStream);
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		HttpSolrClient client = new HttpSolrClient.Builder(prop.getProperty("solrDBName")).build();
		SolrQuery solrQuery = new SolrQuery();
		solrQuery.setQuery("id:"+solrId);
		solrQuery.set("fl","*");
		solrQuery.setRows(1);
		
		
		ImpressoContentItem impressoItem = null;
		try {
		    QueryRequest queryRequest = new QueryRequest(solrQuery);
		    queryRequest.setBasicAuthCredentials(prop.getProperty("solrUserName"),System.getenv("solrPassword"));
		    SolrDocumentList solrResponse = queryRequest.process(client).getResults();
		    impressoItem = new ImpressoContentItem(solrResponse.get(0)); //Get the only item of the list
		} catch (SolrServerException e) {
		    e.printStackTrace();
		} catch (IOException e) {
		    e.printStackTrace();
		}
		
		return impressoItem;
	}
	
}
