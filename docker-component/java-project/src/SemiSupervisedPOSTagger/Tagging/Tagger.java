package SemiSupervisedPOSTagger.Tagging;

import SemiSupervisedPOSTagger.Learning.AveragedPerceptron;
import SemiSupervisedPOSTagger.Structures.BeamElement;
import SemiSupervisedPOSTagger.Structures.IndexMaps;
import SemiSupervisedPOSTagger.Structures.InfoStruct;
import SemiSupervisedPOSTagger.Structures.Pair;
import SemiSupervisedPOSTagger.Structures.Sentence;
import SemiSupervisedPOSTagger.Structures.TaggingState;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;

/**
 * Created by Mohammad Sadegh Rasooli.
 * ML-NLP Lab, Department of Computer Science, Columbia University
 * Date Created: 1/13/15
 * Time: 12:41 PM
 * To report any bugs or problems contact rasooli@cs.columbia.edu
 */

public class Tagger {
    float bigramScore[][] ;
    float trigramScore[][][];
    public AveragedPerceptron perceptron;
    IndexMaps maps;
    public boolean useBeamSearch;
    public int beamSize;
    
    public Tagger(String modelPath) throws  Exception{
        //System.out.print("loading the model...");
        FileInputStream fos = new FileInputStream(modelPath);
        GZIPInputStream gz = new GZIPInputStream(fos);
        ObjectInputStream modelReader = new ObjectInputStream(gz);
        
        InfoStruct info = (InfoStruct) modelReader.readObject();
        info.beamSize = 20;
        this.perceptron=new AveragedPerceptron(info);
        this.maps=(IndexMaps) modelReader.readObject();
        int tagSize=perceptron.tagSize();
        int featSize=perceptron.featureSize();

        bigramScore = new float[tagSize][tagSize];
        trigramScore = new float[tagSize][tagSize][tagSize];

        for (int v = 0; v < perceptron.tagSize(); v++) {
            for (int u = 0; u < perceptron.tagSize(); u++) {
                bigramScore[u][v] = perceptron.score(v, featSize -3, u, true);
                for (int w = 0; w < tagSize; w++) {
                    int bigram = (w << 10) + u;
                    trigramScore[w][u][v] = perceptron.score(v, featSize -2, bigram, true);
                }
            }
        }
        this.useBeamSearch=info.useBeamSearch;
        this.beamSize=info.beamSize;
        
        //System.out.print("done!\n");
        if (!info.useBeamSearch) {
            //System.out.print("using Viterbi algorithm\n");
        } else {
            //System.out.print("using beam search algorithm with beam size: " + info.beamSize + "\n");
        }
    }
    
    public String[] tag(final String line,  final String delim, final boolean usePartialInfo) {
        Sentence sentence = new Sentence(line, maps, delim);
        int[] tags = tag(sentence, usePartialInfo);
        String[] output = new String[tags.length];
        for (int i = 0; i < tags.length; i++)
            output[i] = maps.reversedMap[tags[i]];
        return output;
    }

    public static int[] tag(final Sentence sentence, final AveragedPerceptron classifier, final boolean isDecode, final boolean useBeamSearch, final int beamSize, final boolean usePartialInfo) {
        return useBeamSearch ?
                BeamTagger.thirdOrder(sentence, classifier, isDecode,beamSize,usePartialInfo,null):Viterbi.thirdOrder(sentence, classifier, isDecode,null);
    }

    public  int[] tag(final Sentence sentence,  final boolean usePartialInfo) {
        return useBeamSearch ?
                BeamTagger.thirdOrder(sentence, perceptron, true,beamSize,usePartialInfo,this):Viterbi.thirdOrder(sentence, perceptron, true,this);
    }

    public  int[] tag(final Sentence sentence,  final boolean usePartialInfo, final boolean isDecode) {
        return useBeamSearch ?
                BeamTagger.thirdOrder(sentence, perceptron, isDecode,beamSize,usePartialInfo,this):Viterbi.thirdOrder(sentence, perceptron, isDecode,this);
    }
    
    public Pair<ArrayList<TaggingState>, TreeSet<BeamElement>> tagWithBeam(final Sentence sentence,  final boolean usePartialInfo) {
        return BeamTagger.thirdOrderWithBeam(sentence, perceptron, true, beamSize, usePartialInfo, this);
    }

