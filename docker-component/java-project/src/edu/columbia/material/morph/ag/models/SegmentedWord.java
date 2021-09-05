package edu.columbia.material.morph.ag.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SegmentedWord {
	
	private String word = "";
	private List<String> prefixes = new ArrayList<String>();
	private String stem = "";
	private List<String> suffixes = new ArrayList<String>();

	public SegmentedWord() {
	}
	
	public SegmentedWord(String word, List<String> prefixes, String stem, List<String> suffixes) {
		this.word = word;
		this.prefixes = prefixes;
		this.stem = stem;
		this.suffixes = suffixes;
	}
	
	public SegmentedWord(String segmentedWord) {
		this.word = segmentedWord.replaceAll("[\\+\\@]", "");
		String prefixesStr = segmentedWord.replaceAll("^(.*)\\+?\\@\\@(\\S+)\\@\\@\\+?(.*)$", "$1");
		if(!prefixesStr.equals("")){
			this.prefixes = Arrays.asList(prefixesStr.split("\\+"));
		}
		this.stem = segmentedWord.replaceAll("^(.*)\\+?\\@\\@(\\S+)\\@\\@\\+?(.*)$", "$2");
		String suffixesStr = segmentedWord.replaceAll("^(.*)\\+?\\@\\@(\\S+)\\@\\@\\+?(.*)$", "$3");
		if(!suffixesStr.equals("")){
			this.suffixes = Arrays.asList(suffixesStr.split("\\+"));
		}
	}

	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}

	public List<String> getPrefixes() {
		return prefixes;
	}
	public void setPrefixes(List<String> prefixes) {
		this.prefixes = prefixes;
	}
	public String getPrefixesStr(){
		return getPrefixesStr("");
	}
	public String getPrefixesStr(String join){
		return String.join(join, prefixes);
	}
	
	public String getStem() {
		return stem;
	}
	public void setStem(String stem) {
		this.stem = stem;
	}
	
	public List<String> getSuffixes() {
		return suffixes;
	}
	public void setSuffixes(List<String> suffixes) {
		this.suffixes = suffixes;
	}
	public String getSuffixesStr(){
		return getSuffixesStr("");
	}
	public String getSuffixesStr(String join){
		return String.join(join, suffixes);
	}
	
	public String getPrefix(String searchRegex){
		if(getPrefixesStr().matches(searchRegex)){
			return getPrefixesStr();
		}
		return getAffix(prefixes, searchRegex);
	}
	
	public String getSuffix(String searchRegex){
		if(getSuffixesStr().matches(searchRegex)){
			return getSuffixesStr();
		}
		return getAffix(suffixes, searchRegex);
	}
		
	private String getAffix(List<String> affixes, String searchRegex){
		for(String affix : affixes){
			if(affix.matches(searchRegex)){
				return affix;
			}
		}
		return null;
	}
	
	public String getSegmentationStr(){
		return String.join("+", prefixes)+(prefixes.size()>0?"+":"")+"("+stem+")"+(suffixes.size()>0?"+":"")+String.join("+", suffixes);
	}

}
