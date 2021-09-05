package edu.columbia.material.morph.analysis.constants;

import java.util.Arrays;

public class LithuanianConstants extends LanguageConstants{
		
	//Constructor
	public LithuanianConstants(){
		
		String[] MARKERS_STR = {"ir", "kad", "į", "yra", "su", "tai", "iš", "kaip", "buvo", "tik", "taip", "ne", "apie", "dėl", "tačiau", "nuo", 
								"kai", "gali", "dar", "jau", "labai", "jis", "būti", "už", "to", "bei", "turi", "jei", "kas", "be", "jie", "nes", 
								"iki", "kurie", "mūsų", "daugiau", "todėl", "galima", "daug", "nors", "tiek", "arba", "prie", "prieš", "mes", "tuo", 
								"dabar", "ką", "čia", "kur", "jeigu", "pagal", "kuris", "aš", "kurios", "vienas", "turėtų", "visi", "kiek", 
								"tikrai", "juos", "tarp"};
		
		MARKERS = Arrays.asList(MARKERS_STR);
		
	}
	
	public Language getLanguage() {
		return Language.LITHUANIAN;
	}
	
}

