package com.ct.goober;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Vector;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Dictionary {
	private File sourceDictionaryFile;
	private File outputDictionaryFile;
	private HashSet<String> validWords;
	private HashSet<String> unrecognizedWords;

	private static final Logger log4j = LogManager.getLogger(Dictionary.class 
	        .getName());

	public boolean writeAllToOutputFile(boolean createFileIfNotExistent) {
		return writeToFile(outputDictionaryFile,createFileIfNotExistent, true, true);
    }

	public boolean writeAllToSourceFile(boolean createFileIfNotExistent) {
		return writeToFile(sourceDictionaryFile,createFileIfNotExistent, true, true);
    }

	public boolean writeNewWordsToOutputFile(boolean createFileIfNotExistent) {
		return writeToFile(this.outputDictionaryFile, createFileIfNotExistent, true, false);
	}	

	public boolean writeValidWordsToOutputFile(boolean createFileIfNotExistent) {
		return writeToFile(this.outputDictionaryFile, createFileIfNotExistent, false, true);
	}	

	private boolean writeToFile(File targetFile, boolean createFileIfNotExistent, boolean writeNewWords, boolean writeValidWords) {
		return writeToFile(targetFile, createFileIfNotExistent, writeNewWords, writeValidWords, true);
	}

	
	/*
	 * TODO look up how finally block ends normally
	 */
	private boolean writeToFile(File targetFile, boolean createFileIfNotExistent, boolean writeNewWords, boolean writeValidWords, boolean sorted) {
        try {
            	if (!targetFile.exists()) {
            		log4j.trace("writeToFile(): target file [" + targetFile.getName() + "] does not exist"); 
	            	if (createFileIfNotExistent) {
	            		log4j.trace("writeToFile(): option to create file chosen");
	            		if (!targetFile.createNewFile()) {
	            			log4j.error("writeToFile failed. Cannot write to specified targetFile [" + targetFile.getAbsolutePath() + "]");
	            			return false;
	            		}
	            	}
	            	else {
	            		log4j.error("writeToFile failed. Specified targetFile [" + targetFile.getName() + "] does not exist. Consider "
	            				+ "specifying the option to create the file when not found.");
	        			return false;
	            	}
            	}
        	
        	
        	// FileReader reads text files in the default encoding.
        	FileWriter fileWriter = new FileWriter(targetFile);
 
            // Always wrap FileReader in BufferedWriter.
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            int lineCounter;
            int wordsInTheSet;
            boolean needNewLine = false;
            Vector<String> sortedWords;
            
            if (writeNewWords) {
            	lineCounter = 0;
            	wordsInTheSet = unrecognizedWords.size();
            	sortedWords = new Vector<String>();
            	sortedWords.addAll(this.unrecognizedWords);
            	if (sorted)
            		sortedWords.sort(Comparator.naturalOrder());
            	
            	for (String str : sortedWords) {
                	bufferedWriter.write(str + ",c");
	            	if (lineCounter < wordsInTheSet) //don't add newLine after last word
	            		bufferedWriter.newLine();
	            	else
	            		needNewLine = true;	
	            	++lineCounter;
                }
            }
            
            if (writeValidWords) {
            	lineCounter = 0;
            	wordsInTheSet = unrecognizedWords.size();
            	sortedWords = new Vector<String>();
            	sortedWords.addAll(this.validWords);
            	if (sorted)
            		sortedWords.sort(Comparator.naturalOrder());

            	
            	for (String str : sortedWords) {
	            	if (needNewLine) {
	            		bufferedWriter.newLine(); // add newline if new words were already written because new words will have ended without a newline
	            		needNewLine = false;
	            	}
            		bufferedWriter.write(str + ",r");
	            	if (lineCounter < wordsInTheSet) //don't add newLine after last word
	            		bufferedWriter.newLine();
	            	++lineCounter;
	            }
            }

            bufferedWriter.close();        
            return true;
        }
        
        catch(FileNotFoundException ex) {
        	System.out.println(
                "Dictionary file '" + sourceDictionaryFile + "' not found. " + ex.getMessage());  
        	return false;
        }
        catch(IOException ex) {
            System.out.println(
                "Error writing to file '" + sourceDictionaryFile + "'. " + ex.getMessage());
            return false;
        } /* @TODO look up how finally block ends normally. If return is from finally block, remove returns from catch blocks
         *  finally {
        	return false;
        }*/
    }
	
	public boolean loadSourceDictionary(File fil) {
		log4j.debug("loadSourceDictionary(File): loading words from " + fil.getAbsolutePath());
		if (!fil.exists()) {
			log4j.error("loadSourceDictionary: failed to load dictionary because the file [" + fil.getAbsolutePath() + "] does not exist");
			return false;
		}
		this.clearAllWords();
			
		String line = null;
		this.sourceDictionaryFile = fil;

        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader = new FileReader(sourceDictionaryFile);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) {
                ingestWordFromSource(line);
                log4j.trace("ingested line [" + line + "]");
            }   

            // Always close files.
            bufferedReader.close();
            log4j.trace("loadSourceDictionary(File): complete");
            return true;
        }
        
        catch(MalformedDictionaryLineException ex) {
            log4j.error ("Malformed line exception. " + ex.getMessage());                
        }
        catch(FileNotFoundException ex) {
        	log4j.error("Dictionary file '" + sourceDictionaryFile + "' not found. " + ex.getMessage());                
        }
        catch(IOException ex) {
            log4j.error("Error reading file '" + sourceDictionaryFile + "'. " + ex.getMessage());
        } 
        return false; // only reachable when exception occurred
    }
		
	private void ingestWordFromSource(String line) throws MalformedDictionaryLineException {
		if (!line.contains(","))
			throw new MalformedDictionaryLineException(line, MalformedDictionaryLineException.NO_COMMA);
		
		String theWord = line.trim().substring(0, line.indexOf(","));
		log4j.trace("ingestWordFromSource(): for line " + line + " the WORD is " + theWord);
		String theCode = line.trim().substring(line.indexOf(",") + 1);
		log4j.trace("ingestWordFromSource(): for line " + line + " the CODE is " + theCode);
		
		if (theWord.isEmpty())
			throw new MalformedDictionaryLineException(line, MalformedDictionaryLineException.NO_WORD);
		if (theCode.isEmpty())
			throw new MalformedDictionaryLineException(line, MalformedDictionaryLineException.NO_CODE);
		if (theCode.equals("r"))
			this.addValidWord(theWord);
		else if (theCode.equals("c"))
			this.addUnrecognizedWord(theWord);
		else
			throw new MalformedDictionaryLineException(line, MalformedDictionaryLineException.UNRECOGNIZED_CODE + theCode);
	}
	

	

	
	/* 
	 * 	Returns 
	 * 	- true, if the word is found in validWords
	 *  - false, if the word is NOT found in validWords
	 * TODO enhance to check for exact match and for case-insensitive near-match
	 */
	public boolean checkSpelling(String str, boolean addToCandidates) {
		if (validWords.contains(str))
			return true;
		else {
			if (addToCandidates)
				this.addUnrecognizedWord(str);
			return false;
		}
	}
	
	/* 
	 * TODO enhance to check for exact match and for case-insensitive near-match
	 */
	public boolean checkSpelling(NameToken tok, boolean addToCandidates) {
		return this.checkSpelling(tok.toString(), addToCandidates);
	}
	
	/* 
	 * TODO enhance to check for exact match and for case-insensitive near-match
	 */
	public boolean checkSpelling(String str) {
		return checkSpelling(str, true);
	}
	
	/* 
	 * TODO enhance to check for exact match and for case-insensitive near-match
	 */
	public boolean checkSpelling(NameToken tok) {
		return checkSpelling(tok, true);
	}
	
	/* 
	 * 	Returns true if all Strings are found in validWords
	 * TODO enhance to check for exact match and for case-insensitive near-match
	 */
	public boolean allRecognized(Collection<String> strings) {
		if (validWords.containsAll(strings))
			return true;
		else
			return false;
	}

	public boolean isCandidate(String str) {
		if (this.unrecognizedWords.contains(str))
			return true;
		else
			return false;
	}

	public void digestCodeName(CodeName cnm) {
		for (NameToken tok : cnm.getTheTokens()) {
			this.checkSpelling(tok);
		}
		
	}


	/* *************************
	 * Simple Methods
	 */	
	
	public void clearAllWords() {
		validWords = new HashSet<String>();
		this.unrecognizedWords = new HashSet<String>();
	}

	public void clearUnrecognizedWords() {
		this.unrecognizedWords = new HashSet<String>();
	}
	
	public void clearValidWords() {
		validWords = new HashSet<String>();
	}
	
	public void addValidWord(String str) {
		validWords.add(str);
	}

	public void removeValidWord(String str) {
		validWords.remove(str);
	}

	public void addUnrecognizedWord(String str) {
		this.unrecognizedWords.add(str);
	}

	public void removeUnrecognizedWord(String str) {
		this.unrecognizedWords.remove(str);
	}

	/* *************************
	 * Getters and Setters
	 * 
	 */
	public void addUnrecognizedWords(HashSet<String> stringsToAdd) {
		this.unrecognizedWords.addAll(stringsToAdd);
	}

	public File getSourceDictionaryFile() {
		return sourceDictionaryFile;
	}

	public void setSourceDictionaryFile(File sourceDictionaryFile) {
		this.sourceDictionaryFile = sourceDictionaryFile;
	}

	public File getOutputDictionaryFile() {
		return outputDictionaryFile;
	}

	public void setOutputDictionaryFile(File outputDictionaryFile) {
		this.outputDictionaryFile = outputDictionaryFile;
	}

	public HashSet<String> getValidWords() {
		return validWords;
	}

	public void setValidWords(HashSet<String> validWords) {
		this.validWords = validWords;
	}

	public HashSet<String> getUnrecognizedWords() {
		return unrecognizedWords;
	}

	public void setUnrecognizedWords(HashSet<String> unrecognizedWords) {
		this.unrecognizedWords = unrecognizedWords;
	}

	public void addValidWords(HashSet<String> stringsToAdd) {
		this.validWords.addAll(stringsToAdd);
	}	

	/* *************************
	 * Constructors
	 * 
	 */
	public Dictionary() {
		super();
		this.clearValidWords();
		this.clearUnrecognizedWords();
	}
	
	public Dictionary(HashSet<String> theWords) {
		super();
		this.validWords = theWords;
		this.clearUnrecognizedWords();
	}
	
}
