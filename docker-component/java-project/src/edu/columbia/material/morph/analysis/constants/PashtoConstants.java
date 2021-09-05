package edu.columbia.material.morph.analysis.constants;

import java.util.Arrays;

public class PashtoConstants extends LanguageConstants{
		
	//Constructor
	public PashtoConstants(){
		
		String[] MARKERS_STR = {"چې", "او", "د", "چي", "چې", "يي", "کي", "کې", "ته", "نه", "په", "سره", "هم", "دي", 
								"دې", "او", "دا", "دی", "به", "یې", "هغه", "تر", "یو", "دغه", "يې", "اپغانستان", "افغانستان", 
								"څخه", "خپل", "و", "يوه", "یوه", "شوې", "اپغان", "افغان", "کړي", "کړې", "څه", "وروسته", "نو", 
								"شوي", "شوی", "وو", "کړی", "الله", "وه", "دى", "داسي", "داسې", "هر", "نور", "نورو", "اوس", "څو", 
								"ټولو", "بل", "ښه", "كې", "دوه", "دوي", "دوی", "ټول", "ډير", "ډېر", "بې", "يا"};

		MARKERS = Arrays.asList(MARKERS_STR);
		
	}
	
	public Language getLanguage() {
		return Language.PASHTO;
	}
	
}

