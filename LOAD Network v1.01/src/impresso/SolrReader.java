package impresso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import java.io.FileInputStream;


import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.request.QueryRequest;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.CursorMarkParams;


public class SolrReader {

	public SolrReader() {
		
	}
	
	public List<String> getContentItemIDs(String newspaperID) {
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
		solrQuery.setQuery("meta_journal_s:"+newspaperID + "AND item_type_s:(ar OR ob OR page)"); //Sets query for the newspaper and limits the item_type
		solrQuery.set("fl","id");
		solrQuery.addSort("id", ORDER.asc);  
		solrQuery.setRows(10000);
		List<String> solrIds = new ArrayList<>();
	    QueryRequest queryRequest = new QueryRequest(solrQuery);
	    String cursorMark = CursorMarkParams.CURSOR_MARK_START;
	    boolean done = false;
	    
		try {
			while(!done) {
			    solrQuery.set(CursorMarkParams.CURSOR_MARK_PARAM, cursorMark);
			    queryRequest.setBasicAuthCredentials(prop.getProperty("solrUserName"),System.getenv("solrPassword"));
			    QueryResponse solrResponse = queryRequest.process(client);
			    solrIds.addAll(solrResponse.getResults()
			    		  .stream().map(x -> (String) x.get("id"))
			    		  .collect(Collectors.toList()));
			    String nextCursorMark = solrResponse.getNextCursorMark();
			    if (cursorMark.equals(nextCursorMark)) {
			        done = true;
			    }
			    cursorMark = nextCursorMark;
			}

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
