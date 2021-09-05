package edu.columbia.material.morph.analysis.constants;

import java.util.Arrays;

public class SwahiliConstants extends LanguageConstants{

	public static String SWAHILI_TREE_TAGGER = "resources/tree-tagger";
	public static String SWAHILI_TREE_TAGGER_MODEL = "resources/tree-tagger/swahili-par-linux-3.2.bin";
		
	//Constructor
	public SwahiliConstants(){
		
		String[] MARKERS_STR = {"ya", "na", "kwa", "katika", "za", "ni", "cha", "kuwa", "hiyo", "kwenye", "tanzania", "salaam", 
								"wakati", "vya", "baada", "kutoka", "kama", "yake", "pamoja", "ili", "hilo", "hivyo", "zaidi", 
								"pia", "huu", "hayo", "kwamba", "sasa", "moja", "hii", "ambayo", "ambao", "lakini", "kufanya", 
								"ambapo", "kila", "kulia", "kushoto", "hizo", "huyo", "afrika", "baadhi", "kuhusu", "kupitia", 
								"yao", "hata", "kwanza", "hicho", "ambaye", "kati"};

		MARKERS = Arrays.asList(MARKERS_STR);
		
	}
	
	public Language getLanguage() {
		return Language.SWAHILI;
	}
	
}

