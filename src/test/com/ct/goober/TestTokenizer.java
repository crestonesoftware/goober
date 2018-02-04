package test.com.ct.goober;

import java.util.Vector;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import junit.framework.TestCase;

import com.ct.goober.Tokenizer;


public class TestTokenizer {
	private static final Logger log4j = LogManager.getLogger(TestTokenizer.class 
	        .getName());

	private final String TITLE = "Title";
	private final String LOWER = "lower";
	private final String ABBR = "ABBR";
	private final String ULETTER = "P";
	
	private final String TITLE_TITLE_TITLE = TITLE + TITLE + TITLE;
	private final String LOWER_TITLE_TITLE = LOWER + TITLE + TITLE;
	private final String LOWER_LOWER_LOWER = LOWER + LOWER + LOWER;
	
	private final String TITLE_TITLE_ABBR = TITLE + TITLE + ABBR;
	private final String TITLE_ABBR_TITLE = TITLE + ABBR + TITLE;
	private final String ABBR_TITLE_TITLE = ABBR + TITLE + TITLE;
	
	private final String ULETTER_TITLE_TITLE = ULETTER + TITLE + TITLE;
	private final String TITLE_ULETTER_TITLE = TITLE + ULETTER + TITLE;
	private final String TITLE_TITLE_ULETTER = TITLE + TITLE + ULETTER;
	
	private Vector<String>theWords;
	private Tokenizer tst;
	

	
	@Before
	public void setUp() throws Exception {
		tst = new Tokenizer();
		theWords = new Vector<String>();
	}

	@After
	public void tearDown() throws Exception {
		theWords = null;
		tst = null;
	}

	@Test
	public void tokenize_StringWith3Words_CasesTitleTitleTitle_FormThreeTokens() {
		log4j.debug("Test: parse " + TITLE_TITLE_TITLE + " into three tokens");
		
		theWords.add(TITLE);
		theWords.add(TITLE);
		theWords.add(TITLE);
		
		tst.tokenize(TITLE_TITLE_TITLE);
		TestCase.assertEquals(theWords, tst.getTokens());
	}

	@Test
	public void tokenize_StringWith3Words_CasesLowerTitleTitle_FormThreeTokens() {
		log4j.debug("Test: parse " + this.LOWER_TITLE_TITLE + " into three tokens");
		
		theWords.add(LOWER);
		theWords.add(TITLE);
		theWords.add(TITLE);
		
		tst.tokenize(LOWER_TITLE_TITLE);
		TestCase.assertEquals(theWords, tst.getTokens());		
	}

	@Test
	public void tokenize_StringWith3Words_AllLowerCase_FormOneToken() {
		log4j.debug("Test: parse " + this.LOWER_LOWER_LOWER + " into one token");
		
		theWords.add(LOWER + LOWER + LOWER);
				
		tst.tokenize(this.LOWER_LOWER_LOWER);
		TestCase.assertEquals(theWords, tst.getTokens());		
	}
	
	@Test
	public void tokenize_StringWithAbbreviationAnd2Words_ABBRTitleTitle_FormThreeTokens() {
		log4j.debug("Test: parse " + this.ABBR_TITLE_TITLE + " into three tokens");
		
		theWords.add(ABBR);
		theWords.add(TITLE);
		theWords.add(TITLE);
		
		tst.tokenize(ABBR_TITLE_TITLE);
		
		TestCase.assertEquals(theWords, tst.getTokens());
	}	

	@Test
	public void tokenize_StringWithWordAbbreviationWord_FormThreeTokens() {
		log4j.debug("Test: parse " + this.TITLE_ABBR_TITLE + " into three tokens");
		
		theWords.add(TITLE);
		theWords.add(ABBR);
		theWords.add(TITLE);
		
		tst.tokenize(TITLE_ABBR_TITLE);

		TestCase.assertEquals(theWords, tst.getTokens());

	}	

	@Test
	public void tokenize_StringWithWordWordAbbreviation_FormThreeTokens() {
		log4j.debug("Test: parse " + this.TITLE_TITLE_ABBR + " into three tokens");
		
		theWords.add(TITLE);
		theWords.add(TITLE);
		theWords.add(ABBR);
		
		tst.tokenize(TITLE_TITLE_ABBR);
		
		TestCase.assertEquals(theWords, tst.getTokens());
	}	

	@Test
	public void tokenize_WordWordLetter_FormThreeTokens() {
		log4j.debug("Test: parse " + TITLE_TITLE_ULETTER + " into three tokens");
		theWords.add(TITLE);
		theWords.add(TITLE);
		theWords.add(ULETTER);
		
		tst.tokenize(TITLE_TITLE_ULETTER);
		
		TestCase.assertEquals(theWords, tst.getTokens());
	}	

	@Test
	public void tokenize_WordLetterWord_FormThreeTokens() {
		log4j.debug("Test: parse " + TITLE_ULETTER_TITLE + " into three tokens");
		theWords.add(TITLE);
		theWords.add(ULETTER);
		theWords.add(TITLE);
		
		tst.tokenize(TITLE_ULETTER_TITLE);
		
		TestCase.assertEquals(theWords, tst.getTokens());
	}	

	@Test
	public void tokenize_LetterWordWord_FormThreeTokens() {
		log4j.debug("Test: parse " + this.ULETTER_TITLE_TITLE + " into three tokens");
		
		theWords.add(this.ULETTER);
		theWords.add(TITLE);
		theWords.add(TITLE);
		
		tst.tokenize(ULETTER_TITLE_TITLE);
		
		TestCase.assertEquals(theWords, tst.getTokens());
	}	

	
}
