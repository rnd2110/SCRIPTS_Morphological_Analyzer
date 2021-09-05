package edu.columbia.material.morph.ag.handlers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.columbia.material.morph.ag.models.SegmentedWord;
import edu.columbia.material.morph.analysis.constants.Language;

public class InductiveSegmenter {
	
	private Language language;
	private int segmentationThreshold = 2;
	private Map<String, Map<String, String>> morphs;
	private SegmentationReader segmentationReader;
	private List<String> markers;
	
	public InductiveSegmenter(Language language, String pycfgGrammarFile, String segmentationFile, String expression1, String expression2, List<String> markers){
		try{
			this.language = language;
			morphs = new GrammarMorphGenerator(pycfgGrammarFile).execute(expression1, expression2);
			segmentationReader = new SegmentationReader(segmentationFile);
			segmentationReader.execute();
			this.markers = markers;
		}catch(Exception e){
			//System.out.println("Error initializing segmentation modules!");
		}
	}
	
	public String segmentWordForMaterialBasic(boolean threeWay, String word, int index){

		if(word == null || word.equals("")){
			return null;
		}
		
		String originalWord = word;
		word = word.toLowerCase();
		if(markers.contains(word) || word.length()<= segmentationThreshold) {
			return "@@"+originalWord+"@@";
		}

		String answer = segmentationReader.getTraining().containsKey(word)?segmentationReader.getTraining().get(word):null;
		if(answer == null){
			double maxScore = -1;
			Set<String> segmentations =  new HashSet<String>();
			insertSplits(word, 2, segmentations);
			for(String segmentation : segmentations){
				String[] segments = (" "+segmentation+" ").split("\\+");
				String prefix = segments[0].trim();
				String stem = segments[1].trim();
				if(stem.length() < 2){
					continue;
				}
				String suffix = segments[2].trim();
				
				double score = 	(!segmentationReader.getPrefixes().containsKey(prefix)?0:
								(float)segmentationReader.getPrefixes().get(prefix)/segmentationReader.getPrefixCount())*
								(!segmentationReader.getStems().containsKey(stem)?0:
								(float)segmentationReader.getStems().get(stem)/segmentationReader.getStemCount())*
								(!segmentationReader.getSuffixes().containsKey(suffix)?0:
								(float)segmentationReader.getSuffixes().get(suffix)/segmentationReader.getSuffixCount());
			
				if(score>=maxScore){
					String segmentedPrefix = prefix.equals("")?"":morphs.get("Prefix").get(prefix);
					if(segmentedPrefix == null) {
						segmentedPrefix = "";
					}
					String segmentedSuffix= suffix.equals("")?"":morphs.get("Suffix").get(suffix);
					if(segmentedSuffix == null) {
						segmentedSuffix = "";
					}
					answer = segmentedPrefix+"@@"+stem+"@@"+segmentedSuffix;
					answer = answer.replaceAll("(^\\+|\\+$)", "");
					maxScore = score;
				}
			}
			if(maxScore == 0){
				answer = "@@"+word+"@@";
			}
		}

		if(threeWay) {
			String prefix = answer.replaceAll("^(.*)\\+?\\((.+)\\)\\+?(.*)$", "$1");
			String stem = answer.replaceAll("^(.*)\\+?\\((.+)\\)\\+?(.*)$", "$2");
			String suffix = answer.replaceAll("^(.*)\\+?\\((.+)\\)\\+?(.*)$", "$3");
			prefix = prefix.replaceAll("\\+", "");
			suffix = suffix.replaceAll("\\+", "");
			answer = prefix+"@@"+stem+"@@"+suffix;
		}
		
		if(language == Language.ENGLISH) {
			answer = answer.replaceAll("\\)\\+([abcefghijklmnopqtuvwxzABCEFGHIJKLMNOPQTUVWXZ])$", "$1)");
		}
		
		answer = restoreCasing(answer, originalWord);
		if (answer == null) {
			answer = "@@"+originalWord+"@@";
		}

		return answer;
	}
	
	private String restoreCasing(String segmentation, String originalWord) {
		try {
			int currentOriginalIndex = 0;
			for(int i=0; i<segmentation.length(); i++) {
				if(segmentation.charAt(i) == '+' || segmentation.charAt(i) == '@'){
					originalWord = originalWord.substring(0, currentOriginalIndex) + segmentation.charAt(i) + originalWord.substring(currentOriginalIndex);
				}
				currentOriginalIndex++;
			}
		}catch(Exception e) {
			return null;
		}
		return originalWord;
	}
	
	public SegmentedWord segmentWordForMaterial(boolean threeWay, String word, int index){
		if(word.contains("+") || word.contains("@")) {
			return new SegmentedWord(word, new ArrayList<String>(), word, new ArrayList<String>());
		}
		return new SegmentedWord(segmentWordForMaterialBasic(threeWay, word, index));
	}
	
	//Getting all the possible strings for a given number of splits (recursion)
	private void insertSplits(String word, int count, Set<String> solutions){
		//Count = 0? We are done! Add the current solution and return.
		if(count==0){ 
			solutions.add(word);
			return;
		}
		//Add a plus in all the possible places
		for(int i=0; i<=word.length(); i++){
			//Keep track of the current value for backtracking
			String current = word;
			//Construct the new split
			word=word.substring(0,i)+"+"+word.substring(i);
			//Call recursively with a decremented count
			insertSplits(word, count-1, solutions);
			//Backtracking
			word = current;
		}
		return;
	}

}
