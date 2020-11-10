package hbm.util;

public enum Icons {

	ARROW_UP("\u2191"), ARROW_DOWN("\u2193"), ARROW_LEFT("\u2190"), ARROW_RIGHT("\u2192");

	private String unicode;

	private Icons(String unicode) {
		this.unicode = unicode;
	}

	public String getUnicode() {
		return unicode;
	}
	
	public String getUnicodeWithSpace() {
		return " " + getUnicode();
	}
}
