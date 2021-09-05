package edu.columbia.material.morph.analysis.main;

import java.util.ArrayList;
import java.util.List;

import edu.columbia.material.morph.analysis.constants.Language;
import edu.columbia.material.morph.analysis.handlers.Analyzer;
import edu.columbia.material.morph.analysis.handlers.AnnotationProjectionAnalyzer;
import edu.columbia.material.morph.analysis.models.AnalyzedWord;

public class AnalyzerHolder {
	
	private static Analyzer englishAnalyzer;
	private static Analyzer swahiliAnalyzer;	
	private static Analyzer tagalogAnalyzer;
	private static Analyzer somaliAnalyzer;
	private static Analyzer lithuanianAnalyzer;
	private static Analyzer bulgarianAnalyzer;
	private static Analyzer pashtoAnalyzer;
	private static Analyzer farsiAnalyzer;
	private static Analyzer kazakhAnalyzer;
	private static Analyzer georgianAnalyzer;
	
	public static Analyzer getEnglishAnalyzer() {
		if(englishAnalyzer == null) {
			englishAnalyzer = new AnnotationProjectionAnalyzer(Language.ENGLISH);
		}
		return englishAnalyzer;
	}
	
	public static Analyzer getSwahiliAnalyzer() {
		if(swahiliAnalyzer == null) {
			swahiliAnalyzer = new AnnotationProjectionAnalyzer(Language.SWAHILI);
		}
		return swahiliAnalyzer;
	}
	
	public static Analyzer getTagalogAnalyzer() {
		if(tagalogAnalyzer == null) {
			tagalogAnalyzer = new AnnotationProjectionAnalyzer(Language.TAGALOG);
		}
		return tagalogAnalyzer;
	}
	
	public static Analyzer getSomaliAnalyzer() {
		if(somaliAnalyzer == null) {
			somaliAnalyzer = new AnnotationProjectionAnalyzer(Language.SOMALI);
		}
		return somaliAnalyzer;
	}
	
	public static Analyzer getLithuanianAnalyzer() {
		if(lithuanianAnalyzer == null) {
			lithuanianAnalyzer = new AnnotationProjectionAnalyzer(Language.LITHUANIAN);
		}
		return lithuanianAnalyzer;
	}
	
	public static Analyzer getBulgarianAnalyzer() {
		if(bulgarianAnalyzer == null) {
			bulgarianAnalyzer = new AnnotationProjectionAnalyzer(Language.BULGARIAN);
		}
		return bulgarianAnalyzer;
	}
	
	public static Analyzer getPashtoAnalyzer() {
		if(pashtoAnalyzer == null) {
			pashtoAnalyzer = new AnnotationProjectionAnalyzer(Language.PASHTO);
		}
		return pashtoAnalyzer;
	}

	public static Analyzer getFarsiAnalyzer() {
		if(farsiAnalyzer == null) {
			farsiAnalyzer = new AnnotationProjectionAnalyzer(Language.FARSI);
		}
		return farsiAnalyzer;
	}
	
	public static Analyzer getKazakhAnalyzer() {
		if(kazakhAnalyzer == null) {
			kazakhAnalyzer = new AnnotationProjectionAnalyzer(Language.KAZAKH);
		}
		return kazakhAnalyzer;
	}
	
	public static Analyzer getGeorgianAnalyzer() {
		if(georgianAnalyzer == null) {
			georgianAnalyzer = new AnnotationProjectionAnalyzer(Language.GEORGIAN);
		}
		return georgianAnalyzer;
	}
	
	//Getting analyses
	public static List<List<AnalyzedWord>> getAnalyses(Language language, String text) {
		List<List<AnalyzedWord>> analyses = new ArrayList<List<AnalyzedWord>>();
		if(language == Language.ENGLISH) {
			analyses = AnalyzerHolder.getEnglishAnalyzer().execute(text);
		}else if(language == Language.SWAHILI) {
			analyses = AnalyzerHolder.getSwahiliAnalyzer().execute(text);
		}else if(language == Language.TAGALOG) {
			analyses = AnalyzerHolder.getTagalogAnalyzer().execute(text);
		}else if(language == Language.SOMALI) {
			analyses = AnalyzerHolder.getSomaliAnalyzer().execute(text);
		}else if(language == Language.LITHUANIAN) {
			analyses = AnalyzerHolder.getLithuanianAnalyzer().execute(text);
		}else if(language == Language.BULGARIAN) {
			analyses = AnalyzerHolder.getBulgarianAnalyzer().execute(text);
		}else if(language == Language.PASHTO) {
			analyses = AnalyzerHolder.getPashtoAnalyzer().execute(text);
		}else if(language == Language.FARSI) {
			analyses = AnalyzerHolder.getFarsiAnalyzer().execute(text);
		}else if(language == Language.KAZAKH) {
			analyses = AnalyzerHolder.getKazakhAnalyzer().execute(text);
		}else if(language == Language.GEORGIAN) {
			analyses = AnalyzerHolder.getGeorgianAnalyzer().execute(text);
		}
		return analyses;
	}
	
}
