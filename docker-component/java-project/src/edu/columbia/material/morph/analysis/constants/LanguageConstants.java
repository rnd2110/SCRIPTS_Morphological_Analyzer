package edu.columbia.material.morph.analysis.constants;

import java.util.List;

public abstract class LanguageConstants {
	
	//String RESOURCES_DIRECTORY = "/Users/ramy/workspaces/columbia-workspace/ScriptsMorphologicalAnalyzer/resource/resources/";
	String RESOURCES_DIRECTORY = "resources/";

	public List<String> MARKERS;
	
	public String PATH_GRAMMAR = RESOURCES_DIRECTORY+getLanguage().getValue()+"-grammar.txt";
	public String PATH_SEGMENTATION = RESOURCES_DIRECTORY+getLanguage().getValue()+"-segmentation.txt";
	
	public String PATH_PROJECTION_MODEL_POS = RESOURCES_DIRECTORY+getLanguage().getValue()+"-pos.model";
	public String PATH_PROJECTION_MODEL_NUMBER = RESOURCES_DIRECTORY+getLanguage().getValue()+"-number.model";
	public String PATH_PROJECTION_MODEL_TENSE = RESOURCES_DIRECTORY+getLanguage().getValue()+"-tense.model";
	
	public String PATH_BROWN_CLUSTERS = RESOURCES_DIRECTORY+getLanguage().getValue()+".paths";
	
	public abstract Language getLanguage();

}
