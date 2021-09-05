package edu.columbia.material.morph.analysis.constants;

public enum Num {
	
	SG("SG"),
	PL("PL"),
	NA("NA");
	
	private String value;
	
	Num(String value){
		this.value = value;
	}
	
	public String getValue(){
		return value;
	}
	
	public static Num getByValue(String value){
	    for(Num num : values()){
	        if(num.getValue().equals(value)){
	            return num;
	        }
	    }
	    return Num.NA;
	}
	
    @Override
    public String toString() {
        return value;
    }
	
}
