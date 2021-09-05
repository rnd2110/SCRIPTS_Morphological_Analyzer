package edu.columbia.material.morph.analysis.handlers;

import java.util.List;

import edu.columbia.material.morph.analysis.models.AnalyzedWord;

public abstract class Analyzer {
	
	public abstract List<List<AnalyzedWord>> execute(String inputText);

}
