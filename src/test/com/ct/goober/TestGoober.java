package test.com.ct.goober;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ct.goober.Goober;

import junit.framework.TestCase;

public class TestGoober {
	private final String TEST_RESOURCE_PATH = "testResources/goober/";
	private final String TEST_INPUT_PATH = TEST_RESOURCE_PATH + "input/";
	private final String TEST_OUTPUT_PATH = TEST_RESOURCE_PATH + "output/";
	private final String DICTIONARY_OUTPUT_FILE_PATH = TEST_OUTPUT_PATH + "dictionaryOutputFile.txt";
	private final String TEN_RECOGNIZED = TEST_INPUT_PATH + "tenRecognized.txt";
	private final String SAMPLE_NAMES_FILE_PATH = TEST_INPUT_PATH + "sampleNamesFile.txt";
	
	private Goober theGoober;
	private File namesFile;
	
	@Before
	public void setUp() throws Exception {
		theGoober = new Goober();
	}

	@After
	public void tearDown() throws Exception {
		this.theGoober = null;
	}
		
	@Test
	public void test() {
		namesFile = new File(this.SAMPLE_NAMES_FILE_PATH);
		TestCase.assertTrue("Failed to init theGoober", theGoober.init(this.TEN_RECOGNIZED, DICTIONARY_OUTPUT_FILE_PATH));
		TestCase.assertTrue("Failed to parse", theGoober.parseFile(this.namesFile));
		theGoober.writeToTargetDictionary();
		
	}

}
