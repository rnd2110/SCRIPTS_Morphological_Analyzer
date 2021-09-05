package edu.columbia.material.morph.analysis.main;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;

import edu.columbia.material.morph.analysis.constants.Language;
import edu.columbia.material.morph.analysis.models.AnalyzedWord;

public class Run {
	
	private static int MAAX_THREAD_SIZE = 5000;
	private static int THREAD_FACTOR = 7;
	
	private static String LANGUAGE_ERROR = "Error: Invalid language code! Use a case-insensitives code as follows: EN for English, TL for Tagalog, SW for Swahili, SO for Somali, LT for Lithuanian, BG for Bulgarian, PS for Pashto, FA for Farsi, KK for Kazakh or KA for Georgian.";
			
	public static void main(String args[]){
		try{
			if(args.length < 2){
				System.out.println("Error: Invalid parameters!");
				System.out.println("For running the Docker image, please refer to readme.txt.");
				System.out.println("For running the JAR, please use the commands below:");
				System.out.println("Usage: java -jar morph-analyzer.jar <language> <line>");
				System.out.println("Usage: java -jar morph-analyzer.jar <language> <input-file> <output-json-file>");
				System.out.println("Usage: java -jar morph-analyzer.jar <language> <input-directory> <output-json-directory>");
				System.out.println("Usage: java -jar morph-analyzer.jar <port> <language>");
				System.out.println("Usage: java -jar morph-analyzer.jar <port> <language> <line>");
				System.out.println("Usage: java -jar morph-analyzer.jar <port> <language> <input-file> <output-json-file>");
				System.out.println("Usage: java -jar morph-analyzer.jar <port> <language> <input-directory> <output-json-directory>");
				return;
			}

			int portNumber = -1;
			try {
				portNumber = Integer.parseInt(args[0]);
			}catch(Exception e) {
				portNumber = -1;
			}
			
			if(portNumber > 0) {
			
				String languageCode = args[1].toUpperCase();
				Language language = Language.getByValue(languageCode);
				if(language == null) {
					System.out.println(LANGUAGE_ERROR);
					return;
				}
				
				if(args.length>2) {
					
					String inputLine = "";
					List<File> inputFiles = new ArrayList<File>();
					List<String> outputFiles = new ArrayList<String>();
					
					if(args.length==3) {
						inputLine = args[2].trim();
					} else {
						String inputFile = args[2];
						String outputFile = args[3];
						if(!new File(inputFile).exists()){
							System.out.println("Error: Input file does not exist!");
							return;
						}
												
						//Loop over the files::::::
						
						if(new File(inputFile).isDirectory()) {
							File outputDirectory = new File(outputFile);
							if(!outputDirectory.exists()) {
								outputDirectory.mkdir();
								outputDirectory.setExecutable(true, false);
								outputDirectory.setReadable(true, false);
								outputDirectory.setWritable(true, false);
							}
							File[] directoryListing = new File(inputFile).listFiles();
							if(directoryListing == null) {
								System.out.println("Error: Invalid directory!");
								return;
							}
							for(File file : directoryListing) {
								if(file.getName().endsWith(".txt") || file.getName().endsWith(".text") || file.getName().endsWith(".top")) {
									inputFiles.add(file);
									outputFiles.add(outputFile+"/"+file.getName());
								}
							}
			
						}else {
							inputFiles.add(new File(inputFile));
							outputFiles.add(outputFile);
						}
					}
					
			        MorphologicalAnalysisClient client = new MorphologicalAnalysisClient("localhost", portNumber);

					if(args.length==3) {
			        		String output = client.analyzeLine(inputLine);
			        		System.out.println(output);
					}
						
					//Loop over the input files
					for(int i=0; i<inputFiles.size(); i++) {
			        		List<String> output = client.analyzeFile(inputFiles.get(i));
			    	        //Write the results
			    	       // System.out.println("Writing the output...");
			    			Writer writer = new OutputStreamWriter(new FileOutputStream(outputFiles.get(i)), StandardCharsets.UTF_8);
			    			BufferedWriter bufferedWriter = new BufferedWriter(writer);
			    			for(String text : output) {
			    				bufferedWriter.write(text+"\n");
			    			}
			    			bufferedWriter.close();
			    			writer.close();
					}
					
				}else {
					//Open the socket
					@SuppressWarnings("resource")
					ServerSocket analyzerSocket = new ServerSocket(portNumber);
					while(true) {
						Socket connectionSocket = analyzerSocket.accept();
						DataInputStream inFromClient = new DataInputStream(connectionSocket.getInputStream());
						DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
			            
						String text = "";
						int length = inFromClient.readInt(); // read length of incoming message
						if(length>0) {
						    byte[] message = new byte[length];
						    inFromClient.readFully(message, 0, message.length); // read the message  
						    text = new String(message, StandardCharsets.UTF_8);
						}
			            
			            if(text.replaceAll("\\s", "").length()>0) {
			            		//System.out.println("Running morphological analysis...");
			            		//Analyze the text.
			            		List<List<AnalyzedWord>> analyses = AnalyzerHolder.getAnalyses(language, text);
			            		ObjectMapper mapper = new ObjectMapper();
			            		String analysis = mapper.writeValueAsString(analyses);
			            		final byte[] utf8Bytes = analysis.getBytes("UTF-8");
			        			outToClient.writeInt(utf8Bytes.length);
			        			outToClient.write(analysis.getBytes(StandardCharsets.UTF_8));
			            }else {
			            		outToClient.writeUTF("[]"+"\n");
			            }				
					}
				}
				
			}else {
				String languageCode = args[0].toUpperCase();
				Language language = Language.getByValue(languageCode);
				if(language == null) {
					System.out.println(LANGUAGE_ERROR);
					return;
				}
				
				String inputLine = "";
				List<File> inputFiles = new ArrayList<File>();
				List<String> outputFiles = new ArrayList<String>();
				
				if(args.length==2) {
					inputLine = args[1].trim();
				} else {
					String inputFile = args[1];
					String outputFile = args[2];
					if(!new File(inputFile).exists()){
						System.out.println("Error: Input file does not exist!");
						return;
					}
											
					//Loop over the files::::::
					
					if(new File(inputFile).isDirectory()) {
						File outputDirectory = new File(outputFile);
						if(!outputDirectory.exists()) {
							outputDirectory.mkdir();
							outputDirectory.setExecutable(true, false);
							outputDirectory.setReadable(true, false);
							outputDirectory.setWritable(true, false);
						}
						File[] directoryListing = new File(inputFile).listFiles();
						if(directoryListing == null) {
							System.out.println("Error: Invalid directory!");
							return;
						}
						for(File file : directoryListing) {
							if(file.getName().endsWith(".txt") || file.getName().endsWith(".text") || file.getName().endsWith(".top")) {
								inputFiles.add(file);
								outputFiles.add(outputFile+"/"+file.getName());
							}
						}
		
					}else {
						inputFiles.add(new File(inputFile));
						outputFiles.add(outputFile);
					}
				}

				if(args.length==2) {
					if(inputLine.equals("")) {
						System.out.println("[]");
						return;
					}
					//Analyze the line
					List<List<AnalyzedWord>> analyses = AnalyzerHolder.getAnalyses(language, inputLine);
					//Write the analysis output
					ObjectMapper mapper = new ObjectMapper();
					String jsonOutput = mapper.writeValueAsString(analyses);
					System.out.println(jsonOutput);
					return;
				}
					
				//Loop over the input files
				
				List<Thread> threads = new ArrayList<Thread>();
				int threadSize = inputFiles.size() > MAAX_THREAD_SIZE? (inputFiles.size()/THREAD_FACTOR + 1) : inputFiles.size();
				for(int i=0;i<inputFiles.size();i+=threadSize) {
					//Split the list
					List<File> currentInputFiles = new ArrayList<File>(inputFiles.subList(i, Math.min(inputFiles.size(), i+threadSize)));
					List<String> currentOutputFiles = new ArrayList<String>(outputFiles.subList(i, Math.min(outputFiles.size(), i+threadSize)));
					//Start analysis thread
					Thread thread = new AnalysisThread(language, currentInputFiles, currentOutputFiles);
					threads.add(thread);
					thread.start();
				}
				
				for(Thread thread : threads){
					thread.join();
				}
				
			}

		}catch(Exception e){
			System.out.println("An unexpected error has occurred!");
		}
	}

}
