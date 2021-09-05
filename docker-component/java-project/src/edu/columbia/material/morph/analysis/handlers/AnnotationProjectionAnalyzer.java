package edu.columbia.material.morph.analysis.handlers;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import SemiSupervisedPOSTagger.Tagging.Tagger;
import edu.columbia.material.morph.ag.handlers.InductiveSegmenter;
import edu.columbia.material.morph.ag.models.SegmentedWord;
import edu.columbia.material.morph.analysis.constants.BulgarianConstants;
import edu.columbia.material.morph.analysis.constants.EnglishConstants;
import edu.columbia.material.morph.analysis.constants.FarsiConstants;
import edu.columbia.material.morph.analysis.constants.GeorgianConstants;
import edu.columbia.material.morph.analysis.constants.KazakhConstants;
import edu.columbia.material.morph.analysis.constants.Language;
import edu.columbia.material.morph.analysis.constants.LanguageConstants;
import edu.columbia.material.morph.analysis.constants.LithuanianConstants;
import edu.columbia.material.morph.analysis.constants.POS;
import edu.columbia.material.morph.analysis.constants.PashtoConstants;
import edu.columbia.material.morph.analysis.constants.RegexConstants;
import edu.columbia.material.morph.analysis.constants.SomaliConstants;
import edu.columbia.material.morph.analysis.constants.SwahiliConstants;
import edu.columbia.material.morph.analysis.constants.TagalogConstants;
import edu.columbia.material.morph.analysis.models.AnalyzedWord;
import edu.columbia.material.morph.utils.TaggingUtils;

public class AnnotationProjectionAnalyzer extends Analyzer {
	
	private Language language;
	private InductiveSegmenter inductiveSegmenter;
	private Tagger posTagger;
	private Tagger numberTagger;
	private Tagger tenseTagger;
	
	private static double precision = 0.00001D;
	
	public AnnotationProjectionAnalyzer(Language language){
		try{
			this.language = language;
			LanguageConstants languageConstants = getLanguageConstants(language);
			inductiveSegmenter = new InductiveSegmenter(language, languageConstants.PATH_GRAMMAR, languageConstants.PATH_SEGMENTATION, RegexConstants.EXPRESSION1, RegexConstants.EXPRESSION2, languageConstants.MARKERS);
			posTagger = new Tagger(languageConstants.PATH_PROJECTION_MODEL_POS);
			numberTagger = new Tagger(languageConstants.PATH_PROJECTION_MODEL_NUMBER);
			tenseTagger = new Tagger(languageConstants.PATH_PROJECTION_MODEL_TENSE);
		} catch(Exception e){
			System.out.println("An unexpected error has occurred!");
		}
	}
	
	public List<List<AnalyzedWord>> execute(String inputFile, String encoding){
		List<List<AnalyzedWord>> analysess = new ArrayList<List<AnalyzedWord>>();
		try{
			String inputText = new String(Files.readAllBytes(Paths.get(inputFile)), encoding); 
			analysess = execute(inputText);
		}catch(Exception e){
			System.out.println("Error running the morphological analysis!");
		}
		return analysess;
	}
	
	public List<List<AnalyzedWord>> execute(String inputText){
		List<List<AnalyzedWord>> analysess = new ArrayList<List<AnalyzedWord>>();
		try {
			//Remove endings
			inputText = inputText.replaceAll("\\n\\r?$", "");
			//Sentence splits
			String[] sentences = inputText.split("\\n\\r?");
			
			int index=0;
			for(String sentence : sentences) {
				
				List<AnalyzedWord> sentenceAnalysis = new ArrayList<AnalyzedWord>();
				
				//White-space tokenization
				sentence = RuleBasedTokenizationHandler.tokenizeText(sentence);
				//sentence = TokenizationHandler.tokenizeText(language, sentence);
				sentence = sentence.trim();
				
				if(sentence.length() == 0) {
					analysess.add(sentenceAnalysis);
					continue;
				}
				
				//Word splits
				String[] words = sentence.split("\\s+");

				//Morphological analysis
				Map<String, Double>[] posMaps = posTagger.tagWithBeam(sentence);
				String[] pos = new String[posMaps.length];
				Map<POS, BigDecimal>[] adjustedPosMaps = new Map[posMaps.length];
				for(int j=0; j<posMaps.length; j++) {
					adjustedPosMaps[j] = new HashMap<POS, BigDecimal>();
					for(Entry<String, Double> entry : posMaps[j].entrySet()) {
						POS currentPOS = POS.getByValue(entry.getKey());
						POS newPOS = TaggingUtils.adjustPOS(words[j], entry.getKey());
						if(currentPOS != newPOS) {
							adjustedPosMaps[j] = new HashMap<POS, BigDecimal>();
							adjustedPosMaps[j].put(newPOS, new BigDecimal(1D).setScale(5, BigDecimal.ROUND_HALF_UP));
							break;
						}else if(entry.getValue() >= precision){
							adjustedPosMaps[j].put(currentPOS, new BigDecimal(entry.getValue()).setScale(5, BigDecimal.ROUND_HALF_UP));
						}
					}
					//Sort POS probabilities
					Map<POS, BigDecimal> sortedAdjustedPosMaps = adjustedPosMaps[j].entrySet().stream()
			                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
			                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));
					adjustedPosMaps[j] = sortedAdjustedPosMaps;
					pos[j] = adjustedPosMaps[j].entrySet().iterator().next().getKey().getValue();
				}
	
				String[] number = numberTagger.tag(sentence);
				String[] tense = tenseTagger.tag(sentence);
	
				for(int i=0; i<words.length; i++){
					String word = words[i];
					//Segmentation
					SegmentedWord segmentation = inductiveSegmenter.segmentWordForMaterial(false, word, i);
					AnalyzedWord analyzedWord = new AnalyzedWord();
					analyzedWord.setSegmentation(segmentation);
					analyzedWord.setWord(word);
					analyzedWord.setIndex(index++);
					analyzedWord.setPos(TaggingUtils.adjustPOS(words[i], pos[i]));
					analyzedWord.setPos_props(adjustedPosMaps[i]);
					analyzedWord.setNumber(TaggingUtils.adjustNumber(analyzedWord.getPos(), number[i]));
					analyzedWord.setTense(TaggingUtils.adjustTense(analyzedWord.getPos(), tense[i]));
					sentenceAnalysis.add(analyzedWord);
				}
				analysess.add(sentenceAnalysis);
			}
		}catch(Exception e){
			System.out.println("Error running the morphological analysis!");
		}
		return analysess;
	}
	
	private LanguageConstants getLanguageConstants(Language language){
		switch(language){
		case ENGLISH:
			return new EnglishConstants();
		case SWAHILI:
			return new SwahiliConstants();
		case TAGALOG:
			return new TagalogConstants();
		case SOMALI:
			return new SomaliConstants();
		case LITHUANIAN:
			return new LithuanianConstants();
		case BULGARIAN:
			return new BulgarianConstants();
		case PASHTO:
			return new PashtoConstants();
		case FARSI:
			return new FarsiConstants();
		case KAZAKH:
			return new KazakhConstants();
		case GEORGIAN:
			return new GeorgianConstants();
		default:
			return null;
		}
	}
	
}
