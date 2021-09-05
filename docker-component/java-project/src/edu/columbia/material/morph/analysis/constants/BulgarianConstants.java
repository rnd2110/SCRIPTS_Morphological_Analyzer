package edu.columbia.material.morph.analysis.constants;

import java.util.Arrays;

public class BulgarianConstants extends LanguageConstants{
		
	//Constructor
	public BulgarianConstants(){
		String[] MARKERS_STR = {"на", "и", "в", "да", "е", "за", "от", "се", "не", "че", "са", "по", "ще", "това", "като", "но", 
								"които", "а", "до", "той", "през", "към", "може", "след", "който", "много", "трябва", "те", "му", 
								"или", "ако", "само", "при", "което", "още", "българия", "бъде", "която", "го", "всички", "ни", "така", 
								"тези", "един", "защото", "им", "във", "със", "една", "тя", "когато", "вече", "както", "ги", "повече", 
								"преди", "бъдат", "ли", "ние", "около", "тях", "сега", "него", "между", "без", "срещу", "аз", "какво", 
								"как", "обаче", "ми"};

		MARKERS = Arrays.asList(MARKERS_STR);
		
	}
	
	public Language getLanguage() {
		return Language.BULGARIAN;
	}
	
}

