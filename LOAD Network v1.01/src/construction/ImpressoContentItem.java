package construction;

import org.apache.solr.common.SolrDocument;


public class ImpressoContentItem {

	
	public ImpressoContentItem() {
		
	}
	public ImpressoContentItem(SolrDocument document) {
		String id = (String) document.getFieldValue("id");
		String content_text_fr = (String) document.getFieldValue("content_text_fr");
	}
}
