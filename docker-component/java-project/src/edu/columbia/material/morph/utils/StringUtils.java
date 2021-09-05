package edu.columbia.material.morph.utils;

public class StringUtils {

	public static String REGEX_NON_LETTER =  "([\\_\\\"\\“\\”\\‘\\’\\``\\′\\՛\\.\\·\\.\\ㆍ\\•\\۔\\٫\\,\\、\\;\\:\\?\\？\\!\\[\\]\\{\\}\\(\\)\\|\\«\\»\\…\\،\\٬\\؛\\؟\\¿\\፤\\፣\\።\\፨\\፠\\፧\\፦\\፡\\…\\।\\¡\\「\\」\\《\\》\\』\\『\\〔\\〕\\\\\\–\\—\\−\\„\\‚\\´\\〉\\〈\\【\\】\\（\\）\\~\\。\\○\\．\\♪\\‹\\<\\>\\*\\/\\+\\-\\=\\≠\\%\\$\\£\\€\\¥\\۩\\#\\°\\@\\٪\\≤\\≥\\^\\φ\\θ\\×\\✓\\✔\\△\\©\\☺\\♥\\❤\\❤️\\💕\\💋\\😍\\😂\\😉\\😊\\😔\\👍\\😘\\😁\\🤔\\😃\\😄\\🙈\\😱\\☝\\🙏\\👏])";
	public static String REGEX_LETTER = REGEX_NON_LETTER.replace("([", "([^\\s");
			
	public static boolean hasRepitition(String str){
		if(str == null){ return false; }
		return str.matches("(^.*?)(.)\\2{1,}(.*)$");
	}
	
	public static String removeRepitition(String str){
		if(str== null){ return null; }
		return str.replaceAll("(^.*?)(.)\\2{1,}(.*)$", "$1$2$3");
	}
	
	public static boolean isNumber(String str){
		if(str == null) { return false; }
		return str.matches("\\.?[\\d٠١۲٢٣٤٥٦٧٨٩۴۵۶౦౧౨౩౪౫౬౭౮౯፲፳፴፵፶፷፸፹፺፻०१२३४५६७८९४零一二三四五六七八九十百千万億兆つ]+(([\\.\\,\\٫\\:\\-\\/\\\\])?[\\d٠١۲٢٣٤٥٦٧٨٩۴۵۶౦౧౨౩౪౫౬౭౮౯፲፳፴፵፶፷፸፹፺፻०१२३४५६७८९४零一二三四五六七八九十百千万億兆つ])*");
	}

	public static boolean isSymbol(String str){
		if(str == null) { return false; }
		if(str.matches("^((\\:[\\(\\)dDoOpP])|(\\;[\\(\\)])|m²)$")) {
			return true;
		}
		return str.matches("[\\+\\=\\≠\\%\\$\\£\\€\\¥\\۩\\#\\°\\@\\٪\\≤\\≥\\^\\φ\\θ\\×\\✓\\✔\\△\\©\\☺\\♥\\❤\\❤️\\💕\\💋\\😍\\😂\\😉\\😊\\😔\\👍\\😘\\😁\\🤔\\😃\\😄\\🙈\\😱\\☝\\🙏\\👏]");
	}
	
	public static boolean isPuncInTraining(String str){
		if(str == null) { return false; }
		return str.matches("[\\\\\\_\\\"\\“\\”\\‘\\’\\``\\′\\՛\\.\\·\\.\\ㆍ\\•\\۔\\٫\\,\\、\\;\\:\\?\\？\\!\\[\\]\\{\\}\\(\\)\\|\\«\\»\\…\\٬\\،\\؛\\؟\\¿\\፤\\፣\\።\\፨\\፠\\፧\\፦\\፡\\…\\।\\¡\\「\\」\\《\\》\\』\\『\\‹\\〔\\〕\\>\\<\\^\\*\\/\\+\\-\\–\\—\\−\\‚\\´\\'\\〉\\〈 \\【\\】\\（\\）\\~\\。\\○\\．\\♪]+");
	}
	
	public static boolean isPunc(String str){
		if(str == null) { return false; }
		//str.matches("(\\p{Punct})+")
		return str.matches("[\\\\\\_\\\"\\“\\”\\‘\\’\\``\\′\\՛\\.\\·\\.\\ㆍ\\•\\۔\\٫\\,\\、\\;\\:\\?\\？\\!\\[\\]\\{\\}\\(\\)\\|\\«\\»\\…\\٬\\،\\؛\\؟\\¿\\፤\\፣\\።\\፨\\፠\\፧\\፦\\፡\\…\\।\\¡\\「\\」\\《\\》\\』\\『\\‹\\〔\\〕\\-\\–\\—\\−\\‚\\´\\'\\〉\\〈 \\【\\】\\（\\）\\~\\。\\○\\．\\♪]+") || // - is always PUNCT in our data
			   str.matches("[\\/\\-]{2,}");
	}
	
	public static String splitPunc(String str){
		if(str == null) { return null; }
		if(isNumber(str)) { return str; }
		str =  str.replaceAll(REGEX_NON_LETTER, " $1 ").replaceAll("\\s+", " ").trim();
		str = str.replaceAll("U. S.", "U.S.");
		str = str.replaceAll("U. K.", "U.K.");
		str = str.replaceAll("U. N.", "U.N.");
		return str;
	}
	
	public static String fixTokenization(String text) {
		return text.replaceAll("\\-\\s?(LRB|lrb)\\s?\\-","(").
		replaceAll("\\-\\s?(RRB|rrb)\\s?\\-", ")").
		replaceAll("\\-\\s?(LCB|lcb)\\s?\\-", "{").
		replaceAll("\\-\\s?(RCB|rcb)\\s?\\-", "}").
		replaceAll("\\-\\s?(LSB|lsb)\\s?\\-", "[").
		replaceAll("\\-\\s?(RSB|rsb)\\s?\\-", "]").
   		replace("``", "\"").
   		replace("`", "'").
   		replace("''", "\"").trim();
	}
	
	public static String fixTokenization(String originalText, String text) {
		if(text == null) {
			return null;
		}
		if(!(originalText.contains(". .") || originalText.contains("..")) && text.contains(". .")) {
			text = text.replaceAll("\\.\\s\\.", " .");
		}
		System.out.println(" ".matches("\\s+"));
		text = text.replace(" ", " ");
		
		String[] tokens = text.split("\\s+");
		StringBuilder sb = new StringBuilder();
		for(String token : tokens) {
			token = fixTokenization(token);
			if(token.matches("^"+REGEX_NON_LETTER+"{2,}$")) {
				String splitToken = "";
				for(int i=0; i<token.length(); i++) {
					splitToken += token.charAt(i)+" ";
				}
				token = splitToken.trim();
			}
			sb.append(token+" ");
		}
		return sb.toString().replaceAll("\\s+", " ").trim();
	}
	
}
