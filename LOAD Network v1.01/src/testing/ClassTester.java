package testing;

import java.util.List;
import impresso.SolrReader;
import impresso.ImpressoContentItem;

public class ClassTester {

	public static void main(String[] args) {
		SolrReader reader = new SolrReader();
		//List<String> test = reader.getContentItemIDs("GDL");
		//System.out.println(test.size());
		
		//Testing getting the contentId
		ImpressoContentItem test = reader.getContentItem("EXP-1933-01-16-a-i0055");
		test.printProperties();
	}

}