    public  Pair<int[],Float> tagWithScore(final Sentence sentence,  final boolean usePartialInfo) {
        return useBeamSearch ?
                BeamTagger.thirdOrderWithScore(sentence, perceptron, true, beamSize, usePartialInfo, this):Viterbi.thirdOrderWithScore(sentence, perceptron, true,this);
    }

    public void tag(final String inputPath, final String outputPath,final String delim, final String scoreFile)throws Exception{
        BufferedReader reader=new BufferedReader(new FileReader(inputPath));
        BufferedWriter writer=new BufferedWriter(new FileWriter(outputPath));

        boolean putScore=false;
        BufferedWriter scoreWriter=null;
        if(scoreFile!=null && !scoreFile.equals("")) {
            putScore = true;
            scoreWriter=new BufferedWriter(new FileWriter(scoreFile));
        }
        
        int ln=0;
        String line;
        while((line=reader.readLine())!=null){
            ln++;
            if(ln%1000==0) {
                //System.out.print(ln+"...");
            }
            String[] flds=line.trim().replace("　","").split(" ");
            ArrayList<String> words=new ArrayList<String>(flds.length);
            for(int i=0;i<flds.length;i++){
                if(flds[i].trim().length()==0)
                    continue;
                words.add(flds[i].trim());
            }
            Sentence sentence=new Sentence(words,maps);
                      
            Pair<int[],Float> ts=tagWithScore(sentence,false);
            int[] t=ts.first;
            String[] tags = new String[t.length];
            for (int i = 0; i < tags.length; i++)
                tags[i] = maps.reversedMap[t[i]];
            
            StringBuilder output=new StringBuilder();
            for(int i=0;i<tags.length;i++){
                output.append(words.get(i)+delim+tags[i]+" ");
            }
            writer.write(output.toString().trim()+"\n");
            
            
            if(putScore) {
                float normalizedScore=  ts.second/tags.length;
                scoreWriter.write(normalizedScore+"\n");
            }
        }
        //System.out.print(ln+"\n");
        writer.flush();
        writer.close();
        if(putScore) {
            scoreWriter.flush();
            scoreWriter.close();
        }
    }
    
