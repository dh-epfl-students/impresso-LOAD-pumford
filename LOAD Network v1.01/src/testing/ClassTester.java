package testing;

import java.io.IOException;
import java.util.List;
import impresso.SolrReader;
import impresso.ImpressoContentItem;
import impresso.S3Reader;


public class ClassTester {

	public static void main(String[] args) throws IOException {
		SolrReader reader = new SolrReader();
		//List<String> test = reader.getContentItemIDs("GDL");
		//System.out.println(test.size());
		
		//Testing getting the contentId
		ImpressoContentItem test = reader.getContentItem("EXP-1933-01-16-a-i0055");
		//test.printProperties();
		
		S3Reader injector = new S3Reader("EXP", "1933");
		test = injector.injectLingusticAnnotations(test);
		System.out.println(test.getTokens());
	}

}
