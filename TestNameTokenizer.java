package test.com.ct.goober;

import java.util.Vector;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ct.goober.CodeName;
import com.ct.goober.NameToken;
import com.ct.goober.NameTokenizer;

import junit.framework.TestCase;


public class TestNameTokenizer {
	private static final Logger log4j = LogManager.getLogger(TestNameTokenizer.class 
	        .getName());

	private static final String TITLE = "Title";
	private static final String LOWER = "lower";
	private static final String ABBR = "ABBR";
	private static final String ULETTER = "P";
	
	private static final NameToken TITLE_TOKEN = new NameToken(TITLE);
	private static final NameToken LOWER_TOKEN = new NameToken(LOWER);
	private static final NameToken ABBR_TOKEN = new NameToken(ABBR);
	private static final NameToken ULETTER_TOKEN = new NameToken(ULETTER);
	
	private final String TITLE_TITLE_TITLE = TITLE + TITLE + TITLE;
	private final String LOWER_TITLE_TITLE = LOWER + TITLE + TITLE;
	private final String LOWER_LOWER_LOWER = LOWER + LOWER + LOWER;
	
	private final String TITLE_TITLE_ABBR = TITLE + TITLE + ABBR;
	private final String TITLE_ABBR_TITLE = TITLE + ABBR + TITLE;
	private final String ABBR_TITLE_TITLE = ABBR + TITLE + TITLE;
	
	private final String ULETTER_TITLE_TITLE = ULETTER + TITLE + TITLE;
	private final String TITLE_ULETTER_TITLE = TITLE + ULETTER + TITLE;
	private final String TITLE_TITLE_ULETTER = TITLE + TITLE + ULETTER;
	
	private Vector<NameToken>tstTokens;
	private CodeName tstCodeName;
	
	@Before
	public void setUp() throws Exception {
		tstTokens = new Vector<NameToken>();
	}

	@After
	public void tearDown() throws Exception {
		tstTokens = null;
	}

	@Test
	public void tokenize_1Word_CaseTitle_FormOneToken() {
		log4j.debug("Test: parse " + TITLE + " into one token");
		
		tstTokens.add(TITLE_TOKEN);		
		this.tstCodeName = new CodeName(TITLE);

		compareTokenVectors();
	}

	
	@Test
	public void tokenize_StringWith3Words_CasesTitleTitleTitle_FormThreeTokens() {
		log4j.debug("Test: parse " + TITLE_TITLE_TITLE + " into three tokens");
		
		tstTokens.add(TITLE_TOKEN);
		tstTokens.add(TITLE_TOKEN);
		tstTokens.add(TITLE_TOKEN);
		
		this.tstCodeName = new CodeName(TITLE_TITLE_TITLE);

		compareTokenVectors();
	}

	@Test
	public void tokenize_StringWith3Words_CasesLowerTitleTitle_FormThreeTokens() {
		log4j.debug("Test: parse " + this.LOWER_TITLE_TITLE + " into three tokens");
		
		tstTokens.add(LOWER_TOKEN);
		tstTokens.add(TITLE_TOKEN);
		tstTokens.add(TITLE_TOKEN);
		
		this.tstCodeName = new CodeName(LOWER_TITLE_TITLE);
		compareTokenVectors();		
	}

	@Test
	public void tokenize_StringWith3Words_AllLowerCase_FormOneToken() {
		log4j.debug("Test: parse " + this.LOWER_LOWER_LOWER + " into one token");
		
		tstTokens.add(new NameToken(LOWER + LOWER + LOWER));
				
		this.tstCodeName = new CodeName(this.LOWER_LOWER_LOWER);
		compareTokenVectors();				
	}

	@Test
	public void tokenize_StringWithAbbreviationAnd2Words_ABBRTitleTitle_FormThreeTokens() {
		log4j.debug("Test: parse " + this.ABBR_TITLE_TITLE + " into three tokens");
		
		tstTokens.add(ABBR_TOKEN);
		tstTokens.add(TITLE_TOKEN);
		tstTokens.add(TITLE_TOKEN);
		
		this.tstCodeName = new CodeName(this.ABBR_TITLE_TITLE);
		
		compareTokenVectors();		
	}	

	@Test
	public void tokenize_StringWithWordAbbreviationWord_FormThreeTokens() {
		log4j.debug("Test: parse " + this.TITLE_ABBR_TITLE + " into three tokens");
		
		tstTokens.add(TITLE_TOKEN);
		tstTokens.add(ABBR_TOKEN);
		tstTokens.add(TITLE_TOKEN);
		
		this.tstCodeName = new CodeName(this.TITLE_ABBR_TITLE);

		compareTokenVectors();		
	}	

	@Test
	public void tokenize_StringWithWordWordAbbreviation_FormThreeTokens() {
		log4j.debug("Test: parse " + this.TITLE_TITLE_ABBR + " into three tokens");
		
		tstTokens.add(TITLE_TOKEN);
		tstTokens.add(TITLE_TOKEN);
		tstTokens.add(ABBR_TOKEN);
		
		this.tstCodeName = new CodeName(this.TITLE_TITLE_ABBR);
		
		compareTokenVectors();		
	}	

	@Test
	public void tokenize_WordWordLetter_FormThreeTokens() {
		log4j.debug("Test: parse " + TITLE_TITLE_ULETTER + " into three tokens");
		tstTokens.add(TITLE_TOKEN);
		tstTokens.add(TITLE_TOKEN);
		tstTokens.add(ULETTER_TOKEN);
		
		this.tstCodeName = new CodeName(this.TITLE_TITLE_ULETTER);
		
		compareTokenVectors();		
	}	

	@Test
	public void tokenize_WordLetterWord_FormThreeTokens() {
		log4j.debug("Test: parse " + TITLE_ULETTER_TITLE + " into three tokens");
		tstTokens.add(TITLE_TOKEN);
		tstTokens.add(ULETTER_TOKEN);
		tstTokens.add(TITLE_TOKEN);
		
		this.tstCodeName = new CodeName(this.TITLE_ULETTER_TITLE);
		
		compareTokenVectors();		
	}	

	@Test
	public void tokenize_LetterWordWord_FormThreeTokens() {
		log4j.debug("Test: parse " + this.ULETTER_TITLE_TITLE + " into three tokens");
		
		tstTokens.add(ULETTER_TOKEN);
		tstTokens.add(TITLE_TOKEN);
		tstTokens.add(TITLE_TOKEN);
		
		this.tstCodeName = new CodeName(this.ULETTER_TITLE_TITLE);
		
		compareTokenVectors();		
	}	

	/*
	 * supporting methods
	 */
	private void compareTokenVectors() {
		TestCase.assertEquals(tstTokens.size(), tstCodeName.getTheTokens().size());
		int len = tstTokens.size();
		for (int idx = 0; idx < len; ++idx) {
			TestCase.assertEquals(tstTokens.get(idx).toString(), tstCodeName.getTheTokens().get(idx).toString());
		}
	}
}
