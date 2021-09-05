package edu.columbia.material.morph.analysis.main;

import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.columbia.material.morph.ag.constants.Constants;
import edu.columbia.material.morph.analysis.constants.Language;
import edu.columbia.material.morph.analysis.models.AnalyzedWord;

public class AnalysisThread extends Thread {
		
	private Language language;
	private List<File> inputFiles;
	private List<String> outputFiles;
	
	public AnalysisThread(Language language, List<File> inputFiles, List<String> outputFiles) {
		this.language = language;
		this.inputFiles = inputFiles;
		this.outputFiles = outputFiles;
	}

	public void run() {
		try {
			for(int i=0; i<inputFiles.size(); i++) {
				PrintWriter pw = new PrintWriter(outputFiles.get(i));
				
				//Read the files, one by one
				//System.out.println("Reading: "+inputFiles.get(i));
				String fileContent = new String(Files.readAllBytes(Paths.get(inputFiles.get(i).getAbsolutePath())));
				List<String> lines = new ArrayList<String>();
				List<Language> languages = new ArrayList<Language>();
				
				//Read language-identification output
				boolean processed = false;
				if(isValidJson(fileContent)) {
					try {
						@SuppressWarnings("unchecked")
						HashMap<String,Object> result = new ObjectMapper().readValue(fileContent, HashMap.class);
						@SuppressWarnings("unchecked")
						List<Object> map = (ArrayList<Object>)result.get("sentences");
						for(int mapIndex=0; mapIndex<map.size(); mapIndex++) {
							@SuppressWarnings({ "unchecked", "rawtypes" })
							List<LinkedHashMap<String, String>> items = ((LinkedHashMap<String, ArrayList>)map.get(mapIndex)).get("items");
							String line = "";
							@SuppressWarnings("unchecked")
							String langaugeCode = ((LinkedHashMap<String, String>)map.get(mapIndex)).get("languageCode").toUpperCase();
							if(Constants.UTF_MAP.containsKey(langaugeCode)){
								langaugeCode = Constants.UTF_MAP.get(langaugeCode);
							}
							Language language = Language.getByValue(langaugeCode);
							for(int j=0; j<items.size(); j++) {
								LinkedHashMap<String, String> map2 = items.get(j);
								line+=map2.get("token")+(j<items.size()-1?" ":"");
							}
							lines.add(line.trim());
							languages.add(language);
						}
						processed = true;
					}catch(Exception e) {}
				//Read plain text
				}
				if(!processed) {
					lines = Arrays.asList(fileContent.split("\\n", -1));
					for(int s=0; s<lines.size(); s++) {
						languages.add(language);
					}
				}

				//Analyze the file
				//System.out.println("Analyzing: "+inputFiles.get(i));
				for(int j=0; j<lines.size(); j++) {
					String currentLine = lines.get(j).trim();
					Language currentLanguage = languages.get(j);

					//Analyze the line
					if(currentLine.equals("")) {
						pw.println("[]");
					}else {
						//System.out.println(inputFiles.get(i) + "\t" + currentLine);
						List<List<AnalyzedWord>> analyses = AnalyzerHolder.getAnalyses(currentLanguage, currentLine);
						//Write the analysis output
						try {
						ObjectMapper mapper = new ObjectMapper();
						String jsonOutput = mapper.writeValueAsString(analyses);
						pw.println(jsonOutput);
						}catch(Exception e) {
							//System.out.println(e.getMessage());
							System.out.println(analyses);
						}
					}
				}
				pw.close();
				File file = new File(outputFiles.get(i));
				file.setExecutable(true, false);
		        file.setReadable(true, false);
		        file.setWritable(true, false);
			}
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public boolean isValidJson(String test) {
	    try {
	        new JSONObject(test);
	    } catch (JSONException ex) {
	        try {
	            new JSONArray(test);
	        } catch (JSONException ex1) {
	            return false;
	        }
	    }
	    return true;
	}

}
