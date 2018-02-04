package com.ct.goober;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Goober {
	
	private static final Logger log4j = LogManager.getLogger(Goober.class 
	        .getName());
	
	private Dictionary dict;
	private File sourceDictionaryFile;
	private File outputDictionaryFile;

	public Goober(File dictionarySource) {
		super();
		sourceDictionaryFile = dictionarySource;
	}
	public Goober() {
		// TODO Auto-generated constructor stub
	}
	public boolean init(String sourcePath, String targetPath) {
		log4j.trace("init(String sourcePath, String targetPath): begin");
		dict = new Dictionary();
		this.sourceDictionaryFile = new File(sourcePath);
		this.outputDictionaryFile = new File(targetPath);
		if (!dict.loadSourceDictionary(sourceDictionaryFile)) {
			log4j.error("init: failed to load from source dictionary file [" + sourceDictionaryFile.getAbsolutePath() + "]");
			return false;
		}
		dict.setOutputDictionaryFile(outputDictionaryFile);
		log4j.trace("init(String sourcePath, String targetPath): complete");
		return true;
	}
	
	public boolean init(String sourcePath) {
		log4j.trace("init(String sourcePath, String targetPath): begin");
		dict = new Dictionary();
		this.sourceDictionaryFile = new File(sourcePath);
		if (!dict.loadSourceDictionary(sourceDictionaryFile)) {
			log4j.error("init: failed to load from source dictionary file [" + sourceDictionaryFile.getAbsolutePath() + "]");
			return false;
		}
		log4j.trace("init(String sourcePath, String targetPath): complete");
		return true;
	}	
	public void writeToTargetDictionary () {
		dict.writeAllToOutputFile(true);
	}

	public void updateSourceDictionary () {
		dict.writeAllToSourceFile(true);
	}
	
	public boolean parseFile(File fileToParse) {
		String line = null;
		Vector<NameToken> nameTokens;
		log4j.debug("parseFile(): parsing " + fileToParse.getAbsolutePath());
		
        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader = new FileReader(fileToParse);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) {
            	log4j.trace("line: " + line);
            	nameTokens = NameTokenizer.tokenize(line);
            	for (NameToken tok : nameTokens) 
            		dict.checkSpelling(tok);
            }   

            // Always close files.
            bufferedReader.close();
            return true;
        }
        
        catch(FileNotFoundException ex) {
            log4j.error("Dictionary file '" + sourceDictionaryFile + "' not found. " + ex.getMessage());
        }
        catch(IOException ex) {
        	log4j.error("Error reading file '" + sourceDictionaryFile + "'. " + ex.getMessage());
        }
        return false;
    }
}
