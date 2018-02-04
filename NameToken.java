package com.ct.goober;

public class NameToken {
	private String theString;

	public NameToken(String theString) {
		super();
		this.theString = theString;
	}

	public String getTheString() {
		return theString;
	}

	public void setTheString(String theString) {
		this.theString = theString;
	}

	public boolean equals(NameToken tok) {
		return this.theString.equals(tok.theString);
	}

	public boolean startsWith(String prefix) {
		return theString.startsWith(prefix);
	}

	public char[] toCharArray() {
		return theString.toCharArray();
	}

	public NameToken substring(int beginIndex, int endIndex) {
		return new NameToken(theString.substring(beginIndex, endIndex));
	}

	public NameToken substring(int beginIndex) {
		return new NameToken(theString.substring(beginIndex));
	}
	
	public String toString() {
		return this.theString;
	}
}