    public Map<String, Double>[] tagWithBeam(String text) throws Exception{
		ArrayList<String> words= new ArrayList<>(Arrays.asList(text.trim().replaceAll("\\s+"," ").split(" ")));
        Sentence sentence=new Sentence(words, maps);
        Pair<ArrayList<TaggingState>, TreeSet<BeamElement>> pairResult = tagWithBeam(sentence,false);
		ArrayList<TaggingState> beam = pairResult.first;
		TreeSet<BeamElement> elements = pairResult.second;
		int numberOfWords = beam.get(elements.last().beamNum).tags.length;
    		try {
			Iterator<BeamElement> itr1 = elements.descendingIterator();
			double topScore = elements.last().score;
			double minScore = 0;
			int index = beamSize -1;
			while(itr1.hasNext()) {
				double score = itr1.next().score;
				if(score <= 0) {
					break;
				}
				minScore = score;
				index --;
			}
			
			index++;
			
			int numOfSequences = beamSize - index;
			if(numOfSequences == 0) { //No positive-score sentences
				throw new Exception();
			}
			
			Iterator<BeamElement> itr2 = elements.descendingIterator();
			double[] normalizedScores = new double[numOfSequences];
			double softmaxDenominator = 0;
			index = numOfSequences-1;
			while(index >= 0) {
				double score = itr2.next().score;
				double normalizedScore = 1 - (topScore-score)/(topScore-minScore+1);
				normalizedScores[index] = normalizedScore;
				softmaxDenominator += Math.exp(normalizedScore);
				index --;
			}
	
			double[] softmaxPobs = new double[numOfSequences];
			double[] softmaxScores = new double[numOfSequences];
			double softmaxScoreSum = 0;
			for(int i=0; i<numOfSequences; i++) {
				softmaxPobs[i] = Math.exp(normalizedScores[i])/softmaxDenominator;
				softmaxScores[i] = Math.pow(2, i)*softmaxPobs[i];
				softmaxScoreSum += softmaxScores[i];
			}
			
			double[] normalizedSoftmaxProbs = new double[numOfSequences];
			for(int i=0; i<softmaxScores.length; i++) {
				normalizedSoftmaxProbs[i] = softmaxScores[i]/softmaxScoreSum;
			}
						
			Map<String, Double>[] tagProbabilities = new Map[numberOfWords];
			Iterator<BeamElement> itr3 = elements.descendingIterator();
			index = numOfSequences-1;
			while(index >= 0) {
				int beamNum = itr3.next().beamNum;
				double prob = normalizedSoftmaxProbs[index];
				int[] tags = beam.get(beamNum).tags;
				for(int j=0; j<numberOfWords; j++) {
					if(tagProbabilities[j] == null) {
						tagProbabilities[j] = new HashMap<String, Double>();
					}
					String tagStr = maps.reversedMap[tags[j]];
					tagProbabilities[j].put(tagStr, tagProbabilities[j].getOrDefault(tagStr, 0D)+prob);
				}
				index--;
			}
			
			Map<String, Double>[] sortedTagProbabilities = new Map[numberOfWords];
			for(int j=0; j<numberOfWords; j++) {
		        Map<String, Double> sortedProbs = tagProbabilities[j].entrySet().stream()
		                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
		                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));
				sortedTagProbabilities[j] = sortedProbs;
			}
	        return sortedTagProbabilities;
	    	}catch(Exception e) {
			Map<String, Double>[] tagProbabilities = new Map[numberOfWords];
			int beamNumber = elements.last().beamNum;
			int[] tags = beam.get(beamNumber).tags;
			for(int j=0; j<tags.length; j++) {
				tagProbabilities[j] = new HashMap<String, Double>();
				tagProbabilities[j].put( maps.reversedMap[tags[j]], 0.0001D);
			}
			return tagProbabilities;
	    	}
    }
    
    public String[] tag(String text) throws Exception{
		ArrayList<String> words= new ArrayList<>(Arrays.asList(text.trim().replaceAll("\\s+"," ").split(" ")));
        Sentence sentence=new Sentence(words, maps);
        Pair<int[],Float> ts=tagWithScore(sentence,false);
        int[] t=ts.first;
        String[] tags = new String[t.length];
        for (int i = 0; i < tags.length; i++) {
            tags[i] = maps.reversedMap[t[i]];
        }
        return tags;
    }

    public ArrayList<Pair<String[],Float>> getPossibleTagReplacements(Sentence sentence){
      ArrayList<Pair<int[],Float>> repls=  BeamTagger.getPossibleTagsByOneReplacement(sentence, perceptron, beamSize, this);
        ArrayList<Pair<String[],Float>> replacements=new ArrayList<Pair<String[],Float>>();
        for(Pair<int[],Float> rpl:repls) {
            String[] tags=new String[rpl.first.length];
            for(int i=0;i<rpl.first.length;i++){
                tags[i]=maps.reversedMap[rpl.first[i]];
            }
            replacements.add(new Pair<String[], Float>(tags,rpl.second));
        }
        return replacements;
    }
    
    public  void partialTag( final String inputPath, final String outputPath,final String delim,String scoreFile)throws Exception{
        BufferedReader reader=new BufferedReader(new FileReader(inputPath));
        BufferedWriter writer=new BufferedWriter(new FileWriter(outputPath));

        boolean putScore=false;
        BufferedWriter scoreWriter=null;
        if(scoreFile!=null && !scoreFile.equals("")) {
            putScore = true;
            scoreWriter=new BufferedWriter(new FileWriter(scoreFile));
        }
        
        int ln=0;
        String line;
        while((line=reader.readLine())!=null){
            line=line.trim();
            if(line.length()==0){
                writer.write("\n");
                continue;
            }
            ln++;
            if(ln%1000==0) {
                //System.out.print(ln+"...");
            }
            Sentence sentence=new Sentence(line,maps,delim);

            Pair<int[],Float> ts=tagWithScore(sentence,true);
            int[] t=ts.first;
            
            String[] tags = new String[t.length];
            for (int i = 0; i < tags.length; i++)
                tags[i] = maps.reversedMap[t[i]];

            StringBuilder output=new StringBuilder();
            for(int i=0;i<tags.length;i++){
                output.append(sentence.wordStrs[i]+delim+tags[i]+" ");
            }
            writer.write(output.toString().trim()+"\n");

            if(putScore) {
                float normalizedScore=  ts.second/tags.length;
                scoreWriter.write(normalizedScore+"\n");
            }
        }
        //System.out.print(ln+"\n");
        writer.flush();
        writer.close();

        if(putScore) {
            scoreWriter.flush();
            scoreWriter.close();
        }
    }


    public IndexMaps getMaps() {
        return maps;
    }
}
