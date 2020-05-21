package impresso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.solr.common.SolrDocument;
import org.json.JSONArray;
import org.json.JSONObject;

import construction.Annotation;

public class ImpressoContentItem {

	private String language;
	private String id;
	private String content_txt;
	private Integer year;
	private List<Token> tokens;
	private List<Annotation> annotations;
	private static Properties prop;
	
	public ImpressoContentItem() {
		
	}
	
	public ImpressoContentItem(SolrDocument document, Properties properties) {
		id = (String) document.getFieldValue("id");
		language = (String) document.getFieldValue("lg_s");
		
		switch(language) {
		case "fr":
			content_txt = (String) document.getFieldValue("content_txt_fr");
			break;
		case "de":
			content_txt = (String) document.getFieldValue("content_txt_de");
			break;
		case "lu":
			content_txt = (String) document.getFieldValue("content_txt_lu");
			break;
		}
		
		year = (Integer) document.getFieldValue("meta_year_i");
		tokens = new ArrayList<Token>();
		prop = properties;
	}
	
	public void injectTokens(JSONArray tokenArray, String tokLang) {
		int length = tokenArray.length();
		String[] posTypes = prop.get("PoSTypes").toString().split(",");
		
		for(int i=0; i<length; i++) {
			  JSONObject token = tokenArray.getJSONObject(i);
			  if(tokLang == null) {
				  tokLang = language;
			  }
			  
			  //Check to see if the token is a pos within the PoS types we would like to keep
			  if(Arrays.stream(posTypes).anyMatch(token.getString("p")::equals)) {
				  tokens.add(new Token(token, tokLang));
			  }
		}
		return;
	}
	
	public List<Annotation> getEntities(){
		return annotations;
	}
	/*
	public List<Annotation> getSentences(){
		
	}*/
	
	public List<Token> getTokens(){
		return tokens;
	}
	
	public String getlanguage() {
		return language;
	}
	public String getId() {
		return id;
	}
	public String getContent_txt() {
		return content_txt;
	}
	public Integer getYear() {
		return year;
	}
	
}
