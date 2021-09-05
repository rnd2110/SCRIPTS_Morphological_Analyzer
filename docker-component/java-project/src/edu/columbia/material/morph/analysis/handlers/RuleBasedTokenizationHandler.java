package edu.columbia.material.morph.analysis.handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RuleBasedTokenizationHandler {
	
	private static String REGEX_PUNC =  "([\\_\\\"\\“\\”\\‘\\’\\``\\′\\՛\\.\\·\\.\\ㆍ\\•\\۔\\٫\\,\\、\\;\\:\\?\\？\\!\\[\\]\\{\\}\\(\\)\\|\\«\\»\\…\\،\\٬\\؛\\؟\\¿\\፤\\፣\\።\\፨\\፠\\፧\\፦\\፡\\ː\\…\\।\\¡\\「\\」\\《\\》\\』\\『\\〔\\〕\\\\\\–\\—\\−\\„\\‚\\´\\〉\\〈\\【\\】\\（\\）\\~\\。\\○\\．\\♪\\‹\\<\\>\\*\\/\\+\\-\\=\\≠\\%\\$\\£\\€\\¥\\۩\\#\\°\\@\\٪\\≤\\≥\\^\\φ\\θ\\×\\✓\\✔\\△\\©])";
	private static String REGEX_DIGIT = "[\\d٠١۲٢٣٤٥٦٧٨٩۴۵۶౦౧౨౩౪౫౬౭౮౯፲፳፴፵፶፷፸፹፺፻०१२३४५६७८९४零一二三四五六七八九十百千万億兆つ]";
	private static String REGEX_NUMBER_DATE = "^"+REGEX_DIGIT +"+(([\\.\\,\\٫\\:\\-\\/\\\\])?"+REGEX_DIGIT+")*$";
	private static String REGEX_SPLIT = "(?<=[\\s\\p{N}(\\p{Punct}])|(?=[\\s\\p{N}\\p{Punct}]|"+REGEX_PUNC+")";
	private static String REGEX_ABBREVIATIONS = "^(?i)(U\\.S\\.|U\\.S\\.A\\.|U\\.K\\.|U\\.A\\.E\\.|MR\\.|MS\\.|MRS\\.|DR\\.|PROF\\.|PH\\.D|PH\\.D\\.|ETC\\.|E\\.G\\.|((I)(\\'M|\\'VE))|((HE|SHE|IT)\\'S)|((YOU|WE|THEY)(\\'RE|\\'VE))|((I|HE|SHE|IT|YOU|WE|THEY)(\\'D|\\'LL))|((DO|WO|HAVE|HAS|HAD|CA|COULD|SHOULD|WOULD|AI)N\\'T)|LET\\'S)$";
	private static String REGEX_SPACES = "[\\s\u00A0\u1680]+";
	
	public static String tokenizeText(String text) {
		try {
			String[] tokens = text.split(REGEX_SPACES);
			List<String> tokenizedTokens = new ArrayList<String>();
			for(String token : tokens) {
				if(token.length() == 0) {
					continue;
				}
				if(token.contains("_")) {
					tokenizedTokens.add(token);
				}else if(token.toUpperCase().matches(REGEX_ABBREVIATIONS)) {
					tokenizedTokens.add(token);
				}else if(token.matches(REGEX_NUMBER_DATE)) {
					tokenizedTokens.add(token);
				}else {
					boolean start = false;;
					int middleIndex = -1;
					int maxLength = -1;
					for(int i=0; i<=token.length() ; i++) {
						String prefix = token.substring(0, i);
						String suffix  = token.substring(i);
						if(suffix.matches(REGEX_NUMBER_DATE) && suffix.length() > maxLength){
							middleIndex = i;
							maxLength = suffix.length();
							start = false;
						}else if(prefix.matches(REGEX_NUMBER_DATE) && prefix.length() > maxLength){
							middleIndex = i;
							maxLength = prefix.length();
							start = true;
						}
					}
					if(maxLength > 0) {
						String prefix = token.substring(0, middleIndex);
						String suffix = token.substring(middleIndex);
						if(!start) {
							tokenizedTokens.add(String.join(" ", prefix.split(REGEX_SPLIT)));	
							tokenizedTokens.add(suffix);
						}else {
							tokenizedTokens.add(prefix);
							tokenizedTokens.add(String.join(" ", suffix.split(REGEX_SPLIT)));	
						}
					}else {
						tokenizedTokens.add(String.join(" ", token.split(REGEX_SPLIT)));	
					}
				}
			}
			
			StringBuilder sb = new StringBuilder();
			for(int i=0; i<tokenizedTokens.size(); i++) {
				sb.append(tokenizedTokens.get(i)+(i<tokenizedTokens.size()?" ":""));
			}
			return sb.toString();
	       	
		}catch(Exception e){
			System.out.println(e.getMessage());
			return text;
		}
	}
}
