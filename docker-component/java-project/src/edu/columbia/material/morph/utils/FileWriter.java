package edu.columbia.material.morph.utils;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashSet;
import java.util.List;

public class FileWriter {
	
	public static boolean writeFileFromString(String filePath, String content){
		try{
			Writer writer = new OutputStreamWriter(new FileOutputStream(filePath), "UTF-8");
			writer.write(content);
			writer.close();
			return true;
		}catch(Exception e){
			System.out.println(e.getMessage());
			return false;
		}
	}
	
	public static boolean writeFileFromList(String filePath, List<String> data){
		try{
			String content = "";
			for(String str : data){
				content+=str+"\n";
			}
			Writer writer = new OutputStreamWriter(new FileOutputStream(filePath), "UTF-8");
			writer.write(content);
			writer.close();
			return true;
		}catch(Exception e){
			System.out.println(e.getMessage());
			return false;
		}
	}
	
	public static boolean writeFileFromSet(String filePath, HashSet<String> data){
		try{
			String content = "";
			for(String str : data){
				content+=str+"\n";
			}
			Writer writer = new OutputStreamWriter(new FileOutputStream(filePath), "UTF-8");
			writer.write(content);
			writer.close();
			return true;
		}catch(Exception e){
			System.out.println(e.getMessage());
			return false;
		}
	}
}
