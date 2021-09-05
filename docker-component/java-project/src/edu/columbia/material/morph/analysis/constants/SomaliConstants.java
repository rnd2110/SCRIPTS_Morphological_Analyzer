package edu.columbia.material.morph.analysis.constants;

import java.util.Arrays;

public class SomaliConstants extends LanguageConstants{
		
	//Constructor
	public SomaliConstants(){
		
		String[] MARKERS_STR = {"ilaa", "gudaha", "aay", "ka", "ku", "ayaa", "ah", "in", "and", "iyo", "soomaaliya", "waxaa", 
								"waxa", "mid", "inay", "badan", "waa", "markii", "waxay", "aan", "tahay", "an", "ahaa", "yahay", 
								"inuu", "muqdisho", "dib", "wuxuu", "yihiin", "kenya", "axmed", "wada", "sheikh", "leh", "iyadoo", 
								"ayuu", "sidoo", "kuwa", "ama", "maxamuud", "lahaa", "cusub", "mudane"};

		MARKERS = Arrays.asList(MARKERS_STR);
		
	}
	
	public Language getLanguage() {
		return Language.SOMALI;
	}
	
}

