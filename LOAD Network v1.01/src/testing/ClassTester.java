package testing;

import java.util.List;
import impresso.SolrReader;

public class ClassTester {

	public static void main(String[] args) {
		SolrReader reader = new SolrReader();
		List<String> test = reader.getContentItemIDs("GDL");
		System.out.println(test.size());
	}

}
