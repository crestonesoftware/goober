package com.ct.goober;

import java.util.Vector;

public class CodeName {
	private String theName;
	private Vector<NameToken> theNameTokens;
	
	/* ********************
	 * Constructor
	 */
	public CodeName(String nm) {
		super();
		theName = nm;
		theNameTokens = NameTokenizer.tokenize(theName);
	}

	/* ********************
	 * Getters and Setters
	 */
	public String toString() {
		return theName.toString();
	}
	
	public boolean equals(CodeName cnm) {
		return this.theName.equals(cnm.theName);
	}
	
	public String getTheName() {
		return theName;
	}

	public void setTheName(String theName) {
		this.theName = theName;
	}

	public Vector<NameToken> getTheTokens() {
		return theNameTokens;
	}

	public void setTheParts(Vector<NameToken> theParts) {
		this.theNameTokens = theParts;
	}
	
	/* ********************
	 * Methods
	 */
	
	public void tokenize(Tokenizer toker) {
		//theParts = toker.tokenize(this.theName);
	}
	
	boolean isRecognizedInDictionary(Dictionary dict) {
		return false;
	}

	public char[] toCharArray() {
		return this.theName.toCharArray();
	}

	public void append(NameToken token) {
		this.theNameTokens.addElement(token);	
	}
}
