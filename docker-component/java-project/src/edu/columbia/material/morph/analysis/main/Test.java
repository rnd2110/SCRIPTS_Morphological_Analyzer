package edu.columbia.material.morph.analysis.main;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.codehaus.jackson.map.ObjectMapper;

import edu.columbia.material.morph.analysis.constants.Language;
import edu.columbia.material.morph.analysis.models.AnalyzedWord;

public class Test {

	public static void main(String[] args) {
		try {
			/*
			String REGEX_NON_LETTER =  "([\\_\\\"\\“\\”\\‘\\’\\``\\′\\՛\\.\\·\\.\\ㆍ\\•\\۔\\٫\\,\\、\\;\\:\\?\\？\\!\\[\\]\\{\\}\\(\\)\\|\\«\\»\\…\\،\\٬\\؛\\؟\\¿\\፤\\፣\\።\\፨\\፠\\፧\\፦\\፡\\…\\।\\¡\\「\\」\\《\\》\\』\\『\\〔\\〕\\\\\\–\\—\\−\\„\\‚\\´\\〉\\〈\\【\\】\\（\\）\\~\\。\\○\\．\\♪\\‹\\<\\>\\*\\/\\+\\-\\=\\≠\\%\\$\\£\\€\\¥\\۩\\#\\°\\@\\٪\\≤\\≥\\^\\φ\\θ\\×\\✓\\✔\\△\\©])";
			String str = "\u00A09/9/2013 dr. 내생ӯ9éabbbb\r...!,\u200f‡•§هو ده اللي حلمت بيه؟ أيوة4.5 ، يا معلم١٢.٣";
			String[] tokens = str.split("(?<=[\\s\\p{N}(\\p{Punct}])|(?=[\\s\\p{N}\\p{Punct}]|"+REGEX_NON_LETTER+")");
			String out1 = String.join(" ", tokens);
			String out2 = RuleBasedTokenizationHandler.tokenizeText(Language.FARSI, str);
			System.out.println(out1);
			System.out.println(out2);
			System.out.println(str.replaceAll("\\s+", "").length()+"\t"+out1.replaceAll("\\s+", "").length()+"\t"+out2.replaceAll("\\s+", "").length());
			*/
			
			/*
			String directory = "/Users/ramy/workspaces/columbia-workspace/ScriptsMorphologicalAnalyzer/umd-smt-v3.5_sent-split-v5.0";
			File dir = new File(directory);
			for(File file : dir.listFiles()) {
				if(file.getName().endsWith(".txt")) {
					System.out.println(file.getName());
					String fileContent = new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())));
					List<String> lines = Arrays.asList(fileContent.split("\\n", -1));
					for(String line : lines) {
						List<List<AnalyzedWord>> analyses = AnalyzerHolder.getAnalyses(Language.ENGLISH, line);
						ObjectMapper mapper = new ObjectMapper();
						String jsonOutput = mapper.writeValueAsString(analyses);
						System.out.println(jsonOutput);
					}
				}
			}
			*/

			Map<Language, String> testMap = new HashMap<Language, String>();
			
			testMap.put(Language.ENGLISH, "a_b I'm Reading many boOks that are Compplicated but exciting. I PURCHASED them in the past he's we're I'm you'll can't let's hadn't i've!");
			/*
			testMap.put(Language.SWAHILI, "Nilinunua vitabu vingi ambavyo ni ngumu lakini vya kufurahisha!");
			testMap.put(Language.TAGALOG, "Bumili ako ng maraming libro na kumplikado ngunit kapanapanabik!");
			testMap.put(Language.SOMALI, "Ardaydu waxay kubad ku ciyaarayaan dhamaan jidadka waxayna cunayaan raashin badan iyo tufaax, ciidanka jirka, kasaaro kajir!");
			testMap.put(Language.PASHTO, "ما ډیری کتابونه اخیستي چې پیچلي مګر په زړه پوري دي!");
			testMap.put(Language.BULGARIAN, "Купих много книги, които са сложни, но вълнуващи!");
			testMap.put(Language.LITHUANIAN, "Aš įsigijau naują automobilį, todėl dabar turiu tris automobilius. Ar tai ne nuostabu? išskirtas");
			testMap.put(Language.FARSI, "پس قوم درروز هفتمین آرام گرفتند.");
			testMap.put(Language.KAZAKH, "Студенттер барлық көшеде футбол ойнап, көптеген тағамдар мен алма жеп жатыр.");
			*/
			testMap.put(Language.GEORGIAN, "სკოლები სავსეა სტუდენტებით საქართველოს მრ_ავალ სფეროში_!");
			
			//String fileContent = new String(Files.readAllBytes(Paths.get("/Users/ramy/MATERIAL_OP2-3S_26076836_old.txt"))) + "\n\u200c\u0640\ufe0f make’s make's |@||@@||@@@||@@@@||@@@@@||@@@@@| |@@@|";
			for(Entry<Language, String> entry : testMap.entrySet()) {
				Language currentLanguage = entry.getKey();
				String currentLine = entry.getValue();
				System.out.println(entry.getKey()+"\t"+currentLine);
				//Analyze the line
				if(currentLine.equals("")) {
					System.out.println("[]");
				}else {
					List<List<AnalyzedWord>> analyses = AnalyzerHolder.getAnalyses(currentLanguage, currentLine);
					//Write the analysis output
					try {
						ObjectMapper mapper = new ObjectMapper();
						String jsonOutput = mapper.writeValueAsString(analyses);
						System.out.println(jsonOutput);
					}catch(Exception e) {
						//System.out.println(e.getMessage());
						System.out.println(analyses);
					}
				}
			}

		}catch(Exception e) {
			System.out.println("An unexpected error has occurred!");
		}
	}
	
	private static String restoreCasing(String segmentation, String originalWord) {
		int currentOriginalIndex = 0;
		for(int i=0; i<segmentation.length(); i++) {
			if(segmentation.charAt(i) == '+' || segmentation.charAt(i) == '@'){
				originalWord = originalWord.substring(0, currentOriginalIndex) + segmentation.charAt(i) + originalWord.substring(currentOriginalIndex);
			}
			currentOriginalIndex++;
		}
		return originalWord;
	}

}
