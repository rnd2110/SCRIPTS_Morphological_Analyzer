package edu.columbia.material.morph.analysis.constants;

import java.util.Arrays;

public class KazakhConstants extends LanguageConstants{
		
	//Constructor
	public KazakhConstants(){
		
		String[] MARKERS_STR = {"қазақстан", "және", "мен", "арналға", "бұл", "үшін", "деп", "туралы", "да", "бір", "осы",
								"ол", "жылы", "емес", "ал", "болды", "деген", "адам", "жоқ", "дейін", "немесе", "керек", 
								"ең", "барлық", "екі", "бірақ", "көп", "пен", "басқа", "біз", "не", "кейін", "ғана", "болса",
								"олар", "жүзеге", "біздің", "қандай", "әр"};

		MARKERS = Arrays.asList(MARKERS_STR);
		
	}
	
	public Language getLanguage() {
		return Language.KAZAKH;
	}
	
}

