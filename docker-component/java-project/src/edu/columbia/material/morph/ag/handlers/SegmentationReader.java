package edu.columbia.material.morph.ag.handlers;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import edu.columbia.material.morph.utils.FileReader;

public class SegmentationReader {
	
	private String segmentationFile;
	
	private Map<String, String> training;
	private Map<String, Integer> prefixes;
	private Map<String, Integer> stems;
	private Map<String, Integer> suffixes;
	
	private int prefixCount = 0;
	private int stemCount = 0;
	private int suffixCount = 0;
	
	public SegmentationReader(String segmentationFile){
		this.segmentationFile = segmentationFile;
	}
	
	@SuppressWarnings("resource")
	public void execute(){
		try{
			training = new HashMap<String, String>();
			prefixes = new HashMap<String, Integer>();
			stems = new HashMap<String, Integer>();
			suffixes = new HashMap<String, Integer>();
			InputStream is;
			try{
				is = new FileInputStream(segmentationFile);
			}catch(Exception e){
				is = new FileReader().getClass().getResourceAsStream(segmentationFile);
			}
			BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			String line = null;
			while ((line = br.readLine()) != null) {
				line = line.trim();
				if(line.equals("")){
					continue;
				}
				
				training.put(line.replaceAll("[\\+\\@]", ""), line);
				String[] splits = (" "+line+" ").split("(\\@\\@)");
			
				String prefix = splits[0].replace("+",  "").trim();
				if(!prefixes.containsKey(prefix)){
					prefixes.put(prefix, 0);
				}
				prefixes.put(prefix, prefixes.get(prefix)+1);
				prefixCount++;
				
				String stem = splits[1].replace("+", "").trim();
				if(!stems.containsKey(stem)){
					stems.put(stem, 0);
				}
				stems.put(stem, stems.get(stem)+1);
				stemCount++;
				
				String suffix = splits[2].replace("+", "").trim();
				if(!suffixes.containsKey(suffix)){
					suffixes.put(suffix, 0);
				}
				suffixes.put(suffix, suffixes.get(suffix)+1);
				suffixCount++;
			}
			br.close();
			is.close();
		}catch(Exception e){
			System.out.println("Error reading the segmentations!");
		}
	}
	
	public Map<String, String> getTraining() {
		return training;
	}

	public Map<String, Integer> getPrefixes() {
		return prefixes;
	}
	
	public Map<String, Integer> getSuffixes() {
		return suffixes;
	}

	public Map<String, Integer> getStems() {
		return stems;
	}

	public int getPrefixCount() {
		return prefixCount;
	}
	
	public int getStemCount() {
		return stemCount;
	}

	public int getSuffixCount() {
		return suffixCount;
	}
	
}
