package edu.columbia.material.morph.analysis.constants;

import java.util.Arrays;

public class TagalogConstants extends LanguageConstants{

	public static String TAGALOG_STANFORD_CORENLP_POS_MODEL = "resources/TGL-POSUD-stanford.model";

	//Constructor
	public TagalogConstants(){
				
		String[] MARKERS_STR = {"ang", "kay", "kina", "ma", "mga", "ng", "ni", "ng", "nung", "pa", "a", "si", "pinaka", "na", "at", 
								"and", "hindi", "may", "dahil", "niya", "siya", "pero", "kaya", "nito", "sila", "nila", "araw", "lahat", 
								"dapat", "mula", "kanya", "ayon", "matapos", "dito", "kahit", "kapag", "loob", "marami", "ano"};
		
		MARKERS = Arrays.asList(MARKERS_STR);

	}
	
	public Language getLanguage() {
		return Language.TAGALOG;
	}
	
}

