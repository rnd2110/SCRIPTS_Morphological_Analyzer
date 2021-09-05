package edu.columbia.material.morph.analysis.constants;

public enum Tense {
	
	PAST("PAST"),
	PRES("PRES"),
	FUT("FUT"),
	NA("NA");
	
	private String value;
	
	Tense(String value){
		this.value = value;
	}
	
	public String getValue(){
		return value;
	}
	
	public static Tense getByValue(String value){
	    for(Tense tense : values()){
	        if(tense.getValue().equals(value)){
	            return tense;
	        }
	    }
	    return Tense.NA;
	}
	
    @Override
    public String toString() {
        return value;
    }
	
}
