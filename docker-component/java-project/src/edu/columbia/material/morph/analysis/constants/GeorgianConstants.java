package edu.columbia.material.morph.analysis.constants;

import java.util.Arrays;

public class GeorgianConstants extends LanguageConstants{
		
	//Constructor
	public GeorgianConstants(){
		
		String[] MARKERS_STR = { "და", "რომ", "არ", "ეს", "ამ", "თუ", "კი",  "უნდა", "ის", "არის", "რომელიც", "იყო", 
				"მაგრამ", "რა", "წლის", "შემდეგ", "მე", "ძალიან", "არა", "თუმცა", "ვერ", "ან", "ერთი", "მხოლოდ", "იმ", 
				"უფრო", "მათ", "მას", "ასევე", "სხვა", "აქვს", "ჩვენ", "ახალი", "ჩემი", "არც", "მიერ", "შორის", 
				"შესახებ", "უკვე", "კიდევ", "დღეს", "მის", "როგორ", "როცა", "თავის", "ასე", "რადგან", "სადაც", "ისე", 
				"ახლა", "მან", "წინ", "მაინც", "ორი", "გამო"};

		MARKERS = Arrays.asList(MARKERS_STR);
		
	}
	
	public Language getLanguage() {
		return Language.GEORGIAN;
	}
	
}

