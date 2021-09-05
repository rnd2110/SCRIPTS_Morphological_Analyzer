package edu.columbia.material.morph.analysis.constants;

public enum AttachmentType {
	
	RIGHT("RGHT"),
	LEFT("LFT"),
	MIDDLE("MID");
   
	private String value;
		
	AttachmentType(String value){
		this.value = value;
	}
	
	public String getValue(){
		return value;
	}

}
