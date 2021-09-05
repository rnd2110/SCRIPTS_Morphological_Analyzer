package edu.columbia.material.morph.analysis.main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MorphologicalAnalysisClient {
	
	private String host;		//Server name
	private int portNumber;	//Port Number

	//Constructor
	public MorphologicalAnalysisClient(String host, int portNumber) {
		try {
			this.host = host;
			this.portNumber = portNumber;		
		}catch(Exception e) {
			System.out.println("Error initializing the client!");
		}
	}
	
	//Analyzing a complete file
	public List<String> analyzeFile(File file) {
		return analyzeFile(file.getAbsolutePath());
	}
	
	//Analyzing a complete file
	public List<String> analyzeFile(String filePath) {
		List<String> fileAnalysis = new ArrayList<String>();
		InputStream inputStream = null;
		BufferedReader bufferedReader = null;
		try {
			//Check for the existence of the file
			File file = new File(filePath);
			if(!file.exists()) {
				System.out.println("Error: File does not exist!");
			}
			
			//Read the file line by line
			inputStream = new FileInputStream(filePath);
			bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
			String line = "";
			while ((line = bufferedReader.readLine()) != null) {
				//Analyze the current line
				line = line.trim();
				String analysis = analyzeLine(line);
				if(analysis == null) {
					throw new Exception();
				}else {
					//Record the analysis
					fileAnalysis.add(analysis);
				}
			}
		}catch(Exception e) {
			System.out.println("Error analyzing the file");
		}
		finally {
			try {
				bufferedReader.close();
				inputStream.close();
			}catch(Exception e) {}
		}
		if(fileAnalysis.size() == 0) {
			fileAnalysis.add("[]");
		}
		return fileAnalysis;
	}
	
	//Analyzing a single line
	public String analyzeLine(String line) {
		//Trivial cases
		if(line == null) {
			return "[]";
		}
		line = line.trim();
		if(line.length()==0) {
			return "[]";
		}
		
		//Setup connection
		Socket clientSocket =null;
		DataInputStream inFromServer = null;;
		DataOutputStream outToServer = null;
		try {
			clientSocket = new Socket(host, portNumber);
			inFromServer = new DataInputStream(clientSocket.getInputStream());
			outToServer = new DataOutputStream(clientSocket.getOutputStream());
			
			final byte[] utf8Bytes = line.getBytes("UTF-8");
			outToServer.writeInt(utf8Bytes.length);
	        outToServer.write(line.getBytes(StandardCharsets.UTF_8));
	        
			String lineAnalysis = "";
			int length = inFromServer.readInt(); // read length of incoming message
			if(length>0) {
			    byte[] message = new byte[length];
			    inFromServer.readFully(message, 0, message.length); // read the message  
			    lineAnalysis = new String(message, StandardCharsets.UTF_8);
			}
	        return lineAnalysis;
	        
		}catch(Exception e) {
			System.out.println("Error analyzing the line!");
		}
		finally {
			try {
				outToServer.close();
				inFromServer.close();
				clientSocket.close();
			}catch(Exception e) {}
		}
		return "[]";
	}

	//Getters and Setters

	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}

	public int getPortNumber() {
		return portNumber;
	}
	public void setPortNumber(int portNumber) {
		this.portNumber = portNumber;
	}
	
	//Main method
	public static void main(String args[]){
		try{
			
			String host = "";
			int portNumber = 0;
			String query = "";
			String inFilePath = "";
			String outFilePath = "";
			
			//Read the parameters
			try {
				
				host = args[0];
				portNumber = Integer.parseInt(args[1]);
				
				if(args.length==3) {
					query = args[2];
				}else if(args.length==4) {
					inFilePath = args[2];
					outFilePath = args[3];
				}else {
					throw new Exception();
				}

			}catch(Exception e) {
				System.out.println("Invalid arguments -- The arguments should be as follows:\nFor String Processing: 1) Host Name 2) Port Number 3) String \nFor File processing: 1) Host Name 2) Port Number 3) Input File Path 4) Output File Path");
				return;
			}
			
			//Run the analysis
			System.out.println("Running the analysis...");
	        MorphologicalAnalysisClient client = new MorphologicalAnalysisClient(host, portNumber);
	        
	        if(query.length()>0) {
	        		String output = client.analyzeLine(query);
	        		//Write the results
	        		System.out.println(output);
	        }else if(inFilePath.length()>0 && outFilePath.length()>0) {
	        		List<String> output = client.analyzeFile(inFilePath);
	    	        //Write the results
	    	        System.out.println("Writing the output...");
	    			Writer writer = new OutputStreamWriter(new FileOutputStream(outFilePath), StandardCharsets.UTF_8);
	    			BufferedWriter bufferedWriter = new BufferedWriter(writer);
	    			for(String text : output) {
	    				bufferedWriter.write(text+"\n");
	    			}
	    			bufferedWriter.close();
	    			writer.close();
	        }
	        
			System.out.println("Done!");
			
		}catch(Exception e){
			System.out.println("An unexpected error has occurred!");
		}
	}

}


