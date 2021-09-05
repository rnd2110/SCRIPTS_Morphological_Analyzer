package edu.columbia.material.morph.analysis.constants;

import java.util.Arrays;

public class FarsiConstants extends LanguageConstants {
		
	//Constructor
	public FarsiConstants(){
				
		String[] MARKERS_STR = {"و", "در", "که", "است", "از", "با", "ایران", "آن", "بر", "یک", "هم", "خود", "باشد", "بود", "شود", 
								"برای", "یا", "نیز", "من", "دو", "هر", "آنها", "توسط", "تهران", "پس", "دیگر", "خواهد", "همه", "اگر", 
								"این", "شما", "بین", "اینکه", "چه", "دارای", "هستند", "باید", "یکی", "حال", "داشته", "سه"};
		
		MARKERS = Arrays.asList(MARKERS_STR);
		
	}
	
	public Language getLanguage() {
		return Language.FARSI;
	}
	
}

