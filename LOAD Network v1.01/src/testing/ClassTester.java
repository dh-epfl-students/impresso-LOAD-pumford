package testing;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import impresso.SolrReader;
import impresso.ImpressoContentItem;
import impresso.S3Reader;


public class ClassTester {

	private static final Logger LOGGER = Logger.getLogger(ClassTester.class.getName());

	
	public static void main(String[] args) throws IOException {
		SolrReader reader = new SolrReader();
		
		//Creating logger
		ConsoleHandler handler = new ConsoleHandler();
		handler.setLevel(Level.FINE);
		LOGGER.addHandler(handler);
		LOGGER.log( Level.FINE, "Starting GDL reading");

		
		List<String> indepluxIds = reader.getContentItemIDs("indeplux", false);
		
		for (String id: indepluxIds) {
			System.out.println(id);
		}
		//Testing getting the contentId
		//ImpressoContentItem test = reader.getContentItem("EXP-1933-01-16-a-i0055");
		//test.printProperties();
		
		//S3Reader injector = new S3Reader("EXP", "1933");
		//test = injector.injectLingusticAnnotations(test);
		//System.out.println(test.getTokens());
	}

}
