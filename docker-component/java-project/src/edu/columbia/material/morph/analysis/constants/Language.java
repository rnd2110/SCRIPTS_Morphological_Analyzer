package edu.columbia.material.morph.analysis.constants;

public enum Language {
	
	ENGLISH("EN"),
	SWAHILI("SW"),
	TAGALOG("TL"),
	SOMALI("SO"),
	LITHUANIAN("LT"),
	BULGARIAN("BG"),
	PASHTO("PS"),
	FARSI("FA"),
	KAZAKH("KK"),
	GEORGIAN("KA");
	
	private String value;
		
	Language(String value){
		this.value = value;
	}
	
	public String getValue(){
		return value;
	}
	
	public static Language getByValue(String value){
		if(value == null) {
			return Language.ENGLISH;
		}
		value = value.toUpperCase();
	    for(Language language : values()){
	        if(language.getValue().equals(value)){
	            return language;
	        }
	    }
	    return null;
	}

}
