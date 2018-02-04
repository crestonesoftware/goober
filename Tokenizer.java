package com.ct.goober;

import java.util.Vector;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Tokenizer {

	private Vector<String> tokens;
	private boolean hasAbbreviation = false;
	
	private static final Logger log4j = LogManager.getLogger(Tokenizer.class 
	        .getName());
	
	public Tokenizer() {
		tokens = new Vector<String>();		
	}
	
	/**
	 * @param str
	 */
	public void tokenize(String str) {
		log4j.trace("received string to parse: " + str);
				
		//if ()
		
		getRawTokens(str);
		/* As of now, we have four possible token types:
		 * - all lowercase
		 * - Initial cap
		 * - Abbreviation (All caps)
		 * - Abbreviation followed by Initial Cap (2 or more caps followed by 1
		 * or more lower
		 * 
		 * Next we deal with the abbreviations		 
		 */
		
		if (!hasAbbreviation) {
			log4j.trace("No abbreviations in current string");
			return;
		} else
			log4j.trace("Begin processing abbreviations");
		
		Vector<String> tempTokens = tokens;
		tokens = new Vector<String>();
	
		for (String tokenString : tempTokens) {
			
			if (!tokenString.startsWith("~")) 
				// not an abbreviation, so just add it
				tokens.addElement(tokenString);
			else {
				char[] chars = tokenString.toCharArray();
				// iterate through the token, looking for the first lowercase
				// character, which indicates the abbreviation ended one character earlier
				int xx = 0;
				for (char cc : chars) {
					if (0 == xx) 
						; //skip the tilde
					else if (Character.isLowerCase(cc)) { 
						// this lowercase letter indicates the first character after the 
						// abbreviation, so we can now split the string into abbreviation 
						// and the following token
						tokens.add(tokenString.substring(1,xx-1));
						tokens.add(tokenString.substring(xx-1));
						break;
					} else if (chars.length == xx+1){
						// we reached the end of the token without a lowercase letter,
						// so the token ended with an uppercase letter. Add it before leaving.
						tokens.add(tokenString.substring(1));
					}
					++xx;
				} // the for loop iterating over chars
			} // processing the token with the abbreviation
		} // the for loop iterating over tempTokens
		log4j.debug("Pars.Tokenize(): The final tokens are:");
		listStrings();
	}

	private void getRawTokens(String str ) {
		StringBuffer tok = new StringBuffer();
		char[] chars = str.toCharArray();
		int idx = 0;
		boolean lastCharWasUpper = false;
		
		for (char cc : chars) {
			if (0 == idx) {
				// the first character. Always start a token with the first character.
				tok.append(cc);
				if (Character.isUpperCase(cc)) {
					log4j.trace("First Char is uppercase");
					lastCharWasUpper = true;
				}
				log4j.trace("First character is " + cc);
			} else if (Character.isUpperCase(cc)) {
				log4j.trace(cc + " is uppercase");
				if (lastCharWasUpper) {
					// the previous char was also Upper, so we have consecutive
					// uppercase characters.
					if (1 == tok.length()) {
						// this is the 2nd CAP in the series. Prepend a tilde
						// to mark this token as starting with consecutive caps
						tok.insert(0, '~');
						hasAbbreviation = true;
					}						
				} else {
					// the previous char was lower, and this is Upper, so it is the 
					// start of a new token. Add the current and start the new one.
					tokens.addElement(tok.toString());
					lastCharWasUpper = true;
					if ( idx < chars.length){ 
						tok = new StringBuffer();
					}  
				} 
				tok.append(cc);
			} else {
				//lowercase never starts a token, so continue the current token
				log4j.trace(cc + " is lowercase");
				tok.append(cc);
				lastCharWasUpper = false;
			}
			++idx;
		}
		tokens.addElement(tok.toString());
		log4j.debug("Pars.Tokenize: the raw tokens are:");
		this.listStrings();
	}
	
	public Vector<String> getTokens() {
		return tokens;
	}

	public void setTokens(Vector<String> tokens) {
		this.tokens = tokens;
	}

	public void listStrings() {
		for (String str : tokens)
			log4j.debug(str);
	}
	


}
