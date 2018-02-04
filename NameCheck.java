package com.ct.goober;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NameCheck {

	private static final Logger log4j = LogManager.getLogger(NameCheck.class 
	        .getName());
	
	public static void main(String[] args) {
		
		log4j.debug("NameCheck: begin");
		log4j.debug("source dictionary[" + args[0] +  "]");
		log4j.debug("output to [" + args[1] +  "]");
		log4j.debug("file to parse [" + args[2]);
		
		Goober goob = new Goober();
		if (!goob.init(args[0],args[1]))
			return;
		if (!goob.parseFile(new File(args[2])))
			return;
		goob.writeToTargetDictionary();
			
		log4j.debug("done");

	}

}
