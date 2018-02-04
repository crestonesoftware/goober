package com.ct.goober;

public class MalformedDictionaryLineException extends Exception {
	
	public final static String NO_COMMA = "line has no comma";
	public final static String NO_WORD = "line has no word before the comma";
	public final static String NO_CODE = "line has no code after the comma";
	public static final String UNRECOGNIZED_CODE = "code after the comma must be 'r' or 'c' but was: ";
	
	public MalformedDictionaryLineException(String theLine, String theReason) {
		super("Dictionary line [" + theLine + "] is invalid because: [" + theReason + "]");
	}
}
