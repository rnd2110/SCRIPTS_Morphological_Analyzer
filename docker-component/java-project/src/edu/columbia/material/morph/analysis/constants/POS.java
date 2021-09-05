package edu.columbia.material.morph.analysis.constants;

public enum POS {
	
	ADJ("ADJ", 0),
	ADP("ADP", 1),
	ADV("ADV", 2),
	AUX("AUX", 3),
	CCONJ("CCONJ", 4),
	DET("DET", 5),
	INTJ("INTJ", 6),
	NOUN("NOUN", 7),
	NUM("NUM", 8),
	PART("PART", 9),
	PRON("PRON", 10),
	PROPN("PROPN", 11),
	PUNCT("PUNCT", 12),
	SCONJ("SCONJ", 13),
	SYM("SYM", 14),
	VERB("VERB", 15),
	X("X", 16);
	
	private String value;
	private int index;
		
	POS(String value, int index){
		this.value = value;
		this.index = index;
	}
	
	public String getValue(){
		return value;
	}
	
	public int getIndex(){
		return index;
	}
	
	
	public static POS getByValue(String value){
	    for(POS pos : values()){
	        if(pos.getValue().equals(value)){
	            return pos;
	        }
	    }
	    return POS.X;
	}
	
	
    @Override
    public String toString() {
        return value;
    }
	
}
