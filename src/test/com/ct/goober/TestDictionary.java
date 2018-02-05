package test.com.ct.goober;

import java.io.File;
import java.util.HashSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ct.goober.CodeName;
import com.ct.goober.Dictionary;

import junit.framework.TestCase;

public class TestDictionary {

	private static final Logger log4j = LogManager.getLogger(TestDictionary.class 
	        .getName());
	
	private File tstFile;

	
	
	private final String TEST_RESOURCE_PATH = "testResources/dictionary/";
	private final String TEN_RECOGNIZED = TEST_RESOURCE_PATH + "tenRecognized.txt";
	private final String BAD_NO_COMMA = TEST_RESOURCE_PATH + "bad_noComma.txt";
	private final String BAD_NO_WORD = TEST_RESOURCE_PATH + "bad_noWord.txt";
	private final String BAD_NO_CODE = TEST_RESOURCE_PATH + "bad_noCode.txt";
	private final String BAD_BAD_CODE = TEST_RESOURCE_PATH + "bad_BadCode.txt";
	private final String EMPTY_FILE = TEST_RESOURCE_PATH + "emptyFile.txt";
	
	private String TITLE = "Title";
	private String TOOTLE = "Tootle";
	private String TEETLE = "Teetle";

	private String CANDIDATE = "Candidate";
	private String WORD = "Word";
	
	private String ACCOUNT_TITLE_CASE = "Account";
	private String ACCOUNT_LOWER_CASE = "account";
	private String ACCOUNT_UPPER_CASE = "ACCOUNT";
	
	private String ACCOUNT_MISPELLED_INTERIOR_ONE_C = "Acount";
	private String ACCOUNT_MISPELLED_END_TWO_TS = "Accountt";
	private String ACCOUNT_MISPELLED_FIRST_TWO_CAPS = "ACcount";
	
	private Dictionary tstDict;
	private HashSet<String> tstWords;
	private HashSet<String> tstCandidates;
	
