package edu.columbia.material.morph.ag.handlers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import edu.columbia.material.morph.ag.constants.Constants;
import edu.columbia.material.morph.utils.FileReader;

public class GrammarMorphGenerator {
	
	private String grammarFile;
	private Map<String, Map<String, String>> morphs;
	
	public GrammarMorphGenerator(String grammarFile){
		this.grammarFile = grammarFile;
	}
	
	public Map<String, Map<String, String>> execute(String expression1, String expression2){
		morphs = new HashMap<String, Map<String,String>>();
		morphs.put("Prefix", new HashMap<String,String>());
		morphs.put("Stem", new HashMap<String,String>());
		morphs.put("Suffix", new HashMap<String, String>());
		try{
			Map<String, Map<String, Map<String, Integer>>> morphStats = new HashMap<String, Map<String, Map<String, Integer>>>();
			morphStats.put("Prefix", new HashMap<String, Map<String, Integer>>());
			morphStats.put("Stem", new HashMap<String, Map<String, Integer>>());
			morphStats.put("Suffix", new HashMap<String, Map<String, Integer>>());

			List<String> lines = FileReader.readFile(grammarFile, true, false, true);
			for(String line : lines){
				if(line.contains(">"))
					continue;
				if(line.matches(expression1)){
					String[] outSplits = line.split("[\\s\\(\\)]+");
					StringBuilder builder = new StringBuilder();
					for(int i=0; i<outSplits.length; i++){
						String token = outSplits[i].trim().replaceAll("\\#\\d+", "");
						if(token.matches("([\\dabcdef]{4,8})+")){
							if(Constants.UTF_MAP.containsKey(token)){
								builder.append(Constants.UTF_MAP.get(token));
							}else {
								builder.append((char)Integer.parseInt(token, 16));
							}
						}else if(token.matches(expression2)){
							builder.append("+");
						}
					}
					String segmentedWord = builder.toString();	
					segmentedWord = segmentedWord.trim().replaceAll("\\++", "+").replaceAll("((^\\+)|(\\+$))", "");
					String unsegmentedWord = segmentedWord.replace("+", "");
					String key = "Prefix";
					if(line.startsWith("(Stem")){
						key = "Stem";
					}
					if(line.startsWith("(Suffix")){
						key = "Suffix";
					}
					if(!morphStats.get(key).containsKey(unsegmentedWord)){
						morphStats.get(key).put(unsegmentedWord, new HashMap<String, Integer>());
					}
					if(morphStats.get(key).containsKey(unsegmentedWord) && !morphStats.get(key).get(unsegmentedWord).containsKey(segmentedWord)){
						morphStats.get(key).get(unsegmentedWord).put(segmentedWord, 0);
					}
					morphStats.get(key).get(unsegmentedWord).put(segmentedWord, morphStats.get(key).get(unsegmentedWord).get(segmentedWord)+1);
				}
			}
			
			for(Entry<String, Map<String, Integer>> entry1 : morphStats.get("Prefix").entrySet()){
				int maxfrequency = -1;
				String selected = "";
				for(Entry<String, Integer> entry2 : morphStats.get("Prefix").get(entry1.getKey()).entrySet()){
					if(entry2.getValue() > maxfrequency){
						maxfrequency = entry2.getValue();
						selected = entry2.getKey();
					}
				}
				morphs.get("Prefix").put(entry1.getKey(), selected);
			}
			
			for(Entry<String, Map<String, Integer>> entry1 : morphStats.get("Stem").entrySet()){
				for(Entry<String, Integer> entry2 : morphStats.get("Stem").get(entry1.getKey()).entrySet()){
					morphs.get("Stem").put(entry1.getKey(), entry2.getKey());
					break;
				}
			}
			
			for(Entry<String, Map<String, Integer>> entry1 : morphStats.get("Suffix").entrySet()){
				int maxfrequency = -1;
				String selected = "";
				for(Entry<String, Integer> entry2 : morphStats.get("Suffix").get(entry1.getKey()).entrySet()){
					if(entry2.getValue() > maxfrequency){
						maxfrequency = entry2.getValue();
						selected = entry2.getKey();
					}
				}
				morphs.get("Suffix").put(entry1.getKey(), selected);
			}
		
		}catch(Exception e){
			System.out.println("Error generating the grammar rules!");
		}
		return morphs;
	}

	public Map<String, Map<String, String>> getRules() {
		return morphs;
	}

}
