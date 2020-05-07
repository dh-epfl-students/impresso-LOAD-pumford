package impresso;

import java.util.List;

import org.apache.solr.common.SolrDocument;
import org.json.JSONArray;
import org.json.JSONObject;

import construction.Annotation;

public class ImpressoContentItem {

	private String language;
	private String id;
	private String content_txt;
	private String year;
	private List<Token> tokens;
	
	
	public ImpressoContentItem() {
		
	}
	public ImpressoContentItem(SolrDocument document) {
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
		
		year = (String) document.getFieldValue("meta_year_i");
			
	}
	
	public void printProperties() {
		System.out.println(this.id);
		System.out.println(this.language);
		System.out.println(this.content_txt);
		return;
	}
	
	public void injectTokens(JSONArray tokenArray) {
		int length = tokenArray.length();
		for(int i=0; i<length; i++) {
			  JSONObject token = tokenArray.getJSONObject(i);
			  tokens.add(new Token(token));
		}
		return;
	}
	
	/*public List<Annotation> getEntities(){

	}
	
	public List<Annotation> getSentences(){
		
	}*/
	
	public List<Token> getTokens(){
		return tokens;
	}
}
