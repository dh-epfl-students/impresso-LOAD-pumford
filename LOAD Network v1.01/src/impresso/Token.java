package impresso;

import org.json.JSONObject;

public class Token {

	private String lemma;
	private String pos;
	private String surface;
	private String language;
	private int offset;
	
	public Token() {
		
	}
	public Token(JSONObject token, String tokLanguage) {
		pos = token.getString("p");
		lemma = token.getString("l");
		offset = token.getInt("o");
		surface = token.getString("t");
		language = tokLanguage;
	}
	
	public String getLemma() {
		return lemma;
	}
	
	public String getPOS() {
		return pos;
	}
	
	public int getOffset() {
		return offset;
	}
	
	public String getSurface() {
		return surface;
	}
	public String getLanguage() {
		return language;
	}
}
