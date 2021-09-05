package edu.columbia.material.morph.analysis.constants;

import java.util.Arrays;
import java.util.List;

public class EnglishConstants extends LanguageConstants{
	
	public List<String> SINGULAR_WORDS;
	
	public List<String> PLURAL_WORDS;
		
	//Constructor
	public EnglishConstants(){
				
		String[] SINGULAR_WORDS_STR = {"i", "me", "myself", "you", "yourself", "he", "him", "himself", "she", "her", "herself", "this", "that"};
				
		String[] PLURAL_WORDS_STR = {"we", "us", "ourselves", "they", "them", "themselves", "these", "those"};
		
		String[] MARKERS_STR = {
				"i", "me", "my", "we", "our", "you", "your", "he", "him", "his", "she", "it", "they", "them", "their", 
				"what", "which", "who", "whom", "this", "that", "these", "those", "am", "is", "are", "was",
				"were", "be", "been", "have", "has", "had", "do", "did", "a", "an", "the", "and", "but", "if", 
				"or", "as", "until", "while", "of", "at", "by", "for", "with", "about", "against", "between",
				"through", "before", "after", "above", "below", "to", "from", "up", "down", "in", "out", "on", "off",
				"over", "under", "again", "then", "once", "here", "there", "when", "where", "why", "how", "all",
				"any", "both", "each", "few", "more", "most", "other", "some", "such", "no", "nor", "not", "only",
				"same", "so", "than", "too", "very", "can", "will", "just", "should", "now", "thus", "hence", "less",
			};
		
		MARKERS = Arrays.asList(MARKERS_STR);
		
		SINGULAR_WORDS = Arrays.asList(SINGULAR_WORDS_STR);
		
		PLURAL_WORDS = Arrays.asList(PLURAL_WORDS_STR);
		
		MARKERS = Arrays.asList(MARKERS_STR);
		
	}
	
	public Language getLanguage() {
		return Language.ENGLISH;
	}
	
}

