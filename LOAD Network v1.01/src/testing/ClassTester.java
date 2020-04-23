package testing;

import construction.SolrReader;

public class ClassTester {

	public static void main(String[] args) {
		SolrReader reader = new SolrReader();
		System.out.println(reader.getContentItemIDs("GDL"));
	}

}
