package impresso;

import java.util.List;

import org.apache.solr.common.SolrDocument;
import construction.Annotation;

public class ImpressoContentItem {

	private String language;
	private String id;
	private String content_txt;
	private String year;

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
	
	/*public List<Annotation> getEntities(){
		
		
		
	}*/
}
