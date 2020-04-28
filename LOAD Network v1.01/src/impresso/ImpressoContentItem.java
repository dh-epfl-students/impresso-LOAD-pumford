package impresso;

import org.apache.solr.common.SolrDocument;


public class ImpressoContentItem {

	
	public ImpressoContentItem() {
		
	}
	public ImpressoContentItem(SolrDocument document) {
		String id = (String) document.getFieldValue("id");
		String language = (String) document.getFieldValue("lg_s");
		String content_text = "";
		
		switch(language) {
		case "fr":
			content_text = (String) document.getFieldValue("content_text_fr");

		case "de":
			content_text = (String) document.getFieldValue("content_text_de");

		case "lu":
			content_text = (String) document.getFieldValue("content_text_lu");

		}
			
	}
}
