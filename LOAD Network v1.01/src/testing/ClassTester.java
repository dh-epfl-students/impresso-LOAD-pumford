package testing;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import impresso.SolrReader;
import impresso.ImpressoContentItem;
import impresso.S3Reader;


public class ClassTester {

	private static final Logger LOGGER = Logger.getLogger(ClassTester.class.getName());

	
	public static void main(String[] args) throws IOException {
		//Creating logger
		ConsoleHandler handler = new ConsoleHandler();
		handler.setLevel(Level.FINE);
		LOGGER.addHandler(handler);
		LOGGER.log( Level.FINE, "Starting GDL reading");

		//Loads the property file
		Properties prop=new Properties();
		String propFilePath = "../resources/config.properties";
		
		FileInputStream inputStream;
		try {
			inputStream = new FileInputStream(propFilePath);
			prop.load(inputStream);
			inputStream.close();
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		
		SolrReader reader = new SolrReader(prop);
		

		
		List<String> indepluxIds = reader.getContentItemIDs("indeplux", false);

		//Testing getting the contentId
		ImpressoContentItem test = reader.getContentItem(indepluxIds.get(4));
		System.out.println(test.getContent_txt());
		
		//S3Reader injector = new S3Reader("EXP", "1933");
		//test = injector.injectLingusticAnnotations(test);
		//System.out.println(test.getTokens());
	}

}
