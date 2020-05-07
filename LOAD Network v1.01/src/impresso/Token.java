package impresso;

import org.json.JSONObject;

public class Token {

	private String lemma;
	private String pos;
	private String surface;
	private int location;
	
	public Token() {
		
	}
	public Token(JSONObject token) {
		pos = token.getString("p");
		lemma = token.getString("l");
		location = token.getInt("o");
		}
	
	public String getLemma() {
		return lemma;
	}
	
	public String getPOS() {
		return pos;
	}
	
	public int getLocaiton() {
		return location;
	}
}
