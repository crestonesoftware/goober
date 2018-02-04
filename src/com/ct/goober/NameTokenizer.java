package com.ct.goober;

import java.util.Vector;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NameTokenizer {

	private static Vector<NameToken> tokens;
	private static boolean hasAbbreviation = false;
	
	private static final Logger log4j = LogManager.getLogger(NameTokenizer.class 
	        .getName());

	private static void init() {
		tokens = new Vector<NameToken>();
		hasAbbreviation = false;
	}
	
	/**
	 * 
	 */
	public static Vector<NameToken> tokenize(String str) {
		log4j.debug(NameTokenizer.class.getName() + " starting tokenization of string [" + str + "]");
			
		init();
		
		getRawTokens(str);
		/* As of now, we have four possible token types:
		 * - all lowercase
		 * - Initial cap
		 * - Abbreviation (All caps)
		 * - Abbreviation followed by Initial Cap (2 or more caps followed by 1
		 * or more lower
		 * 
		 * if we have abbreviations attached to title case tokens, we need to split them		 
		 */
		
		if (hasAbbreviation)
			processAbbreviations();
		
		log4j.debug(NameTokenizer.class.getName() + " identified the following tokens for string \"" + str + "\":");
		if (log4j.isDebugEnabled())
			listStrings();
		
		return tokens;
	}
	
	private static void processAbbreviations() {
	log4j.trace("Begin processing abbreviations");
		
		Vector<NameToken> tempTokens = tokens;
		tokens = new Vector<NameToken>();
	
		for (NameToken token : tempTokens) {
			
			if (!token.startsWith("~")) 
				// not an abbreviation, so just add it
				tokens.addElement(token);
			else {
				char[] chars = token.toCharArray();
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
						tokens.add(token.substring(1,xx-1));
						tokens.add(token.substring(xx-1));
						break;
					} else if (chars.length == xx+1){
						// we reached the end of the token without a lowercase letter,
						// so the token ended with an uppercase letter. Add it before leaving.
						tokens.add(token.substring(1));
					}
					++xx;
				} // the for loop iterating over chars
			} // processing the token with the abbreviation
		} // the for loop iterating over tempTokens
		log4j.debug(NameTokenizer.class.getName() + ": after processing abbreviations, the tokens are:");
		listStrings();
	}

	
	private static void getRawTokens(String nm ) {
		StringBuffer tok = new StringBuffer();
		char[] chars = nm.toCharArray();
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
					tokens.addElement(new NameToken(tok.toString()));
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
		tokens.addElement(new NameToken(tok.toString()));
		
		// DEBUG - log tokens as of now
		log4j.debug(NameTokenizer.class.getName() + ": before processing abbreviations, the tokens are:");
		if (log4j.isDebugEnabled())
			listStrings();
	}
	
	private static void listStrings() {
		for (NameToken tok : tokens)
			log4j.debug(tok.toString());
	}
	


}