	@Before
	public void setUp() throws Exception {
		tstDict = new Dictionary();
		tstWords = new HashSet<String>();
		tstCandidates = new HashSet<String>();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void constructor_ConstructWithHashSet_OK() {
		tstWords.add(ACCOUNT_TITLE_CASE); 
		tstDict = new Dictionary(tstWords);
		TestCase.assertEquals(tstDict.getValidWords(),tstWords);
	}
	
	@Test
	public void setTheWords_OneWord_OK() {
		tstWords.add(ACCOUNT_TITLE_CASE); 
		tstDict.addValidWord(this.ACCOUNT_TITLE_CASE);
		TestCase.assertEquals(tstDict.getValidWords(),tstWords);
	}
	
	/*
	 * simple tests of recognizing mispellings
	 */
	@Test
	public void isRecognizedDoNotAdd_MispelledFirstTwoCaps_false() {
		tstDict.addValidWord(this.ACCOUNT_TITLE_CASE);
		TestCase.assertFalse(tstDict.checkSpelling(ACCOUNT_MISPELLED_FIRST_TWO_CAPS, false));
	}
	
	@Test
	public void isRecognizedDoNotAdd_MispelledInterior_false() {
		tstDict.addValidWord(this.ACCOUNT_TITLE_CASE);
		TestCase.assertFalse(tstDict.checkSpelling(ACCOUNT_MISPELLED_INTERIOR_ONE_C, false));
	}

	
	@Test
	public void isRecognizedDoNotAdd_MispelledEnd_false() {
		tstDict.addValidWord(this.ACCOUNT_TITLE_CASE);
		TestCase.assertFalse(tstDict.checkSpelling(ACCOUNT_MISPELLED_END_TWO_TS, false));
	}

	/*
	 * test addition of candidates
	 */
	@Test
	public void isRecognized_AddMispelledToCandidates_added() {
		tstDict.addValidWord(this.ACCOUNT_TITLE_CASE);
		this.tstCandidates.add(ACCOUNT_MISPELLED_FIRST_TWO_CAPS);
		// test: misspelling was NOT added to words
		TestCase.assertFalse(tstDict.checkSpelling(ACCOUNT_MISPELLED_FIRST_TWO_CAPS));

		// test: misspelling was added to candidates
		TestCase.assertTrue(tstDict.isCandidate(ACCOUNT_MISPELLED_FIRST_TWO_CAPS));
		
	}

	/*
	 * adds one word and two misspellings to dictionary
	 */
	@Test
	public void addCandidates_AddSetOfTwo_added() {
		HashSet<String> setOfStrings = new HashSet<String>();
		setOfStrings.add(this.ACCOUNT_LOWER_CASE);
		setOfStrings.add(this.ACCOUNT_MISPELLED_END_TWO_TS);
		
		this.tstDict.addUnrecognizedWords(setOfStrings);
		
		// test: candidates were NOT added as words
		TestCase.assertFalse(tstDict.checkSpelling(this.ACCOUNT_LOWER_CASE));
		TestCase.assertFalse(tstDict.checkSpelling(ACCOUNT_MISPELLED_END_TWO_TS));

		// test: candidates were added as candidates
		TestCase.assertTrue(tstDict.isCandidate(this.ACCOUNT_LOWER_CASE));
		TestCase.assertTrue(tstDict.isCandidate(this.ACCOUNT_MISPELLED_END_TWO_TS));
	}

	/*
	 * 
	 */
	@Test
	public void digestCodeName_ThreeUnrecognized_allAddedToCandidates() {
		CodeName cnm = new CodeName("TitleTootleTeetle");

		tstCandidates.add(this.TITLE);
		tstCandidates.add(this.TOOTLE);
		tstCandidates.add(this.TEETLE);
		
		this.tstDict.digestCodeName(cnm);
		TestCase.assertEquals(tstCandidates, tstDict.getUnrecognizedWords());
		TestCase.assertEquals(tstWords, tstDict.getValidWords());
		
	}
	
	/*
	 * 
	 */
	@Test
	public void digestCodeName_ThreeRecognized_noneAddedToCandidates() {
		CodeName cnm = new CodeName("TitleTootleTeetle");
		
		tstWords.add(this.TITLE);
		tstWords.add(this.TOOTLE);
		tstWords.add(this.TEETLE);
		
		this.tstDict.addValidWords(tstWords);
		
		this.tstDict.digestCodeName(cnm);
		TestCase.assertEquals(tstCandidates, tstDict.getUnrecognizedWords());
		TestCase.assertEquals(tstWords, tstDict.getValidWords());
		
	}

	/*
	 * 
	 */
	@Test
	public void digestCodeName_ThreeWordsOneRecognized_twoAddedToCandidates() {
		CodeName cnm = new CodeName("TitleTootleTeetle");
		
		tstWords.add(this.TITLE);
		this.tstCandidates.add(this.TOOTLE);
		this.tstCandidates.add(this.TEETLE);
		
		this.tstDict.addValidWords(tstWords);
		
		this.tstDict.digestCodeName(cnm);
		TestCase.assertEquals(tstCandidates, tstDict.getUnrecognizedWords());
		TestCase.assertEquals(tstWords, tstDict.getValidWords());
		
	}

	@Test
	public void loadFile_LoadTenRecognizedWords_ok() {
		tstFile = new File(this.TEN_RECOGNIZED);	
		tstDict.setSourceDictionaryFile(tstFile);
		tstDict.loadSourceDictionary(tstFile);
	}

	@Test
	public void loadFile_badNoWord_stopAfterFourWords() {
		tstFile = new File(this.BAD_NO_WORD);	
		tstDict.loadSourceDictionary(tstFile);
		TestCase.assertEquals("should have stopped after fourth word; number of words loaded", 4, tstDict.getValidWords().size());
		TestCase.assertEquals("should have loaded zero candidates", 0, tstDict.getUnrecognizedWords().size());
	}

	@Test
	public void loadFile_badNoComma_stopAfterFourWords() {
		tstFile = new File(this.BAD_NO_COMMA);	
		tstDict.loadSourceDictionary(tstFile);
		TestCase.assertEquals("should have stopped after fourth word; number of words loaded", 4, tstDict.getValidWords().size());
		TestCase.assertEquals("should have loaded zero candidates", 0, tstDict.getUnrecognizedWords().size());
	}

	@Test
	public void loadFile_badNoCode_stopAfterFourWords() {
		tstFile = new File(this.BAD_NO_CODE);	
		tstDict.loadSourceDictionary(tstFile);
		TestCase.assertEquals("should have stopped after fourth word; number of words loaded", 4, tstDict.getValidWords().size());
		TestCase.assertEquals("should have loaded zero candidates", 0, tstDict.getUnrecognizedWords().size());
	}

	@Test
	public void loadFile_badBadCode_stopAfterFourWords() {
		tstFile = new File(this.BAD_BAD_CODE);	
		tstDict.loadSourceDictionary(tstFile);
		TestCase.assertEquals("should have stopped after fourth word; number of words loaded", 4, tstDict.getValidWords().size());
		TestCase.assertEquals("should have loaded zero candidates", 0, tstDict.getUnrecognizedWords().size());
	}
	
	@Test
	public void writeNewWordsToOutputFile_OneCandidate_ok() {
		log4j.info("Test: writeNewWordsToOutputFile_OneCandidate_ok" );
		
		addOneOfEach();		
		
		setOutputToEmptyFile();
		
		tstDict.writeNewWordsToOutputFile(true);
		
		tstDict.loadSourceDictionary(tstFile);		
		TestCase.assertEquals(1,tstDict.getUnrecognizedWords().size());
		TestCase.assertEquals(0,tstDict.getValidWords().size());
		
		for (String str : tstDict.getUnrecognizedWords())
			TestCase.assertEquals(this.CANDIDATE, str);
		for (String str : tstDict.getValidWords())
			TestCase.assertEquals(this.WORD, str);	
	}

	@Test
	public void writeValidWordsToOutputFile_OneWordNoCandidates_ok() {
		addOneOfEach();
		setOutputToEmptyFile();
		
		tstDict.writeValidWordsToOutputFile(true);
		
		tstDict.loadSourceDictionary(tstFile);
		TestCase.assertEquals(0,tstDict.getUnrecognizedWords().size());
		TestCase.assertEquals(1,tstDict.getValidWords().size());

		for (String str : tstDict.getUnrecognizedWords())
			TestCase.assertEquals(this.CANDIDATE, str);
		for (String str : tstDict.getValidWords())
			TestCase.assertEquals(this.WORD, str);	
	}

	@Test
	public void writeAllWordsToOutputFile_OneWordOneCandidate_ok() {
		addOneOfEach();		
		setOutputToEmptyFile();
		
		tstDict.writeAllToOutputFile(true);

		tstDict.loadSourceDictionary(tstFile);
		TestCase.assertEquals(1,tstDict.getUnrecognizedWords().size());
		TestCase.assertEquals(1,tstDict.getValidWords().size());
		
		for (String str : tstDict.getUnrecognizedWords())
			TestCase.assertEquals(this.CANDIDATE, str);
		for (String str : tstDict.getValidWords())
			TestCase.assertEquals(this.WORD, str);	
	}

	private void setOutputToEmptyFile() {
		tstFile = new File(this.EMPTY_FILE);			
		tstDict.setOutputDictionaryFile(tstFile);
	}
	
	private void addOneOfEach() {
		tstDict.addUnrecognizedWord(CANDIDATE);
		tstDict.addValidWord(WORD);
	}
}
