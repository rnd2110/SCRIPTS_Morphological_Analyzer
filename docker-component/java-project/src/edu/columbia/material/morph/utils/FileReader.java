package edu.columbia.material.morph.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FileReader {
	
	public static List<String> readFile(String filePath){
		return readFile(filePath, true, true, true);
	}
	
	@SuppressWarnings("resource")
	public static List<String> readFile(String filePath, boolean skipEmpty, boolean skipComments, boolean utf8){
		try{
			List<String> lines = new ArrayList<String>(); //To hold the file lines
			InputStream is = null;
			try{
				is = new FileInputStream(filePath);
			}catch(Exception e){
				is = new FileReader().getClass().getResourceAsStream(filePath);
			}
			
			BufferedReader br = new BufferedReader(utf8? new InputStreamReader(is, "utf-8") : new InputStreamReader(is));
	        String line;
	        while ((line = br.readLine()) != null){
	        	if(((skipEmpty && !line.equals("")) || !skipEmpty) && ((skipComments && !line.startsWith("//")) || !skipComments))
	        		lines.add(line);
	        }
            is.close();
            return lines;
		}catch(Exception e){
			System.out.println(e.getMessage());
			return null;
		}
	}
	
}
